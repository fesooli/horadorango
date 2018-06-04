package br.com.fellipe.horadorango;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import br.com.fellipe.horadorango.dao.HoraDoRangoDatabase;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.dao.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.fellipe.horadorango.dao.HoraDoRangoDatabase.DATABASE_NAME;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btCreateAccount)
    Button btCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login(View v) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login ou senha inválidos!",
                    Toast.LENGTH_SHORT).show();
        } else {
            final Context context = this;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                User userDao = new User();
                                userDao.setUsername(user.getEmail());
                                DatabaseTask databaseTask = new DatabaseTask(userDao);
                                User userReturned = null;
                                try {
                                    userReturned = databaseTask.execute().get();
                                    if(userReturned == null) {
                                        Intent intent = new Intent(context, AddressActivity.class);
                                        intent.putExtra("CREATE_ADRESS", 0);
                                        startActivity(intent);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                if (user != null && userReturned != null) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Erro ao realizar o login " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
        }
    }

    public class DatabaseTask extends AsyncTask<Void, Void, User> {
        private User user;

        public DatabaseTask(User user) {
            this.user = user;
        }

        @Override
        protected User doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            return database.userDAO().findByUsername(user.getUsername());
        }
    }

    public class DatabaseTaskInsert extends AsyncTask<Void, Void, Long> {
        private User user;

        public DatabaseTaskInsert(User user) {
            this.user = user;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            database.userDAO().insert(user);
            return 0L;
        }
    }

}

