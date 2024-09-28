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
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ManualViewModel @Inject constructor() : BaseViewModel() {

    private val _isEnableAddButton = MutableLiveData<Boolean>()
    val isEnableAddButton: LiveData<Boolean> = _isEnableAddButton

    private val _displayRunner = MutableLiveData<RunnerEntity>()
    val displayRunner: LiveData<RunnerEntity> = _displayRunner

    private val _hideRunner = MutableLiveData<Unit>()
    val hideRunner: LiveData<Unit> = _hideRunner

    private val _updateSuccess = MutableLiveData<RunnerEntity>()
    val updateSuccess: LiveData<RunnerEntity> = _updateSuccess

    private var runnerList: List<RunnerEntity> = mutableListOf()
    private var runnerSelected: RunnerEntity? = null
    private var isInRace = true

    fun getMemberList(context: Context, isInRace: Boolean) {
        this.isInRace = isInRace
        viewModelScope.launch {
            runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()
        }
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
        viewModelScope.launch {
            runnerSelected?.let { runner ->
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

                _updateSuccess.value = runner
            }
        }
    }
}