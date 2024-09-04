package com.aofficially.runtrack.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.aofficially.runtrack.R
import com.aofficially.runtrack.utils.SingleEvent
import com.aofficially.runtrack.utils.buildLoading
import com.aofficially.runtrack.utils.dialog.DialogUtility
import javax.inject.Inject

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    @LayoutRes val layoutId: Int,
    private val inflate: Inflate<VB>
) : Fragment(layoutId) {

    lateinit var binding: VB
    private var loadingDialog: Dialog? = null

    private fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = inflate.invoke(inflater, container, false)
    }

    @Inject
    lateinit var dialogUtility: DialogUtility

    abstract fun initView()
    abstract fun observeViewModel()
    abstract fun setupListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        init(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
        setupListener()
        loadingDialog = requireActivity().buildLoading()
    }

    private fun startLoading() {
        loadingDialog?.show()
    }

    private fun stopLoading() {
        loadingDialog?.dismiss()
    }

    fun initLoadingView(stateLoading: LiveData<SingleEvent<Boolean>>) {
        stateLoading.observe(this) {
            it.consume()?.let { isLoading ->
                if (isLoading) {
                    startLoading()
                } else {
                    stopLoading()
                }
            }
        }
    }

    fun sendingExternal(keywordShare: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, keywordShare)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun showDialogError(message: String, onButtonClick: (() -> Unit?)? = null) {
        dialogUtility.showAlertDialog(
            context = requireContext(),
            message = message,
            title = getString(R.string.app_name),
            positiveText = getString(R.string.common_ok),
            onPositive = {
                onButtonClick?.invoke()
            }
        )
    }
}