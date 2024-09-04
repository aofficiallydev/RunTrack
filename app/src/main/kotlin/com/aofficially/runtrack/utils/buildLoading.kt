package com.aofficially.runtrack.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import com.aofficially.runtrack.R

fun Context.buildLoading(): Dialog {
    return Dialog(this).apply {
        setContentView(R.layout.view_loading)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        setCancelable(false)

    }
}