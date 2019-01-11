package com.gavin.RNAndroidGeolocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;

import javax.annotation.Nullable;

public class LocationReceiver extends BroadcastReceiver {

    private ReactApplicationContext reactContext;

    public LocationReceiver(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        HashMap<String, Object> locationInfo = (HashMap<String, Object>) intent.getSerializableExtra("params");

        WritableMap coords = Arguments.createMap();
        coords.putDouble("latitude", (double) locationInfo.get("latitude"));
        coords.putDouble("longitude", (double) locationInfo.get("longitude"));

        WritableMap params = Arguments.createMap();
        params.putMap("coords", coords);
        this.sendEvent("onLocationChanged", params);
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
