package com.aofficially.runtrack.utils.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.View

interface DialogUtility {
    fun showAlertDialog(
        context: Context,
        title: String = "",
        message: String = "",
        positiveText: String = "",
        negativeText: String = "",
        cancelable: Boolean = false,
        onPositive: (() -> Unit) = {},
        onNegative: (() -> Unit) = {},
        contentAlignment: Int = View.TEXT_ALIGNMENT_CENTER
    ): AlertDialog
}