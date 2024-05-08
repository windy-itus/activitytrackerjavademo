package com.example.activity_tracker_sdk_java.data.models;

import java.util.Map;
import java.util.Objects;

public class Event {
    private final String name;
    private final Map<String, Object> data;

    public Event(String name, Map<String, Object> data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) &&
                Objects.equals(data, event.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}

