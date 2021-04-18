package com.newscycle.util

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.fragment.app.DialogFragment

class SwipeUpEventListener(val view: DialogFragment) : OnTouchListener {
    enum class Action {
        LR,  // Left to Right
        RL,  // Right to Left
        TB,  // Top to bottom
        BT,  // Bottom to Top
        None // when no action was detected
    }

    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f
    var action = Action.None
        private set

    fun swipeDetected(): Boolean {
        return action != Action.None
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                action = Action.None
                return false // allow other events like Click to be processed
            }
            MotionEvent.ACTION_MOVE -> {
                upX = event.x
                upY = event.y
                val deltaX = downX - upX
                val deltaY = downY - upY

                // horizontal swipe detection
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        Log.i(logTag, "Swipe Left to Right")
                        action = Action.LR
                        return false
                    }
                    if (deltaX > 0) {
                        Log.i(logTag, "Swipe Right to Left")
                        action = Action.RL
                        return false
                    }
                } else if (Math.abs(deltaY) > MIN_DISTANCE) { // vertical swipe
                    // detection
                    // top or down
                    if (deltaY < 0) {
                        Log.i(logTag, "Swipe Top to Bottom")
                        action = Action.TB
                        view.dismiss()
                        return false
                    }
                    if (deltaY > 0) {
                        Log.i(logTag, "Swipe Bottom to Top")
                        action = Action.BT
                        view.dismiss()
                        return false
                    }
                }
                return false
            }
        }
        return false
    }

    companion object {
        private const val logTag = "SwipeDetector"
        private const val MIN_DISTANCE = 100
    }
}