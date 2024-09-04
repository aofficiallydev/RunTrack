package com.aofficially.runtrack.ui.runner

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
class RunnerViewModel @Inject constructor() : BaseViewModel() {

    private val _displayRunners = MutableLiveData<List<RunnerEntity>>()
    val displayRunners: LiveData<List<RunnerEntity>> = _displayRunners

    private var runnerList: List<RunnerEntity> = mutableListOf()

    fun getMemberList(context: Context) {
        viewModelScope.launch {
            runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()

            _displayRunners.value = filterRunnerToDisplay()
        }
    }

    fun resetRunner(context: Context, runBib: String) {
        viewModelScope.launch {
            RunnerDatabase(context)
                .runnerDao()
                .getRunner(runBib)?.let {runner ->
                        runner.timeStamp = 0
                        runner.dateIn = ""
                        runner.dateOut = ""
                        runner.timeInt = ""
                        runner.timeOut = ""
                        runner.runStatus = RunnerStatus.IN_RACE.status
                        runner.hasUpdate = false
                        runner.isUpLoaded = false

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
                RunnerDatabase(context)
                    .runnerDao()
                    .findRunner("%${keyword}%")
            } else {
                filterRunnerToDisplay()
            }
        }
    }

    private fun filterRunnerToDisplay(): List<RunnerEntity> {
        return runnerList.any { it.timeStamp.toInt() != 0 }.let { hasData ->
            if (hasData) {
                runnerList.sortedByDescending { it.timeStamp }
            } else {
                runnerList.sortedBy { it.runBid }
            }
        }
    }
}