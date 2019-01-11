
package com.gavin.RNAndroidGeolocationService;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.annotation.Nullable;

public class RNAndroidGeolocationServiceModule extends ReactContextBaseJavaModule {

    private boolean isStopOnTerminate;
    private long interval;
    private long fastestInterval;
    private float distanceFilter;
    private int priority;

    private final String IS_STOP_ON_TERMINATE_JS_KEY = "stopOnTerminate";
    private final String INTERVAL_JS_KEY = "interval";
    private final String FASTEST_INTERVAL_JS_KEY = "fastestInterval";
    private final String DISTANCE_FILTER_JS_KEY = "distanceFilter";
    private final String PRIORITY_JS_KEY = "priority";

    private final int PERMISSIONS_DENIED_ERROR = 40001;
    private final int PRIORITY_NOT_SUPPORTED_ERROR = 40002;
    private final int GOOGLE_PLAY_SERVICE_UPDATING = 40003;
    private final int GOOGLE_PLAY_SERVICE_NEED_UPDATE = 40004;
    private final int GOOGLE_PLAY_SERVICE_ERROR = 40005;


    public RNAndroidGeolocationServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);

        LocationReceiver receiver = new LocationReceiver(reactContext);
        IntentFilter filter = new IntentFilter(LocationService.BROADCAST_ACTION);
        reactContext.registerReceiver(receiver, filter);
    }

    @Override
    public String getName() {
        return "GeolocationService";
    }

    @ReactMethod
    public void start(@Nullable final ReadableMap options) {
        if (getReactApplicationContext() == null) {
            Log.e("LocationServiceModule", "ReactApplicationContext is null");
            return;
        }

        int resultForGooglePlayServices = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getReactApplicationContext());

        if (resultForGooglePlayServices == ConnectionResult.SERVICE_UPDATING) {
            sendError(GOOGLE_PLAY_SERVICE_UPDATING, "GooglePlayServices is updating.");
            return;
        } else if (resultForGooglePlayServices == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            sendError(GOOGLE_PLAY_SERVICE_NEED_UPDATE, "GooglePlayServices needs to update.");
            return;
        } else if (resultForGooglePlayServices != ConnectionResult.SUCCESS) {
            sendError(GOOGLE_PLAY_SERVICE_ERROR, "GooglePlayServices is unavailable");
            return;
        }

        if (!hasPermissions()) {
            sendError(PERMISSIONS_DENIED_ERROR, "Need request permissions.");
            return;
        }

        if (!loadOptionValuesFromReact(options)) {
            return;
        }

        startLocationService(isStopOnTerminate);

    }

    @ReactMethod
    public void stop() {
        if (getReactApplicationContext() == null) {
            Log.e("LocationServiceModule", "ReactApplicationContext is null");
            return;
        }
        Intent serviceIntent = new Intent(getReactApplicationContext(), LocationService.class);
        getReactApplicationContext().getApplicationContext().stopService(serviceIntent);
    }

    private void sendError(int errorCode, String errorMessage) {

        WritableMap params = Arguments.createMap();
        params.putInt("code", errorCode);
        params.putString("message", errorMessage);

        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onError", params);
    }

    private void startLocationService(final boolean isNeedStop) {
        Intent serviceIntent = new Intent(getReactApplicationContext(), LocationService.class);
        serviceIntent.putExtra(LocationService.INTERVAL_EXTRA_KEY, interval);
        serviceIntent.putExtra(LocationService.FASTEST_INTERVAL_EXTRA_KEY, fastestInterval);
        serviceIntent.putExtra(LocationService.DISTANCE_FILTER_EXTRA_KEY, distanceFilter);
        serviceIntent.putExtra(LocationService.PRIORITY_EXTRA_KEY, priority);

        getReactApplicationContext().getApplicationContext().startService(serviceIntent);

        getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {

            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {
                if (isNeedStop) {
                    stop();
                }
            }
        });

    }

    private boolean loadOptionValuesFromReact(ReadableMap options) {

        // setting default options value
        isStopOnTerminate = true;
        priority = LocationService.PRIORITY_BALANCED_POWER_ACCURACY;
        interval = LocationService.INTERVAL_DEFAULT_VALUE;
        fastestInterval = LocationService.FASTEST_INTERVAL_DEFAULT_VALUE;
        distanceFilter = LocationService.DISTANCE_FILTER_DEFAULT_VALUE;

        // load values from map if has setting
        if (options.hasKey(PRIORITY_JS_KEY)) {
            int priorityValue = options.getInt(PRIORITY_JS_KEY);
            if (priorityValue == LocationService.PRIORITY_BALANCED_POWER_ACCURACY
                    || priorityValue == LocationService.PRIORITY_HIGH_ACCURACY
                    || priorityValue == LocationService.PRIORITY_LOW_POWER
                    || priorityValue == LocationService.PRIORITY_NO_POWER) {
                priority = priorityValue;
            } else {
                sendError(PRIORITY_NOT_SUPPORTED_ERROR, "The priority not supported.");
                return false;
            }
        }

        if (options.hasKey(IS_STOP_ON_TERMINATE_JS_KEY)) {
            isStopOnTerminate = options.getBoolean(IS_STOP_ON_TERMINATE_JS_KEY);
        }

        if (options.hasKey(INTERVAL_JS_KEY)) {
            interval = options.getInt(INTERVAL_JS_KEY);
        }

        if (options.hasKey(FASTEST_INTERVAL_JS_KEY)) {
            fastestInterval = options.getInt(FASTEST_INTERVAL_JS_KEY);
        }

        if (options.hasKey(DISTANCE_FILTER_JS_KEY)) {
            distanceFilter = (float) options.getDouble(DISTANCE_FILTER_JS_KEY);
        }

        return true;
    }

    private boolean hasPermissions() {
        if (ActivityCompat.checkSelfPermission(getReactApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getReactApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

}