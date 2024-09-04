package com.aofficially.runtrack.ui.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.ui.login.domain.LoginUseCase
import com.aofficially.runtrack.ui.runingevents.domain.RunningEventsModel
import com.aofficially.runtrack.utils.SingleLiveEvent
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _displayEvent = MutableLiveData<RunningEventsModel>()
    val displayEvent: LiveData<RunningEventsModel> = _displayEvent

    private val _navigateToHomePage = SingleLiveEvent<Unit>()
    val navigateToHomePage: LiveData<Unit> = _navigateToHomePage

    private var eventSelected: RunningEventsModel = RunningEventsModel()

    fun setupData(event: RunningEventsModel) {
        eventSelected = event
        _displayEvent.value = event
    }


    fun login(username: String, password: String) {
        loginUseCase.execute(
            raceId = eventSelected.raceId,
            username = username,
            password = password
        )
            .flowOn(Dispatchers.IO)
            .onStart { showLoading() }
            .onCompletion { hideLoading() }
            .catch { }
            .onEach {
                _navigateToHomePage.value = Unit
            }
            .launchIn(viewModelScope)
    }

}