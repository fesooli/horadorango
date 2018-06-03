package br.com.fellipe.horadorango;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutionException;

import br.com.fellipe.horadorango.api.CepApi;
import br.com.fellipe.horadorango.dao.HoraDoRangoDatabase;
import br.com.fellipe.horadorango.dao.model.Address;
import br.com.fellipe.horadorango.dao.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.fellipe.horadorango.dao.HoraDoRangoDatabase.DATABASE_NAME;

public class AddressActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @BindView(R.id.etCep)
    EditText etCep;

    @BindView(R.id.etNumber)
    EditText etNumber;

    @BindView(R.id.btSave)
    Button btSave;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        final Activity activity = this;

        Bundle extras = getIntent().getExtras();
        final String username = extras.getString("USERNAME");
        final Integer result = extras.getInt("CREATE_ADRESS");

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String number = etNumber.getText().toString();
                final String cep = etCep.getText().toString();

                if(number == "" || cep == "") {
                    Toast.makeText(activity, "Os campos não podem estar em branco", Toast.LENGTH_SHORT).show();
                    return;
                }

                String URL = "https://viacep.com.br/ws/" + cep + "/";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                CepApi service = retrofit.create(CepApi.class);
                final Call<Address> cepCall = service.getCep();
                cepCall.enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        Address address = response.body();
                        address.setNumber(Integer.parseInt(number));

                        User user = new User();
                        user.setUsername(username);

                        if(result == 0) {
                            DatabaseTask databaseTask = new DatabaseTask(address, user);
                            Long result = null;
                            try {
                                result = databaseTask.execute().get();
                                if(result == 0L) {
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else if(result == 1) {
                            if(username == null) {
                                user.setUsername(mAuth.getCurrentUser().getEmail());
                            }
                            DatabaseTaskUpdate databaseTaskUpdate = new DatabaseTaskUpdate(address, user);
                            Long result = null;
                            try {
                                result = databaseTaskUpdate.execute().get();
                                if(result == 0L) {
                                    Toast.makeText(activity, "Endereço atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {

                    }
                });
            }
        });
    }

    public class DatabaseTask extends AsyncTask<Void, Void, Long> {

        private Address address;
        private User user;

        public DatabaseTask(Address address, User user) {
            this.address = address;
            this.user = user;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            Long id = database.addressDAO().insert(address);
            user.setAddressId(id.intValue());
            database.userDAO().insert(user);
            return 0L;
        }
    }

    public class DatabaseTaskUpdate extends AsyncTask<Void, Void, Long> {

        private Address address;
        private User user;

        public DatabaseTaskUpdate(Address address, User user) {
            this.address = address;
            this.user = user;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            this.user = database.userDAO().findByUsername(this.user.getUsername());
            Address oldAddress = database.addressDAO().findByAddressId(user.getAddressId());
            this.address.setId(oldAddress.getId());
            database.addressDAO().update(this.address);
            return 0L;
        }
    }
}
