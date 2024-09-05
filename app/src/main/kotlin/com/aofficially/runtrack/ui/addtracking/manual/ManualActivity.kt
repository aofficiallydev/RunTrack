package com.aofficially.runtrack.ui.addtracking.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityManualBinding
import com.aofficially.runtrack.extensions.getCurrentDateTime
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.addtracking.manual.bottomsheet.SecondBottomSheetDialog
import com.aofficially.runtrack.ui.addtracking.manual.picker.DatePickerFragment
import com.aofficially.runtrack.ui.addtracking.manual.picker.TimePickerFragment
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
        tvDate.text = getCurrentDateTime("dd/MM/yyyy")
        tvHourTime.text = getCurrentDateTime("HH:mm")
        tvSecTime.text = "${getCurrentDateTime("ss")}s"
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
            binding.tvNumberBib.setText("")
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
                second = binding.tvSecTime.text.toString().dropLast(1)
            )
        }

        binding.tvDate.setOnClickWithDebounce {
            showDatePicker()
        }

        binding.tvHourTime.setOnClickWithDebounce {
            showTimePicker()
        }

        binding.tvSecTime.setOnClickWithDebounce {
            showSecondTimeDialog()
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerFragment()
        datePicker.show(supportFragmentManager, "")
        datePicker.onDateSelected = { year, month, day ->

            val date = "${String.format("%02d", day)}/${String.format("%02d", month)}/${year}"
            binding.tvDate.text = date
        }
    }

    private fun showTimePicker() {
        val timePicker = TimePickerFragment()
        timePicker.show(supportFragmentManager, "")
        timePicker.onTimeSelected = { hour, min ->

            val date = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
            binding.tvHourTime.text = date
        }
    }

    private fun showSecondTimeDialog() {
        val secPicker = SecondBottomSheetDialog()
        supportFragmentManager.let { secPicker.show(it, "LogoutBottomSheetDialog") }
        secPicker.onSelectedListener = {
            binding.tvSecTime.text = "${it}s"
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