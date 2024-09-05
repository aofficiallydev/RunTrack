package com.aofficially.runtrack.ui.addtracking.manual.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aofficially.runtrack.databinding.LayoutSecondTimePickerBinding
import com.aofficially.runtrack.extensions.getCurrentDateTime
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SecondBottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutSecondTimePickerBinding

    private var secondSelected = getCurrentDateTime("ss").toInt()
    var onSelectedListener: ((Int) -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSecondTimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                behavior.maxHeight = 200
                behavior.peekHeight = 200
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numberPicker.apply {
            value = getCurrentDateTime("ss").toInt()
            setOnValueChangedListener { _, _, newVal ->
                secondSelected = newVal
            }
        }

        binding.btnOk.setOnClickWithDebounce {
            onSelectedListener?.invoke(secondSelected)
            dismiss()
        }
    }
}