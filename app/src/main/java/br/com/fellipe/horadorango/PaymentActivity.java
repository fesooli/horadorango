package br.com.fellipe.horadorango;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import br.com.fellipe.horadorango.api.PlacesApi;
import br.com.fellipe.horadorango.dao.HoraDoRangoDatabase;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.dao.model.User;
import br.com.fellipe.horadorango.model.Payment;
import br.com.fellipe.horadorango.util.LocationUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.fellipe.horadorango.dao.HoraDoRangoDatabase.DATABASE_NAME;

public class PaymentActivity extends AppCompatActivity implements android.location.LocationListener{

    private FirebaseAuth mAuth;

    @BindView(R.id.tvDeliveryAddress)
    TextView tvDeliveryAddress;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.tvOrderItems)
    TextView tvOrderItems;

    @BindView(R.id.ivPlaceImage)
    ImageView ivPlaceImage;

    @BindView(R.id.rgPaymentForm)
    RadioGroup rgPaymentForm;

    @BindView(R.id.layoutDelivery)
    LinearLayout linearLayout;

    @BindView(R.id.btCloseOrder)
    Button btCloseOrder;

    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        final Activity activity = this;

        Bundle bundle = getIntent().getExtras();
        final Order order = (Order) bundle.getSerializable("ORDER");
        tvTotalPrice.setText("Total: R$" + order.getTotalPrice().toString());
        for(String item : order.getItems()) {
            String items = tvOrderItems.getText().toString();
            tvOrderItems.setText(items + "1 " + item + "\n");
        }
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final DatabaseTask databaseTask = new DatabaseTask(firebaseUser.getEmail(), order);
        try {
            br.com.fellipe.horadorango.dao.model.Address address = databaseTask.execute().get();
            order.setDeliveryAddressId(address.getId());
            tvDeliveryAddress.setText(address.getStreet());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.mocky.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlacesApi service = retrofit.create(PlacesApi.class);
        final Call<Payment> paymentCall = service.listPaymentAndDeliveryAddress();
        paymentCall.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, final Response<Payment> response) {
                Payment payment = response.body();
                try {
                    LocationUtil locationUtil = new LocationUtil(activity);
                    LatLng latLng = locationUtil.getLatLong();

                    Double latitude, longitude;
                    if(latLng != null) {
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;

                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        List<Address> addresses = geocoder
                                .getFromLocation(latitude, longitude, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        tvDeliveryAddress.setText(address);
                    } else {
                        if(((PaymentActivity) activity).location != null) {
                            latitude = ((PaymentActivity) activity).location.getLatitude();
                            longitude = ((PaymentActivity) activity).location.getLongitude();

                            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                            List<Address> addresses = geocoder
                                    .getFromLocation(latitude, longitude, 1);
                            String address = addresses.get(0).getAddressLine(0);
                            tvDeliveryAddress.setText(address);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < payment.getPaymentForm().getCardBrand().size(); i++) {
                    String card = payment.getPaymentForm().getCardBrand().get(i);
                    RadioButton radioButton = new RadioButton(activity);
                    radioButton.setId(i);
                    radioButton.setText(card);
                    rgPaymentForm.addView(radioButton);
                }

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, MapsActivity.class);
                        activity.startActivity(intent);
                    }
                });

                btCloseOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton radioButton = activity.findViewById(rgPaymentForm.getCheckedRadioButtonId());
                        order.setPaymentForm(radioButton.getText().toString());
                        DatabaseTaskInsert databaseTaskInsert = new DatabaseTaskInsert(order);
                        try {
                            Long result = databaseTaskInsert.execute().get();
                            if(result == 0L) {
                                Intent intent = new Intent(activity, OrderCompletedActivity.class);
                                activity.startActivity(intent);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public class DatabaseTask extends AsyncTask<Void, Void, br.com.fellipe.horadorango.dao.model.Address> {
        private String email;
        private Order order;

        public DatabaseTask(String email, Order order) {
            this.email = email;
            this.order = order;
        }

        @Override
        protected br.com.fellipe.horadorango.dao.model.Address doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            User user = database.userDAO().findByUsername(this.email);

            if(user.getId() == null) {
                Long id = database.userDAO().insert(user);
                order.setUserId(id.intValue());
            } else {
                order.setUserId(user.getId());
            }
            return database.addressDAO().findByAddressId(user.getAddressId());
        }
    }

    public class DatabaseTaskInsert extends AsyncTask<Void, Void, Long> {
        private Order order;

        public DatabaseTaskInsert(Order order) {
            this.order = order;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            getApplicationContext(),
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            database.orderDAO().insert(order);
            return 0L;
        }
    }
}
