package com.aofficially.runtrack.ui.runingevents.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.aofficially.runtrack.R
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityRunningEventListBinding
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.home.presentation.MainActivity
import com.aofficially.runtrack.ui.login.presentation.LoginActivity
import com.aofficially.runtrack.ui.runingevents.presentation.adapter.RunningEventsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningEventsActivity :
    BaseActivity<ActivityRunningEventListBinding>(ActivityRunningEventListBinding::inflate) {

    private val viewModel: RunningEventsViewModel by viewModels()
    private val runningEventsAdapter: RunningEventsAdapter by lazy { RunningEventsAdapter() }

    override fun initView() {
        initLoadingView(viewModel.loadingState)
        setupRecycleView()
        viewModel.getRunningEvent()
        setupNavigationView(binding.rvRunningEvents)
    }

    override fun observeViewModel() {
        viewModel.displayRunEvents.observe(this) {
            runningEventsAdapter.submitList(it)
        }

        viewModel.navigateToHomePage.observe(this) {
            MainActivity.navigate(this)
        }

        viewModel.handleChangeDomainDialog.observe(this) {
            showDialogAfterChangeDomain()
        }
    }

    override fun setupListener() {
        binding.icSetting.setOnClickWithDebounce {
            showDialogDomain()
        }
    }

    private fun setupRecycleView() {
        binding.rvRunningEvents.apply {
            adapter = runningEventsAdapter
            runningEventsAdapter.onItemClick = {
                LoginActivity.navigate(this@RunningEventsActivity, it)
            }
        }
    }

    private fun showDialogDomain() {
        val currentDomain = viewModel.getDomain()
        dialogUtility.showAlertDialog(
            context = this,
            positiveText = getString(R.string.common_ok),
            onPositive = {
                viewModel.setDomain(it)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {},
            isShowEditText = true,
            informEditText = currentDomain
        )
    }

    private fun showDialogAfterChangeDomain() {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.restart_app_title),
            message = getString(R.string.restart_app_description),
            positiveText = getString(R.string.common_ok),
            onPositive = {
                finishAffinity()
            }
        )
    }

    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, RunningEventsActivity::class.java))
        }
    }
}