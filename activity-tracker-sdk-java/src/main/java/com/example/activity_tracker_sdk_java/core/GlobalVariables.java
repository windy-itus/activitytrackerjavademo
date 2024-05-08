package com.example.activity_tracker_sdk_java.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class GlobalVariables {
    private static final GlobalVariables INSTANCE = new GlobalVariables();
    private @Nullable WeakReference<Context> applicationContextReference;
    private @Nullable String serverEndpoint;
    private @Nullable Integer appId;
    private @Nullable String userId;
    private int eventBatchLimit = 10;
    private long sendDelayInMilliSeconds = 500L;

    private GlobalVariables() { } // Private constructor to enforce singleton

    @NonNull
    public static GlobalVariables getInstance() {
        return INSTANCE;
    }

    public void setApplicationContext(@NonNull Context context) {
        Context applicationContext = context.getApplicationContext();
        if (applicationContext == null) {
            return;
        }
        this.applicationContextReference = new WeakReference<>(applicationContext);
    }

    @Nullable
    public Context getApplicationContext() {
        return applicationContextReference != null ? applicationContextReference.get() : null;
    }

    public void setServerEndpoint(@NonNull String endpoint) {
        this.serverEndpoint = endpoint;
    }

    @Nullable
    public String getServerEndpoint() {
        return serverEndpoint;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Nullable
    public Integer getAppId() {
        return appId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setEventBatchLimit(int eventBatchLimit) {
        this.eventBatchLimit = eventBatchLimit;
    }

    public int getEventBatchLimit() {
        return eventBatchLimit;
    }

    public void setSendDelayInMilliSeconds(long sendDelayInMilliSeconds) {
        this.sendDelayInMilliSeconds = sendDelayInMilliSeconds;
    }

    public long getSendDelayInMilliSeconds() {
        return sendDelayInMilliSeconds;
    }

    public void reset() {
        applicationContextReference = null;
        serverEndpoint = null;
        appId = null;
        userId = null;
        eventBatchLimit = 0;
        sendDelayInMilliSeconds = 0;
    }
}
