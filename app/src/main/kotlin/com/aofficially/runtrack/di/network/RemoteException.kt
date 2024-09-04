package com.aofficially.runtrack.di.network

data class RemoteException(val code: Int = 500, val msg: String? = "") : Throwable(message = msg)