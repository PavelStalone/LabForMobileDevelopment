package com.example.lab1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.lab1.ui.theme.AppTheme

class MainActivity : LogActivity() {

    override val TAG: String = "MainActivity"

    private var timestamp: Long = 0L
    private var count: Int = 0

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback {
            if (System.currentTimeMillis() - timestamp <= BACK_TIMEOUT) {
                count++

                if (count >= COUNT_TO_EXIT) {
                    finish()
                }
            } else {
                count = 1
            }

            timestamp = System.currentTimeMillis()
            toast?.cancel()
            toast = Toast.makeText(
                applicationContext,
                "Click ${COUNT_TO_EXIT - count} for exit",
                Toast.LENGTH_SHORT
            )
            toast?.show()
        }

        enableEdgeToEdge()
        setContent {
            AppTheme {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        FragmentContainerView(context).apply {
                            id = containerId
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                        }
                    }
                )

                LaunchedEffect(Unit) {
                    supportFragmentManager.beginTransaction()
                        .add(containerId, OnboardFragment())
                        .commit()
                }
            }
        }
    }

    companion object {

        private const val BACK_TIMEOUT = 5000L
        private const val COUNT_TO_EXIT = 5

        private val containerId = View.generateViewId()

        fun navigateToFragment(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            args: Bundle? = null,
        ) {
            fragmentManager.commit {
                add(containerId, fragment::class.java, args)
                addToBackStack(fragment.toString())
            }
        }
    }
}
