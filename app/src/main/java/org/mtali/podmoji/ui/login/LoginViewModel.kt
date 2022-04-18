package org.mtali.podmoji.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mtali.podmoji.shared.Event

class LoginViewModel : ViewModel() {
    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun onClickLogin() {
        _loginEvent.value = Event(Unit)
        _isLoading.value = true
    }

    fun loading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}