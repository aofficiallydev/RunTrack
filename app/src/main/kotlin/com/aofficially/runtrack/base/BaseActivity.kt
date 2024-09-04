package com.aofficially.runtrack.base

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.aofficially.runtrack.R
import com.aofficially.runtrack.extensions.viewBinding
import com.aofficially.runtrack.utils.SingleEvent
import com.aofficially.runtrack.utils.buildLoading
import com.aofficially.runtrack.utils.dialog.DialogUtility
import javax.inject.Inject
import kotlin.math.ceil


abstract class BaseActivity<VB : ViewBinding>(inflate: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    @Inject
    lateinit var dialogUtility: DialogUtility

    protected open val binding: VB by viewBinding(inflate)

    private var loadingDialog: Dialog? = null

    abstract fun initView()
    abstract fun observeViewModel()
    abstract fun setupListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadingDialog = this.buildLoading()
        setStatusBar()
        initView()
        observeViewModel()
        setupListener()
    }

    private fun getStatusBarHeight(): Int {
        val resourceId: Int = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else ceil((if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) 24 else 25) * resources.displayMetrics.density)
            .toInt()
    }

    private fun setStatusBar() {
        binding.root.setPadding(0, getStatusBarHeight(), 0, 0)
    }

    private fun showInformationDialog(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val isTouchOnView = { view: View, event: MotionEvent ->
            val location = IntArray(2).apply(view::getLocationInWindow)
            val xLocation = location[0]
            val endXLocation = view.width + xLocation
            val yLocation = location[1]
            val endYLocation = view.height + yLocation
            event.x.toInt() in (xLocation..endXLocation) && event.y.toInt() in (yLocation..endYLocation)
        }
        if (MotionEvent.ACTION_DOWN == ev?.action) {
            currentFocus?.takeIf { it is EditText }
                ?.takeUnless { isTouchOnView(it, ev) }?.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }

    fun showLoading() {
        loadingDialog?.let {
            if (it.isShowing.not()) {
                it.show()
            }
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }

    fun initLoadingView(stateLoading: LiveData<SingleEvent<Boolean>>) {
        stateLoading.observe(this) {
            it.consume()?.let { isLoading ->
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
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
            context = this,
            message = message,
            title = getString(R.string.app_name),
            positiveText = getString(R.string.common_ok),
            onPositive = {
                onButtonClick?.invoke()
            }
        )
    }
}