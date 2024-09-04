package com.aofficially.runtrack.ui.addtracking.scan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityScanBinding
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import com.aofficially.runtrack.utils.NewNotificationUtil
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanActivity :
    BaseActivity<ActivityScanBinding>(ActivityScanBinding::inflate) {

    private val viewModel: ScanViewModel by viewModels()

    private lateinit var codeScanner: CodeScanner

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun initView() {
        viewModel.initContext(this)
        val isInRace = intent.extras?.getBoolean(BUNDLE_IS_IN_RACE) ?: false
        codeScanner = CodeScanner(this, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                codeScanner.stopPreview()
                viewModel.getRunner(it.text, isInRace)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun observeViewModel() {
        viewModel.updateSuccess.observe(this) {
            codeScanner.startPreview()
            NewNotificationUtil.showCustomNotification(
                activity = this,
                runBib = it.runBid,
                runName = "${it.runFirstname} ${it.runLastname}",
                runTime = it.timeInt,
                runGroup = "${it.runSex} ${it.runDistance}",
                runDate = it.dateIn,
                isInRace = it.runStatus == RunnerStatus.IN_RACE.status
            )
        }
    }

    override fun setupListener() {

    }

    companion object {

        private const val BUNDLE_IS_IN_RACE = "BUNDLE_IS_IN_RACE"
        fun navigate(context: Context, isInRace: Boolean) {
            val intent = Intent(context, ScanActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(BUNDLE_IS_IN_RACE, isInRace)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}