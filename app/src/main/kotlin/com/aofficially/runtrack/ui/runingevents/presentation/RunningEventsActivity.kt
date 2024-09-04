package com.aofficially.runtrack.ui.runingevents.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityRunningEventListBinding
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
    }

    override fun observeViewModel() {
        viewModel.displayRunEvents.observe(this) {
            runningEventsAdapter.submitList(it)
        }

        viewModel.navigateToHomePage.observe(this) {
            MainActivity.navigate(this)
        }
    }

    override fun setupListener() {

    }

    private fun setupRecycleView() {
        binding.rvRunningEvents.apply {
            adapter = runningEventsAdapter
            runningEventsAdapter.onItemClick = {
                LoginActivity.navigate(this@RunningEventsActivity, it)
            }
        }
    }

    companion object {
        fun navigate(context: Context) {
            context.startActivity(Intent(context, RunningEventsActivity::class.java))
        }
    }
}