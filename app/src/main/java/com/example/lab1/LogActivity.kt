package com.example.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

abstract class LogActivity : ComponentActivity() {

    protected abstract val TAG: String

    private val lifeCycleObserver = object : LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            Log.d(TAG, "onStateChanged: $event")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.addObserver(lifeCycleObserver)

        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(lifeCycleObserver)

        super.onDestroy()
    }
}
