package com.aofficially.runtrack.ui.home.presentation.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.aofficially.runtrack.databinding.LayoutLogoutBottomSheetBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.ViewGroup as ViewGroup1

class LogoutBottomSheetDialog(private val isShowResetButton: Boolean) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutLogoutBottomSheetBinding

    var onRefreshRunner: (() -> Unit)? = null
    var onLogoutListener: (() -> Unit)? = null
    var onResetListener: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup1?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutLogoutBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setup() {
        if (isShowResetButton) {
            binding.btnReset.gone()
        } else {
            binding.btnReset.visible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        binding.btnLogout.setOnClickWithDebounce {
            onLogoutListener?.invoke()
        }

        binding.btnRefresh.setOnClickWithDebounce {
            onRefreshRunner?.invoke()
        }

        binding.btnReset.setOnClickWithDebounce {
            onResetListener?.invoke()
        }
    }
}