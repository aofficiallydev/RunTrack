package com.aofficially.runtrack.ui.addtracking.manual

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject constructor() : BaseViewModel() {

    private val _isEnableAddButton = MutableLiveData<Boolean>()
    val isEnableAddButton: LiveData<Boolean> = _isEnableAddButton

    private val _displayRunner = MutableLiveData<RunnerEntity>()
    val displayRunner: LiveData<RunnerEntity> = _displayRunner

    private val _hideRunner = MutableLiveData<Unit>()
    val hideRunner: LiveData<Unit> = _hideRunner

    private val _runnerAdded = MutableLiveData<Unit>()
    val runnerAdded: LiveData<Unit> = _runnerAdded

    private val _updateSuccess = MutableLiveData<RunnerEntity>()
    val updateSuccess: LiveData<RunnerEntity> = _updateSuccess

    private val _displayRunners = MutableLiveData<List<RunnerEntity>>()
    val displayRunners: LiveData<List<RunnerEntity>> = _displayRunners

    private var runnerList: List<RunnerEntity> = mutableListOf()
    private var runnerSelected: RunnerEntity? = null
    private var isInRace = true

    fun setInRace(isInRace: Boolean) {
        this.isInRace = isInRace
    }

    fun getMemberList(context: Context) {
        viewModelScope.launch {
            runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()

            runnerList.map { it.isSetHead = false }

            if (runnerList.isNotEmpty()) {
                _displayRunners.value = filterRunnerToDisplay(runnerList)
            }
        }
    }

    private fun filterRunnerToDisplay(runnerList: List<RunnerEntity>): List<RunnerEntity> {
        return runnerList.filter { it.hasUpdate }
            .filter {
                it.runStatus == if (isInRace) {
                    RunnerStatus.IN_RACE.status
                } else {
                    RunnerStatus.DNF.status
                }
            }
            .sortedByDescending { it.timeStamp }
    }

    fun findRunnerByBib(runBib: String) {
        runnerSelected = runnerList.find { it.runBid.equals(runBib, true) }

        runnerSelected?.let {
            _isEnableAddButton.value = true
            _displayRunner.value = it
        } ?: run {
            _isEnableAddButton.value = false
            _hideRunner.value = Unit
        }
    }

    fun updateRunner(context: Context, date: String, hour: String, second: String) {
        runnerSelected?.let { runner ->

            val canAdd = if (isInRace) {
                runner.runStatus != RunnerStatus.IN_RACE.status
            } else {
                runner.runStatus != RunnerStatus.DNF.status
            }

            if (runner.hasUpdate && canAdd.not()) {
                _runnerAdded.value = Unit
            } else {
                viewModelScope.launch {
                    runner.timeStamp = Calendar.getInstance().timeInMillis
                    runner.dateIn = date
                    runner.dateOut = date

                    val time = "${hour}:${second}"
                    runner.timeIn = time
                    runner.timeOut = time
                    runner.runStatus =
                        if (isInRace) RunnerStatus.IN_RACE.status else RunnerStatus.DNF.status
                    runner.hasUpdate = true
                    runner.isUpLoaded = false

                    RunnerDatabase(context)
                        .runnerDao()
                        .updateRunner(runner)

                    getMemberList(context)
                    _updateSuccess.value = runner
                }
            }
        }
    }

    fun resetRunner(context: Context, runBib: String) {
        viewModelScope.launch {
            RunnerDatabase(context)
                .runnerDao()
                .getRunner(runBib)?.let { runner ->
                    runner.let {
                        it.timeStamp = 0
                        it.dateIn = ""
                        it.dateOut = ""
                        it.timeIn = ""
                        it.timeOut = ""
                        it.runStatus = RunnerStatus.IN_RACE.status
                        it.hasUpdate = false
                        it.isUpLoaded = false
                    }
                    updateRunner(context, runner)
                }
        }
    }

    private suspend fun updateRunner(context: Context, runner: RunnerEntity) {
        RunnerDatabase(context)
            .runnerDao()
            .updateRunner(runner)

        getMemberList(context)
    }
}