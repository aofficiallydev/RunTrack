package com.aofficially.runtrack.ui.trackrunner

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TrackRunnerViewModel @Inject constructor() : BaseViewModel() {

    private val _displayRunners = MutableLiveData<List<RunnerEntity>>()
    val displayRunners: LiveData<List<RunnerEntity>> = _displayRunners

    private var isInRace = true

    private var runnerFilterList: List<RunnerEntity> = mutableListOf()

    fun setRace(isInRace: Boolean) {
        this.isInRace = isInRace
    }

    fun getMemberList(context: Context) {
        viewModelScope.launch {
            val runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()

            if (runnerList.isNotEmpty()) {
                runnerFilterList = runnerList
                _displayRunners.value = filterRunnerToDisplay()
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
                        it.timeInt = ""
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

    fun findRunner(context: Context, keyword: String) {
        viewModelScope.launch {
            _displayRunners.value = if (keyword.isNotEmpty()) {
                val result = RunnerDatabase(context)
                    .runnerDao()
                    .findRunner("%${keyword}%")

                result.filter { it.hasUpdate }
                    .filter {
                        it.runStatus == if (isInRace) {
                            RunnerStatus.IN_RACE.status
                        } else {
                            RunnerStatus.DNF.status
                        }
                    }
            } else {
                filterRunnerToDisplay()
            }
        }
    }

    private fun filterRunnerToDisplay(): List<RunnerEntity> {
        return runnerFilterList.filter { it.hasUpdate }
            .filter {
                it.runStatus == if (isInRace) {
                    RunnerStatus.IN_RACE.status
                } else {
                    RunnerStatus.DNF.status
                }
            }
            .sortedByDescending { it.timeStamp }
    }
}