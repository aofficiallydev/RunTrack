package com.aofficially.runtrack.ui.runner

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import com.aofficially.runtrack.ui.runner.domain.RunnerSize
import com.aofficially.runtrack.utils.DateTimeUtils
import com.aofficially.runtrack.utils.DateTimeUtils.sortFromDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RunnerViewModel @Inject constructor() : BaseViewModel() {

    private val _displayRunners = MutableLiveData<List<RunnerEntity>>()
    val displayRunners: LiveData<List<RunnerEntity>> = _displayRunners

    private val _displayRunnerSize = MutableLiveData<RunnerSize>()
    val displayRunnerSize: LiveData<RunnerSize> = _displayRunnerSize

    private val _displayRunnerAdded = MutableLiveData<String>()
    val displayRunnerAdded: LiveData<String> = _displayRunnerAdded

    private var runnerList: List<RunnerEntity> = mutableListOf()

    fun getMemberList(context: Context) {
        viewModelScope.launch {
            runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner().also { list ->
                    val runnerGroupBy = list.sortedByDescending { it.rDID }.groupBy { it.rDID }
                    var resultList = ""
                    runnerGroupBy.onEachIndexed { index, it ->
                        val key = it.value[index].runDistance

                        val inRace = it.value.filter { it.timeIn.isNotEmpty() }
                            .filter { it.runStatus == RunnerStatus.IN_RACE.status }.size
                        val dnf = it.value.filter { it.timeIn.isNotEmpty() }
                            .filter { it.runStatus == RunnerStatus.DNF.status }.size

                        resultList += "$key | Inrace $inRace Dnf $dnf\n"
                    }

                    _displayRunnerAdded.value = resultList
                    _displayRunnerSize.value = RunnerSize(
                        all = list.size,
                        inRace = filterRunnerInRace(list),
                        dnf = filterRunnerDNF(list)
                    )
                }
        }
    }

    private fun filterRunnerInRace(list: List<RunnerEntity>): Int {
        return list.filter { it.hasUpdate }
            .filter {
                it.runStatus == RunnerStatus.IN_RACE.status
            }.size
    }

    private fun filterRunnerDNF(list: List<RunnerEntity>): Int {
        return list.filter { it.hasUpdate }
            .filter {
                it.runStatus == RunnerStatus.DNF.status
            }.size
    }

    fun resetRunner(context: Context, runBib: String) {
        viewModelScope.launch {
            RunnerDatabase(context)
                .runnerDao()
                .getRunner(runBib)?.let { runner ->
                    runner.timeStamp = 0
                    runner.dateIn = ""
                    runner.dateOut = ""
                    runner.timeIn = ""
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
            .updateRunner(runner).also {
                getMemberList(context)
            }
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
                runnerList.sortedByDescending { sortFromDateTime(it.dateIn, it.timeIn) }
            } else {
                runnerList.sortedBy { it.runBid }
            }
        }
    }
}