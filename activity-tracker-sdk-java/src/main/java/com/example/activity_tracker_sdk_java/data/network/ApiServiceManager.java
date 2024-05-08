package com.example.activity_tracker_sdk_java.data.network;

import android.content.Context;

import com.example.activity_tracker_sdk_java.core.GlobalVariables;

public class ApiServiceManager {
    private static EventApiService eventApiService = null;

    public static EventApiService getEventApiService() {
        if (eventApiService == null) {
            synchronized (ApiServiceManager.class) {
                if (eventApiService == null) { // double-checked locking
                    String baseUrl = GlobalVariables.getInstance().getServerEndpoint();
                    if (baseUrl == null) {
                        throw new IllegalStateException("Server endpoint is not set. Call initialize() first.");
                    }
                    Context context = GlobalVariables.getInstance().getApplicationContext();
                    if (context != null) {
                        eventApiService = new ServiceBuilder()
                                .setBaseUrl(baseUrl)
                                .createService(EventApiService.class);
                    }
                }
            }
        }
        return eventApiService;
    }
}
