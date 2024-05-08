package com.example.activity_tracker_sdk_java.core;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.activity_tracker_sdk_java.BuildConfig;
import com.example.activity_tracker_sdk_java.core.tracker.NetworkTracker;
import com.example.activity_tracker_sdk_java.data.models.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityTrackerManager {
    private static ActivityTrackerManager instance = null;

    private static final String SDK_PREFS = "ActivityTrackerPrefs";
    private static final String USER_ID_KEY = "user_id";
    private boolean isInitialized = false;

    private NetworkTracker tracker = null;

    public static synchronized ActivityTrackerManager getInstance() {
        if (instance == null) {
            instance =  new ActivityTrackerManager();
        }
        return instance;
    }

    public void initialize(Context applicationContext, String serverEndpoint, Integer appId) {
        if (!isInitialized) {
            tracker = new NetworkTracker();
            GlobalVariables.getInstance().setApplicationContext(applicationContext);
            GlobalVariables.getInstance().setServerEndpoint(serverEndpoint);
            GlobalVariables.getInstance().setAppId(appId);

            SharedPreferences prefs = applicationContext.getSharedPreferences(SDK_PREFS, Context.MODE_PRIVATE);
            String userId = prefs.getString(USER_ID_KEY, null);
            if (userId == null) {
                userId = UUID.randomUUID().toString();
                prefs.edit().putString(USER_ID_KEY, userId).apply();
                sendOpenEvent(true);
            } else {
                sendOpenEvent(false);
            }
            GlobalVariables.getInstance().setUserId(userId);
            isInitialized = true;
        }
    }

    private void sendOpenEvent(boolean isFirstOpen) {
        String openEventName = isFirstOpen ? "first_open" : "open";
        Map<String, Object> openEventData = new HashMap<>();
        openEventData.put("version", BuildConfig.VERSION_NAME);

        Event openEvent = new Event(openEventName, openEventData);
        tracker.trackEvent(openEvent);
    }

    public void sendStartLevelEvent(int levelId, @NonNull String levelName) {
        String eventName = "start_level";
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("level_id", levelId);
        eventData.put("level_name", levelName);
        Event event = new Event(eventName, eventData);
        tracker.trackEvent(event);
    }

    public void sendEndLevelEvent(int levelId, @NonNull String levelName, boolean passed) {
        String eventName = passed ? "complete_level" : "fail_level";
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("level_id", levelId);
        eventData.put("level_name", levelName);
        Event event = new Event(eventName, eventData);
        tracker.trackEvent(event);
    }

    public void sendCustomEvent(@NonNull String eventName, @NonNull String eventDataJsonString) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> eventData = new Gson().fromJson(eventDataJsonString, type);
        Event event = new Event(eventName, eventData);
        tracker.trackEvent(event);
    }

    public void shutdown() {
        GlobalVariables.getInstance().reset();
        isInitialized = false;
    }

}
