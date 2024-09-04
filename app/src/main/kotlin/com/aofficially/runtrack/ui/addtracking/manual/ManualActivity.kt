package com.aofficially.runtrack.ui.addtracking.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityManualBinding
import com.aofficially.runtrack.extensions.getCurrentDate
import com.aofficially.runtrack.extensions.getCurrentTime
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import com.aofficially.runtrack.utils.NewNotificationUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualActivity :
    BaseActivity<ActivityManualBinding>(ActivityManualBinding::inflate) {

    private val viewModel: ManualViewModel by viewModels()

    override fun initView() {
        val isInRace = intent.extras?.getBoolean(BUNDLE_IS_IN_RACE) ?: false
        setupView()
        viewModel.getMemberList(this, isInRace)
    }

    private fun setupView() = with(binding) {
        tvDate.text = getCurrentDate()
        tvHourTime.text = getCurrentTime("HH:mm")
        tvSecTime.text = "${getCurrentTime("ss")}s"
    }

    override fun observeViewModel() {
        viewModel.isEnableAddButton.observe(this) {
            binding.btnAdd.isEnabled = it
        }

        viewModel.displayRunner.observe(this) {
            binding.tvFullName.text = "${it.runFirstname} ${it.runLastname}"
            binding.tvGroup.text = "${it.runSex} ${it.runDistance}"
        }

        viewModel.hideRunner.observe(this) {
            binding.tvFullName.text = ""
            binding.tvGroup.text = ""
        }

        viewModel.updateSuccess.observe(this) {
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
        binding.numericKeyboard.field = binding.tvNumberBib

        binding.tvNumberBib.doOnTextChanged { text, _, _, _ ->
            viewModel.findRunnerByBib(text.toString())
        }

        binding.btnAdd.setOnClickWithDebounce {
            viewModel.updateRunner(
                context = this,
                date = binding.tvDate.text.toString(),
                hour = binding.tvHourTime.text.toString(),
                second = binding.tvSecTime.text.toString()
            )
        }
    }

    companion object {

        private const val BUNDLE_IS_IN_RACE = "BUNDLE_IS_IN_RACE"
        fun navigate(context: Context, isInRace: Boolean) {
            val intent = Intent(context, ManualActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(BUNDLE_IS_IN_RACE, isInRace)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}