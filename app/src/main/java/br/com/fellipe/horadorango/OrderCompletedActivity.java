package br.com.fellipe.horadorango;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import br.com.fellipe.horadorango.dao.model.Order;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderCompletedActivity extends AppCompatActivity {

    @BindView(R.id.ibShare)
    ImageButton ibShare;

    @BindView(R.id.ibBack)
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        final Order order = (Order) bundle.get("ORDER");

        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(order);
            }
        });

        final Activity activity = this;

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    private void share(Order order) {
        String text = "Olhe o prato que acabei de de pedir no #HoraDoRango "
                + order.getItems()
                + "\n\nBaixe o APP do #HoraDoRango e não passe mais vontade!";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
