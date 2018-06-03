package br.com.fellipe.horadorango;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.fellipe.horadorango.util.LocationUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationUtil locationUtil;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationUtil = new LocationUtil(this, mMap);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            LatLng currentLocation = locationUtil.getLatLong();
            if(currentLocation != null) {
                List<Address> addresses = geocoder
                        .getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

                mMap.addMarker(new MarkerOptions().position(currentLocation).title(address));
                mMap.setMinZoomPreference(10.0f);
                mMap.setMaxZoomPreference(20.0f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
