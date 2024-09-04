package com.aofficially.runtrack.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aofficially.runtrack.utils.SingleEvent


open class BaseViewModel : ViewModel(), LoadingStateViewModel, LifecycleObserver {

    private val _loadingState = MutableLiveData<SingleEvent<Boolean>>()
    override val loadingState: LiveData<SingleEvent<Boolean>> = _loadingState

    override fun showLoading() {
        _loadingState.postValue(SingleEvent(true))
    }

    override fun hideLoading() {
        _loadingState.postValue(SingleEvent(false))
    }
}