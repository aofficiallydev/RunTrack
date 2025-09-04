package com.aofficially.runtrack.ui.addtracking.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.aofficially.runtrack.R
import com.aofficially.runtrack.base.BaseActivity
import com.aofficially.runtrack.databinding.ActivityManualBinding
import com.aofficially.runtrack.extensions.getCurrentDateTime
import com.aofficially.runtrack.extensions.setOnClickWithDebounce
import com.aofficially.runtrack.ui.addtracking.manual.bottomsheet.SecondTimePickerBottomSheetDialog
import com.aofficially.runtrack.ui.addtracking.manual.picker.DatePickerFragment
import com.aofficially.runtrack.ui.addtracking.manual.picker.TimePickerFragment
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import com.aofficially.runtrack.ui.trackrunner.adapter.TrackRunnerAdapter
import com.aofficially.runtrack.utils.NewNotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class ManualActivity :
    BaseActivity<ActivityManualBinding>(ActivityManualBinding::inflate) {

    private val viewModel: ManualViewModel by viewModels()

    private val trackRunnerAdapter: TrackRunnerAdapter by lazy { TrackRunnerAdapter() }
    private val handler = Handler(Looper.getMainLooper())
    private var timeUpdater = object : Runnable {
        override fun run() {
            val currentTime = LocalDateTime.now()
            val HourFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val HourFformattedTime = currentTime.format(HourFormatter)
            binding.tvHourTime.text = HourFformattedTime

            val secFormatter = DateTimeFormatter.ofPattern("ss")
            val secFormattedTime = currentTime.format(secFormatter)
            binding.tvSecTime.text = "${secFormattedTime}s"

            handler.postDelayed(this, 1000)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timeUpdater)
    }

    override fun initView() {
        val isInRace = intent.extras?.getBoolean(BUNDLE_IS_IN_RACE) ?: false
        setupView()
        viewModel.setInRace(isInRace)
        viewModel.getMemberList(this)
        setupNavigationView(binding.numericKeyboard)
    }

    private fun setupView() = with(binding) {
        tvDate.text = getCurrentDateTime("dd/MM/yyyy")
        tvHourTime.text = getCurrentDateTime("HH:mm")
        tvSecTime.text = "${getCurrentDateTime("ss")}s"

        handler.post(timeUpdater)
        recyclerView.adapter = trackRunnerAdapter
        trackRunnerAdapter.onLongClickItemListener = {
            showResetDialog(it.runBid)
        }
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
                runTime = it.timeIn,
                runGroup = "${it.runSex} ${it.runDistance}",
                runDate = it.dateIn,
                isInRace = it.runStatus == RunnerStatus.IN_RACE.status
            )
        }

        viewModel.runnerAdded.observe(this) {
            Toast.makeText(this, "Already add this runner", Toast.LENGTH_SHORT).show()
        }

        viewModel.displayRunners.observe(this) {
            trackRunnerAdapter.submitList(it)
            trackRunnerAdapter.notifyDataSetChanged()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.recyclerView.smoothScrollToPosition(0)
            }, 500)
        }
    }

    override fun setupListener() {
        binding.numericKeyboard.field = binding.tvNumberBib

        binding.tvNumberBib.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                handler.post(timeUpdater)
            }
            viewModel.findRunnerByBib(text.toString())
        }

        binding.btnAdd.setOnClickWithDebounce {
            handler.post(timeUpdater)
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
            val date = "${String.format("%02d", day)}/${String.format("%02d", month + 1)}/${year}"
            binding.tvDate.text = date
        }
    }

    private fun showTimePicker() {
        val timePicker = TimePickerFragment()
        timePicker.show(supportFragmentManager, "")
        timePicker.onTimeSelected = { hour, min ->
            handler.removeCallbacks(timeUpdater)
            val date = "${String.format("%02d", hour)}:${String.format("%02d", min)}"
            binding.tvHourTime.text = date
        }
    }

    private fun showSecondTimeDialog() {
        val secPicker = SecondTimePickerBottomSheetDialog()
        supportFragmentManager.let { secPicker.show(it, "LogoutBottomSheetDialog") }
        secPicker.onSelectedListener = {
            handler.removeCallbacks(timeUpdater)
            binding.tvSecTime.text = "${it}s"
        }
    }

    private fun showResetDialog(runBid: String) {
        dialogUtility.showAlertDialog(
            context = this,
            title = getString(R.string.reset_runner_title),
            message = getString(R.string.reset_runner_des),
            positiveText = getString(R.string.reset),
            onPositive = {
                viewModel.resetRunner(this, runBid)
            },
            negativeText = getString(R.string.cancel),
            onNegative = {}
        )
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