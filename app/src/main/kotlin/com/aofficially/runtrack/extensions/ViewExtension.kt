package com.aofficially.runtrack.extensions

import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.aofficially.runtrack.R
import com.aofficially.runtrack.utils.FragmentViewBindingDelegate

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy {
    bindingInflater.invoke(layoutInflater)
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun View.setOnClickWithDebounce(debounceTime: Long = 1000L, onClick: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime >= debounceTime) {
                onClick.invoke()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        }
    })
}

fun View.setOnDoubleClick(onClick: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 300
        override fun onClick(v: View) {
            val clickTime = System.currentTimeMillis()
            if (SystemClock.elapsedRealtime() - lastClickTime < lastClickTime) {
                onClick.invoke()
                lastClickTime = clickTime
            }
        }
    })
}

fun View?.fadeOut() {
    this?.let {
        AnimationUtils.loadAnimation(it.context, R.anim.fade_out)
            .also { animation ->
                it.startAnimation(animation)
                it.visibility = View.GONE
            }
    }
}

fun View?.fadeIn() {
    this?.let {
        AnimationUtils.loadAnimation(it.context, R.anim.fade_in)
            .also { animation ->
                it.startAnimation(animation)
                it.visibility = View.VISIBLE
            }
    }
}

fun View?.hideDelay(onRunnable: () -> Unit = {}, delayMillis: Long) {
    this?.let {
        Handler(Looper.myLooper()!!).postDelayed({
            onRunnable.invoke()
            it.visibility = View.GONE
        }, delayMillis)
    }
}

fun View?.visibleOrGone(visibilityFlag: Int) {
    this?.let {
        it.visibility = visibilityFlag
    }
}

fun View?.visible() {
    this?.let {
        it.visibility = View.VISIBLE
    }
}

fun View?.invisible() {
    this?.let {
        it.visibility = View.INVISIBLE
    }
}

fun View?.gone() {
    this?.let {
        it.visibility = View.GONE
    }
}

fun EditText?.clearFocusOnDoneAction() {
    this?.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            this.clearFocus()
            true
        }
        false
    }
}

fun EditText.onDone(callback: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    maxLines = 1
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }
        false
    }
}

fun View?.closeKeyboard(context: Activity) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this?.windowToken, 0)
}

fun ImageView?.rotateArrowIcon(expand: Boolean, animDuration: Long) {
    if (expand) {
        ObjectAnimator.ofFloat(this, View.ROTATION, 180f)
            .setDuration(animDuration)
            .start()
    } else {
        ObjectAnimator.ofFloat(this, View.ROTATION, 0f)
            .setDuration(animDuration)
            .start()
    }
}