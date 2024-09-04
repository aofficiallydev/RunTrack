package com.aofficially.runtrack.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.core.view.updatePadding
import com.aofficially.runtrack.R
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar

object NewNotificationUtil {

    @JvmStatic
    @JvmOverloads
    fun showCustomNotification(
        activity: Activity,
        runBib: String,
        runName: String,
        runTime: String,
        runGroup: String,
        runDate: String,
        isInRace: Boolean
    ) {
        val snackBar: Snackbar = Snackbar.make(activity.window.decorView, "", Snackbar.LENGTH_SHORT)
        val layout =
            activity.layoutInflater.inflate(R.layout.layout_notification_view, null) as ConstraintLayout
        val tvBib = layout.findViewById<View>(R.id.tvBib) as AppCompatTextView
        val tvName = layout.findViewById<View>(R.id.tvName) as AppCompatTextView
        val tvTime = layout.findViewById<View>(R.id.tvTime) as AppCompatTextView
        val tvGroup = layout.findViewById<View>(R.id.tvGroup) as AppCompatTextView
        val tvDate = layout.findViewById<View>(R.id.tvDate) as AppCompatTextView

        if (isInRace) {
            layout.setBackgroundResource(R.drawable.round12_green)
        } else {
            layout.setBackgroundResource(R.drawable.round12_red)
        }
        tvBib.text = runBib
        tvName.text = runName
        tvTime.text = runTime
        tvGroup.text = runGroup
        tvDate.text = runDate


        val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
        snackBar.view.apply {
            this.layoutParams = layoutParams
            snackBar.view.setBackgroundColor(Color.TRANSPARENT)
            activity.findViewById<View>(android.R.id.content).doOnAttach {
                val top = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        activity.window.decorView.rootWindowInsets.getInsets(WindowInsets.Type.systemBars()).top
                    }

                    else -> {
                        activity.window.decorView.rootWindowInsets.stableInsetTop
                    }
                }
                updatePadding(top = top)
                snackBar.animationMode = ANIMATION_MODE_FADE
                (this as FrameLayout).addView(layout)
                snackBar.show()
            }
        }
    }
}