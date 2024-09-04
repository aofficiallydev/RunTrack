package com.aofficially.runtrack.ui.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aofficially.runtrack.base.BaseViewModel
import com.aofficially.runtrack.utils.preference.PreferenceDataSource
import com.aofficially.runtrack.utils.preference.PreferenceDataSourceImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainShareViewModel @Inject constructor(
    private val pref: PreferenceDataSource
) : BaseViewModel() {

    private val _fetchRunnerList = MutableLiveData<Unit>()
    val fetchRunnerList: LiveData<Unit> = _fetchRunnerList

    fun getStationName() = pref.getString(PreferenceDataSourceImp.KEY_STATION_NAME)

    fun fetchRunnerList() {
        _fetchRunnerList.value = Unit
    }
}