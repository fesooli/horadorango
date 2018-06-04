package br.com.fellipe.horadorango.util;

import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.google.android.gms.location.LocationSettingsStatusCodes.RESOLUTION_REQUIRED;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SUCCESS;

/**
 * Created by fellipe on 31/05/18.
 */

public class LocationUtil {

    private Activity activity;
    private GoogleMap mMap;

    public LocationUtil(Activity activity) {
        this.activity = activity;
    }

    public LocationUtil(Activity activity, GoogleMap mMap) {
        this.activity = activity;
        this.mMap = mMap;
    }

    public LatLng getLatLong() {
        if (ActivityCompat
                .checkSelfPermission(activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat
                .checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            if(mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
            LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                return new LatLng(latitude, longitude);
            } else {
                location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                if (location != null) {
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    return new LatLng(latitude, longitude);
                }

                return null;
            }
        }
        return null;
    }

    public void requestGPSPermission() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        connectionResult.getErrorMessage();
                    }
                }).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final com.google.android.gms.common.api.Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case SUCCESS:
                        break;
                    case RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

}
