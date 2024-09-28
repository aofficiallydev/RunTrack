package com.aofficially.runtrack.ui.addtracking.scan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.extensions.getCurrentDateTime
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ScanViewModel @Inject constructor() : BaseViewModel() {

    private lateinit var context: Context

    private val _updateSuccess = MutableLiveData<RunnerEntity>()
    val updateSuccess: LiveData<RunnerEntity> = _updateSuccess

    private val _notFoundRunner = MutableLiveData<String>()
    val notFoundRunner: LiveData<String> = _notFoundRunner

    fun initContext(context: Context) {
        this.context = context
    }

    fun getRunner(runBib: String, isInRace: Boolean) {
        viewModelScope.launch {
            RunnerDatabase(context)
                .runnerDao()
                .getRunner(runBib)?.let { runner ->
                    runner.timeStamp = Calendar.getInstance().timeInMillis
                    runner.dateIn = getCurrentDateTime("dd/MM/yyyy")
                    runner.dateOut = getCurrentDateTime("dd/MM/yyyy")
                    runner.timeIn = getCurrentDateTime()
                    runner.timeOut = getCurrentDateTime()
                    runner.runStatus =
                        if (isInRace) RunnerStatus.IN_RACE.status else RunnerStatus.DNF.status
                    runner.hasUpdate = true
                    runner.isUpLoaded = false

                    updateRunner(runner)
                } ?: run {
                _notFoundRunner.value = runBib
            }
        }
    }

    private suspend fun updateRunner(runner: RunnerEntity) {
        RunnerDatabase(context)
            .runnerDao()
            .updateRunner(runner)

        _updateSuccess.value = runner
    }
}