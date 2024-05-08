package com.example.activity_tracker_java_demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.activity_tracker_sdk_java.core.ActivityTrackerManager
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnInitialize = findViewById<Button>(R.id.btnInitializeSDK)
        btnInitialize.setOnClickListener {
            // Call SDK initialize function
            ActivityTrackerManager.getInstance().initialize(
                applicationContext,
                "https://trackingdemo-server.onrender.com/events/",
                230820,
            )
        }

        val btnShutdown = findViewById<Button>(R.id.btnShutdownSDK)
        btnShutdown.setOnClickListener {
            ActivityTrackerManager.getInstance().shutdown()
        }

        val btnSendStartLevelEvent = findViewById<Button>(R.id.btnSendStartLevelEvent)
        btnSendStartLevelEvent.setOnClickListener {
            ActivityTrackerManager.getInstance().sendStartLevelEvent(1, "beginner")
        }

        val btnSendEndLevelPassEvent = findViewById<Button>(R.id.btnSendEndLevelPassEvent)
        btnSendEndLevelPassEvent.setOnClickListener {
            ActivityTrackerManager.getInstance().sendEndLevelEvent(1, "beginner", true)
        }

        val btnSendEndLevelFailedEvent = findViewById<Button>(R.id.btnSendEndLevelFailedEvent)
        btnSendEndLevelFailedEvent.setOnClickListener {
            ActivityTrackerManager.getInstance().sendEndLevelEvent(1, "beginner", false)
        }

        val btnSendCustomEvent = findViewById<Button>(R.id.btnSendCustomEvent)
        btnSendCustomEvent.setOnClickListener {
            ActivityTrackerManager.getInstance().sendCustomEvent("DummyEvent", mapOf("key" to "Hello world").toJsonString())
        }

        val btnSendManyEvents = findViewById<Button>(R.id.btnSendManyEvents)
        btnSendManyEvents.setOnClickListener {
            sendManyEvents()
        }
    }

    private fun sendManyEvents() {
        for (i in 1..100) { // Number of events you want to send
            ActivityTrackerManager.getInstance().sendCustomEvent("DummyEvent", mapOf("eventNumber" to i).toJsonString())
        }
    }
}

fun Map<String, Any>.toJsonString(): String {
    return Gson().toJson(this)
}