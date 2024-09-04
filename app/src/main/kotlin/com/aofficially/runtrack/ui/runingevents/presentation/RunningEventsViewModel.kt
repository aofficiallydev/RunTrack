package com.aofficially.runtrack.ui.runingevents.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.ui.runingevents.domain.GetRunningEventsUseCase
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import com.aofficially.runtrack.utils.SingleLiveEvent
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp.Companion.KEY_RACE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@HiltViewModel
class RunningEventsViewModel @Inject constructor(
    private val getRunningEventsUseCase: GetRunningEventsUseCase,
    private val pref: PreferenceDataSource
) : BaseViewModel() {

    private val _displayRunEvents = MutableLiveData<List<RunningEventsModel>>()
    val displayRunEvents: LiveData<List<RunningEventsModel>> = _displayRunEvents

    private val _navigateToHomePage = SingleLiveEvent<Unit>()
    val navigateToHomePage: LiveData<Unit> = _navigateToHomePage

    fun getRunningEvent() {
        val raceId = pref.getString(KEY_RACE_ID)
        if (raceId.isEmpty()) {
            getRunningEventsUseCase.execute()
                .flowOn(Dispatchers.IO)
                .onStart { showLoading() }
                .onCompletion { hideLoading() }
                .catch { }
                .onEach {
                    _displayRunEvents.value = it
                }
                .launchIn(viewModelScope)
        } else {
            _navigateToHomePage.value = Unit
        }
    }
}