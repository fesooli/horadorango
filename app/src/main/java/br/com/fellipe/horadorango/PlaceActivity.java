package br.com.fellipe.horadorango;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.fellipe.horadorango.adapters.ListViewAdapter;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.model.Place;
import br.com.fellipe.horadorango.util.CallUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceActivity extends AppCompatActivity {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        mRecyclerView = findViewById(R.id.listMenu);
        order.setItems(new ArrayList<String>());
        Bundle extras = getIntent().getExtras();
        final Place place = (Place) extras.getSerializable("PLACE");
        final Activity activity = this;
        tvName.setText(place.getName());
        tvAddress.setText(place.getAddress());
        tvPhone.setText(place.getPhone());
        order.setUrlPlaceImage(place.getImage());
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new ListViewAdapter(place.getMenu(), this, place, order));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order.getItems().isEmpty()) {
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("ORDER", order);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity, "Por favor, coloque pelo menos 1 item no carrinho", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallUtil.checkPermissionPhoneCall(activity, tvPhone.getText().toString());
            }
        });
    }
}
