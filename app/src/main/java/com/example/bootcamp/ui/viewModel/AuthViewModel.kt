package com.example.bootcamp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
import com.example.domain.entity.mapToBaseEntity
import com.example.domain.interactors.AuthInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModelProvider(private val interactor: AuthInteractor) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AuthViewModel(private val authInteractor: AuthInteractor) : BaseViewModel() {

    private var _authState =
        MutableStateFlow<AuthScreenState>(AuthScreenState.InitialState)

    fun authState(): StateFlow<AuthScreenState> = _authState


    private var mLoginWithPhoneJob: Job? = null

    fun loginWithPhone(phoneNumber: String) {
        _authState.value = AuthScreenState.LoadingState

        mLoginWithPhoneJob?.cancel()
        mLoginWithPhoneJob = sendRequest({ authInteractor.loginWithPhone(phoneNumber) }, {
            when (it) {
                is RequestResult.Error -> _authState.value =
                    AuthScreenState.ErrorState(it.mapToBaseEntity())

                is RequestResult.Success -> _authState.value =
                    AuthScreenState.LoginWithPhoneResult(it.body)
            }
        })
    }
}

sealed class AuthScreenState {
    data object InitialState : AuthScreenState()
    data object LoadingState : AuthScreenState()
    data class LoginWithPhoneResult(val baseEntity: BaseEntity) : AuthScreenState()
    data class ErrorState(val entity: BaseEntity) : AuthScreenState()
}