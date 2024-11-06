package com.aofficially.runtrack.ui.home.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.aofficially.runtrack.R
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityMainBinding
import com.aofficially.runtrack.extensions.gone
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.extensions.visible
import com.aofficially.runtrack.ui.addtracking.manual.ManualActivity
import com.aofficially.runtrack.ui.addtracking.scan.ScanActivity
import com.aofficially.runtrack.ui.home.presentation.bottomsheet.LogoutBottomSheetDialog
import com.aofficially.runtrack.ui.home.presentation.pager.LandingPagerAdapter
import com.aofficially.runtrack.ui.runingevents.presentation.RunningEventsActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()
    private val shareViewModel: MainShareViewModel by viewModels()
    private var isInRace = true

    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                navigateToScanPage()
            } else {
                Toast.makeText(
                    this,
                    "Go to settings and enable camera permission to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun initView() {
        initLoadingView(viewModel.loadingState)
        setupView()
        viewModel.getRunnerListFromLocalize(this)
    }

    private fun setupView() = with(binding) {
        val adapter = LandingPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position

                when (tab.position) {
                    0 -> {
                        isInRace = true
                        inputDataLayout.visible()
                    }

                    1 -> {
                        isInRace = false
                        inputDataLayout.visible()
                    }

                    else -> {
                        inputDataLayout.gone()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun observeViewModel() {
        viewModel.logout.observe(this) {
            RunningEventsActivity.navigate(this)
            finish()
        }

        viewModel.resetRunner.observe(this) {
            shareViewModel.fetchRunnerList()
        }

        viewModel.uploadSuccess.observe(this) { size ->
            setViewReadyUpload()
            showUploadSuccess(size)
            shareViewModel.fetchRunnerList()
        }

        viewModel.displayUploadEmpty.observe(this) {
            setViewReadyUpload()
            showUploadListEmpty()
        }

        viewModel.upLoadFail.observe(this) {
            setViewReadyUpload()
            showUploadFail()
        }
    }

    override fun setupListener() {
        binding.headerLayout.apply {
            imgUpload.setOnClickWithDebounce {
                setViewUploading()
                viewModel.uploadRunner(this@MainActivity)
            }

            imgOption.setOnClickWithDebounce {
                val isShowResetButton = binding.viewPager.currentItem != 2
                val bottomSheet = LogoutBottomSheetDialog(isShowResetButton)

                supportFragmentManager.let { bottomSheet.show(it, "LogoutBottomSheetDialog") }

                bottomSheet.onLogoutListener = {
                    bottomSheet.dismiss()
                    showLogoutDialog()
                }

                bottomSheet.onRefreshRunner = {
                    bottomSheet.dismiss()
                    viewModel.refreshRunner(this@MainActivity)
                }

                bottomSheet.onResetListener = {
                    bottomSheet.dismiss()
                    showResetDialog()
                }
            }
        }

        binding.scanLayout.setOnClickWithDebounce {
            handleCameraPermission()
        }

        binding.manualLayout.setOnClickWithDebounce {
            ManualActivity.navigate(this, isInRace)
        }
    }

    private fun setViewUploading() {
        binding.headerLayout.imgUpload.gone()
        binding.headerLayout.progressBarUpload.visible()
    }

    private fun setViewReadyUpload() {
        binding.headerLayout.imgUpload.visible()
        binding.headerLayout.progressBarUpload.gone()
    }

    private fun showLogoutDialog() {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.logout_title),
            message = getString(R.string.logout_des),
            positiveText = getString(R.string.logout),
            onPositive = {
                viewModel.logout(this)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {}
        )
    }

    private fun showResetDialog() {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.reset_title),
            message = getString(R.string.reset_des),
            positiveText = getString(R.string.reset),
            onPositive = {
                viewModel.resetRunnerList(this)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {}
        )
    }

    private fun showUploadListEmpty() {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.upload_complete_title),
            message = getString(R.string.upload_empty_des),
            positiveText = getString(R.string.common_ok),
            onPositive = {

            }
        )
    }

    private fun showUploadSuccess(count: Int) {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.upload_complete_title),
            message = getString(R.string.upload_has_data_des, count.toString()),
            positiveText = getString(R.string.common_ok),
            onPositive = {

            }
        )
    }

    private fun showUploadFail() {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.upload_fail_title),
            message = getString(R.string.upload_fail_des),
            positiveText = getString(R.string.common_retry),
            onPositive = {
                binding.headerLayout.imgUpload.performClick()
            },
            negativeText = getString(R.string.common_cancel),
            onNegative = {

            }
        )
    }

    private fun handleCameraPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                navigateToScanPage()
            }

            else -> {
                cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun navigateToScanPage() {
        ScanActivity.navigate(this, isInRace)
    }

    companion object {
        fun navigate(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}