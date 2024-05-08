package com.example.activity_tracker_sdk_java.core.tracker;

import com.example.activity_tracker_sdk_java.data.models.Event;

interface BaseTracker {
    void trackEvent(Event event);
     void close();
}
