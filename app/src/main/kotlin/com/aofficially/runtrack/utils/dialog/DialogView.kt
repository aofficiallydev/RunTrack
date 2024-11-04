package com.aofficially.runtrack.utils.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.aofficially.runtrack.databinding.CommonDialogBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.extensions.visible

object DialogView : DialogUtility {

    private var binding: ViewBinding? = null

    override fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        cancelable: Boolean,
        onPositive: ((String) -> Unit),
        onNegative: (() -> Unit),
        contentAlignment: Int,
        isShowEditText: Boolean,
        informEditText: String
    ): AlertDialog {
        binding = getLayout(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding?.root)
        builder.setCancelable(cancelable)
        val alert = builder.create()
        setButtonListener(
            title,
            message,
            positiveText,
            onPositive,
            alert,
            negativeText,
            onNegative,
            isShowEditText,
            informEditText
        )
        alert.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        alert.show()
        return alert
    }

    private fun getLayout(inflater: LayoutInflater): ViewBinding {
        return CommonDialogBinding.inflate(inflater, null, false)
    }

    private fun setButtonListener(
        title: String,
        message: String,
        positiveText: String,
        onPositive: ((String) -> Unit)?,
        alert: AlertDialog,
        negativeText: String,
        onNegative: (() -> Unit)?,
        isShowEditText: Boolean,
        informEditText: String
    ) {

        if (binding is CommonDialogBinding) {
            val dialogBinding = binding as CommonDialogBinding

            if (isShowEditText) {
                dialogBinding.tvTitle.gone()
                dialogBinding.tvDescription.gone()
                dialogBinding.edtDomain.visible()
                dialogBinding.edtDomain.setText(informEditText)
            } else {
                dialogBinding.tvTitle.visible()
                dialogBinding.tvDescription.visible()
                dialogBinding.edtDomain.gone()

                dialogBinding.tvTitle.text = title
                dialogBinding.tvDescription.text = message
            }
            if (positiveText.isNotEmpty()) {
                dialogBinding.btnPositive.text = positiveText
                dialogBinding.btnPositive.setOnClickWithDebounce {
                    onPositive?.invoke(dialogBinding.edtDomain.text.toString())
                    alert.dismiss()
                }
                dialogBinding.btnPositive.visible()
            } else {
                dialogBinding.btnPositive.gone()
            }

            if (negativeText.isNotEmpty()) {
                dialogBinding.btnNegative.text = negativeText
                dialogBinding.btnNegative.setOnClickWithDebounce {
                    onNegative?.invoke()
                    alert.dismiss()
                }
                dialogBinding.btnNegative.visible()
            } else {
                dialogBinding.btnNegative.gone()
                dialogBinding.line2.gone()
            }
        }
    }
}