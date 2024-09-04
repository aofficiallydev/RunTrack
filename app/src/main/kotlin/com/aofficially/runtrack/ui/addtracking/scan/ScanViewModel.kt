package com.aofficially.runtrack.ui.addtracking.scan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.extensions.getCurrentDate
import com.aofficially.runtrack.extensions.getCurrentTime
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ScanViewModel @Inject constructor() : BaseViewModel() {

    private lateinit var context: Context

    private val _updateSuccess = MutableLiveData<RunnerEntity>()
    val updateSuccess: LiveData<RunnerEntity> = _updateSuccess

    fun initContext(context: Context) {
        this.context = context
    }

    fun getRunner(runBib: String, isInRace: Boolean) {
        viewModelScope.launch {
            val runner = RunnerDatabase(context)
                .runnerDao()
                .getRunner(runBib)

            runner.timeStamp = Calendar.getInstance().timeInMillis
            runner.dateIn = getCurrentDate()
            runner.dateOut = getCurrentDate()
            runner.timeInt = getCurrentTime()
            runner.timeOut = getCurrentTime()
            runner.runStatus =
                if (isInRace) RunnerStatus.IN_RACE.status else RunnerStatus.DNF.status
            runner.hasUpdate = true
            runner.isUpLoaded = false

            updateRunner(runner)
        }
    }

    private suspend fun updateRunner(runner: RunnerEntity) {
        RunnerDatabase(context)
            .runnerDao()
            .updateRunner(runner)

        _updateSuccess.value = runner
    }
}