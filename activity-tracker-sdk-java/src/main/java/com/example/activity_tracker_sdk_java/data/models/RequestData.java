package com.example.activity_tracker_sdk_java.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.Objects;

public class RequestData {
    @SerializedName("app_Id")
    private int appId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("events")
    private Event[] events;

    public RequestData(int appId, String userId, Event[] events) {
        this.appId = appId;
        this.userId = userId;
        this.events = events;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestData that = (RequestData) o;
        return appId == that.appId &&
                Objects.equals(userId, that.userId) &&
                Arrays.equals(events, that.events);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(appId, userId);
        result = 31 * result + Arrays.hashCode(events);
        return result;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "appId=" + appId +
                ", userId='" + userId + '\'' +
                ", events=" + Arrays.toString(events) +
                '}';
    }
}
