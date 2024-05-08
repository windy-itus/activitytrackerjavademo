package com.example.activity_tracker_sdk_java.core.tracker;

import android.util.Log;

import com.example.activity_tracker_sdk_java.core.GlobalVariables;
import com.example.activity_tracker_sdk_java.data.models.Event;
import com.example.activity_tracker_sdk_java.data.models.RequestData;
import com.example.activity_tracker_sdk_java.data.network.ApiServiceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NetworkTracker implements BaseTracker {
    private final List<Event> eventCache = new ArrayList<>();
    private volatile boolean isSending = false;
    private final ExecutorService sendExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void trackEvent(Event event) {
        synchronized (eventCache) {
            eventCache.add(event);
        }
        if (!isSending) {
            sendEventsWithDelay();
        }
    }

    @Override
    public void close() {
        eventCache.clear();
    }

    private void sendEventsWithDelay() {
        sendExecutor.submit(() -> {
            try {
                isSending = true;
                while (!eventCache.isEmpty()) {
                    sendNextBatch();
                    TimeUnit.MILLISECONDS.sleep(GlobalVariables.getInstance().getSendDelayInMilliSeconds());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (NullPointerException e) {
                Log.e("Debug", e.toString());
            } finally {
                isSending = false;
            }
        });
    }

    private void sendNextBatch() {
        List<Event> eventsToSend = new ArrayList<>();
        synchronized (eventCache) {
            int batchLimit = GlobalVariables.getInstance().getEventBatchLimit();
            while (eventsToSend.size() < batchLimit && !eventCache.isEmpty()) {
                eventsToSend.add(eventCache.remove(0));
            }
        }
        if (!eventsToSend.isEmpty()) {
            repeatSendingEvents(eventsToSend, maxRetries);
        }
    }

    private void repeatSendingEvents(List<Event> events, int retries) {
        int attempt = 0;
        boolean success = false;

        while (attempt < retries && !success) {
            Log.d("NetworkTracker", "Start send events size=" + events.size() + ", attemptCount=" + attempt);
            Response<ResponseBody> response = sendEvents(events);
            success = response != null && response.code() == 200;
            Log.d("NetworkTracker", "success" + success);
            if (!success) {
                attempt++;
                Log.e("NetworkTracker", "Attempt " + attempt + " failed, retrying...");
                try {
                    TimeUnit.MILLISECONDS.sleep(GlobalVariables.getInstance().getSendDelayInMilliSeconds());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                Log.d("NetworkTracker", "Send successful");
            }
        }
        if (!success) {
            Log.e("NetworkTracker", "All attempts to send events failed");
        }
    }

    private Response<ResponseBody> sendEvents(List<Event> events) {
        Integer appId = GlobalVariables.getInstance().getAppId();
        if (appId == null) {
            throw new IllegalStateException("App ID is not set. Call initialize() first.");
        }
        String userId = GlobalVariables.getInstance().getUserId();
        if (userId == null) {
            throw new IllegalStateException("User ID is not set. Call initialize() first.");
        }
        String serverEndpoint = GlobalVariables.getInstance().getServerEndpoint();
        if (serverEndpoint == null) {
            throw new IllegalStateException("Server endpoint is not set. Call initialize() first.");
        }

        RequestData requestData = new RequestData(appId, userId, events.toArray(new Event[0]));
        try {
            return ApiServiceManager.getEventApiService().sendData(serverEndpoint, requestData).execute().body();
        } catch (Exception ex) {
            Log.e("NetworkTracker", "Network unavailable", ex);
            return null;
        }
    }

    private static final int maxRetries = 3;
}
