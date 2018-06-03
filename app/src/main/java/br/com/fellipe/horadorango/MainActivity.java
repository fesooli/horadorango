package br.com.fellipe.horadorango;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import br.com.fellipe.horadorango.fragments.AboutFragment;
import br.com.fellipe.horadorango.fragments.FoodFragment;
import br.com.fellipe.horadorango.fragments.OrdersFragment;
import br.com.fellipe.horadorango.util.LocationUtil;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.food_list_fragment:
                    fragmentTransaction.replace(R.id.fl_fragment, new FoodFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.middle_fragment:
                    fragmentTransaction.replace(R.id.fl_fragment, new OrdersFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.about_fragment:
                    fragmentTransaction.replace(R.id.fl_fragment, new AboutFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationUtil locationUtil = new LocationUtil(this);
        locationUtil.requestGPSPermission();

        BottomNavigationItemView bottomNavigationItemView = findViewById(R.id.food_list_fragment);
        mOnNavigationItemSelectedListener.onNavigationItemSelected(bottomNavigationItemView.getItemData());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
