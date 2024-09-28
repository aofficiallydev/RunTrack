package com.aofficially.runtrack.ui.home.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.database.RunnerDatabase
import com.aofficially.runtrack.database.RunnerEntity
import com.aofficially.runtrack.ui.home.domain.GetRunnerListUseCase
import com.aofficially.runtrack.ui.home.domain.RunnerStatus
import com.aofficially.runtrack.ui.home.domain.UploadRunnerUseCase
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_RACE_ID
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_STATION_ID
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_STATION_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRunnerListUseCase: GetRunnerListUseCase,
    private val uploadRunnerUseCase: UploadRunnerUseCase,
    private val pref: PreferenceDataSource
) : BaseViewModel() {

    private val _logout = MutableLiveData<Unit>()
    val logout: LiveData<Unit> = _logout

    private val _resetRunner = MutableLiveData<Unit>()
    val resetRunner: LiveData<Unit> = _resetRunner

    private val _upLoadFail = MutableLiveData<Unit>()
    val upLoadFail: LiveData<Unit> = _upLoadFail

    private val _uploadSuccess = MutableLiveData<Int>()
    val uploadSuccess: LiveData<Int> = _uploadSuccess

    private val _displayUploadEmpty = MutableLiveData<Unit>()
    val displayUploadEmpty: LiveData<Unit> = _displayUploadEmpty

    fun getRunnerListFromLocalize(context: Context) {
        viewModelScope.launch {
            val runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()

            if (runnerList.isEmpty()) {
                getRunnerList(context)
            }
        }
    }

    private fun getRunnerList(context: Context) {
        getRunnerListUseCase.execute()
            .flowOn(Dispatchers.IO)
            .onStart { showLoading() }
            .onCompletion { hideLoading() }
            .catch { }
            .onEach { response ->
                insertRunnerList(context, response)
            }
            .launchIn(viewModelScope)
    }

    private fun insertRunnerList(context: Context, runners: List<RunnerEntity>) {
        viewModelScope.launch {
            RunnerDatabase(context)
                .runnerDao()
                .insertAllUser(runners)
        }
    }

    fun logout(context: Context) {
        pref.apply {
            putString(KEY_RACE_ID, "")
            putString(KEY_STATION_ID, "")
            putString(KEY_STATION_NAME, "")
        }
        viewModelScope.launch {
            val runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner().filter { it.hasUpdate && it.isUpLoaded.not() }

            if (runnerList.isNotEmpty()) {
                showLoading()
                uploadRunnerUseCase.execute(runnerList)
                    .flowOn(Dispatchers.IO)
                    .catch {
                        _upLoadFail.value = Unit
                    }
                    .onEach { _ ->
                        runnerList.map {
                            it.isUpLoaded = true
                        }
                        updateRunnerAfterUpload(context, runnerList)
                        delay(1000)
                        hideLoading()
                        _logout.value = Unit
                    }
                    .launchIn(viewModelScope)
            } else {
                _logout.value = Unit
            }
        }
    }

    fun resetRunnerList(context: Context) {
        viewModelScope.launch {
            val runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner()

            runnerList.map {
                it.timeStamp = 0
                it.dateIn = ""
                it.dateOut = ""
                it.timeIn = ""
                it.timeOut = ""
                it.runStatus = RunnerStatus.IN_RACE.status
                it.hasUpdate = false
                it.isUpLoaded = false
            }

            updateRunnerAfterReset(context, runnerList)
        }
    }

    private suspend fun updateRunnerAfterReset(context: Context, runner: List<RunnerEntity>) {
        runner.map {
            RunnerDatabase(context)
                .runnerDao()
                .updateRunner(it)
        }

        _resetRunner.value = Unit
    }

    fun uploadRunner(context: Context) {
        viewModelScope.launch {
            val runnerList = RunnerDatabase(context)
                .runnerDao()
                .getAllRunner().filter { it.hasUpdate && it.isUpLoaded.not() }

            if (runnerList.isNotEmpty()) {
                requestUploadRunnerList(context, runnerList)
            } else {
                _displayUploadEmpty.value = Unit
            }
        }
    }

    private fun requestUploadRunnerList(context: Context, runnerList: List<RunnerEntity>) {
        uploadRunnerUseCase.execute(runnerList)
            .flowOn(Dispatchers.IO)
            .catch {
                _upLoadFail.value = Unit
            }
            .onEach { _ ->
                runnerList.map {
                    it.isUpLoaded = true
                }
                updateRunnerAfterUpload(context, runnerList)
                delay(1000)
                _uploadSuccess.value = runnerList.size
            }
            .launchIn(viewModelScope)
    }

    private suspend fun updateRunnerAfterUpload(context: Context, runner: List<RunnerEntity>) {
        runner.map {
            RunnerDatabase(context)
                .runnerDao()
                .updateRunner(it)
        }
    }
}