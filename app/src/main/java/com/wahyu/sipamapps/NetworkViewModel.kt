package com.wahyu.sipamapps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wahyu.sipamapps.utils.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NetworkViewModel(application: Application) : AndroidViewModel(application) {

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    init {
        checkInternetConnection()
    }

    private fun checkInternetConnection() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val isConnected = withContext(Dispatchers.IO) {
                NetworkUtil.isConnectedToInternet(context)
            }
            _isConnected.postValue(isConnected)
        }
    }
}
