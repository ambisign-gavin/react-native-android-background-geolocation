package com.gavin.RNAndroidGeolocationService;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;

import javax.annotation.Nullable;

public class LocationService extends Service {

    public static final String BROADCAST_ACTION = "com.gavin.RNAndroidGeolocationService.LOCATION_CHANGED";

    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 10001;
    public static final int PRIORITY_HIGH_ACCURACY = 10002;
    public static final int PRIORITY_LOW_POWER = 10003;
    public static final int PRIORITY_NO_POWER = 10004;

    public static final long INTERVAL_DEFAULT_VALUE = 20 * 1000; // 20 sec
    public static final long FASTEST_INTERVAL_DEFAULT_VALUE = 15* 1000; //15 sec
    public static final float DISTANCE_FILTER_DEFAULT_VALUE = 100; // 100m

    public static final String INTERVAL_EXTRA_KEY = "interval";
    public static final String FASTEST_INTERVAL_EXTRA_KEY = "fastestInterval";
    public static final String DISTANCE_FILTER_EXTRA_KEY = "distanceFilter";
    public static final String PRIORITY_EXTRA_KEY = "priorityMode";

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                handleLocationChanged(location);
            }
        }
    };

    public void handleLocationChanged(Location location) {

        ReactInstanceManager mReactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
        ReactContext context = mReactInstanceManager.getCurrentReactContext();

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("latitude", location.getLatitude());
        result.put("longitude", location.getLongitude());

        final HashMap<String, Object> params = result;

        if (context == null) {
            if (!mReactInstanceManager.hasStartedCreatingInitialContext()) {
                mReactInstanceManager.createReactContextInBackground();
            }
            mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    sendLocationChangedBroadcast(params);
                }
            });

        } else {
            sendLocationChangedBroadcast(params);
        }

    }

    private void sendLocationChangedBroadcast(HashMap<String, Object> params) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION);
        intent.putExtra("params", params);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY;
        }

        LocationRequest locationRequest = LocationRequest.create();

        switch (intent.getIntExtra(PRIORITY_EXTRA_KEY, PRIORITY_BALANCED_POWER_ACCURACY)) {
            case PRIORITY_BALANCED_POWER_ACCURACY:
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                break;
            case PRIORITY_HIGH_ACCURACY:
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                break;
            case PRIORITY_LOW_POWER:
                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                break;
            case PRIORITY_NO_POWER:
                locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
                break;
        }

        locationRequest.setInterval(intent.getLongExtra(INTERVAL_EXTRA_KEY, INTERVAL_DEFAULT_VALUE));
        locationRequest.setFastestInterval(intent.getLongExtra(FASTEST_INTERVAL_EXTRA_KEY, FASTEST_INTERVAL_DEFAULT_VALUE));
        locationRequest.setSmallestDisplacement(intent.getFloatExtra(DISTANCE_FILTER_EXTRA_KEY, DISTANCE_FILTER_DEFAULT_VALUE));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

}
