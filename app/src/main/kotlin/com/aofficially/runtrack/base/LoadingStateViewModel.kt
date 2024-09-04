package com.aofficially.runtrack.base

import androidx.lifecycle.LiveData
import com.aofficially.runtrack.utils.SingleEvent

interface LoadingStateViewModel {
    val loadingState: LiveData<SingleEvent<Boolean>>

    fun showLoading()
    fun hideLoading()
}