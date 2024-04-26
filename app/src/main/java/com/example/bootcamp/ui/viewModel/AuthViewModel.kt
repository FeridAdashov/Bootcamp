package com.example.bootcamp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
import com.example.domain.entity.mapToBaseEntity
import com.example.domain.interactors.AuthInteractor
import kotlinx.coroutines.Job

class AuthViewModelProvider(private val interactor: AuthInteractor) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//@HiltViewModel
//class AuthViewModel @Inject constructor(private val authInteractor: AuthInteractor) : BaseViewModel() {

class AuthViewModel(private val authInteractor: AuthInteractor) : BaseViewModel() {

    private var mLoginWithPhoneJob: Job? = null

    fun loginWithPhone(phoneNumber: String) {
        _commonState.tryEmit(ScreenState.Loading)

        mLoginWithPhoneJob?.cancel()
        mLoginWithPhoneJob = sendRequest({ authInteractor.loginWithPhone(phoneNumber) }, {
            _commonState.tryEmit(ScreenState.Initial)

            when (it) {
                is RequestResult.Error -> {
                    _commonState.tryEmit(ScreenState.Error(it.mapToBaseEntity()))
                }

                is RequestResult.Success -> _screenState.value =
                    ScreenState.Result(SignInScreenState.LoginWithPhoneResult(it.body))
            }
        })
    }

    fun loginWithGmail(gmailAccount: String) {
        _commonState.tryEmit(ScreenState.Loading)

        mLoginWithPhoneJob?.cancel()
        mLoginWithPhoneJob = sendRequest({ authInteractor.loginWithGmail(gmailAccount) }, {
            _commonState.tryEmit(ScreenState.Initial)

            when (it) {
                is RequestResult.Error -> {
                    _commonState.tryEmit(ScreenState.Error(it.mapToBaseEntity()))
                }

                is RequestResult.Success -> _screenState.value =
                    ScreenState.Result(SignInScreenState.LoginWithGmailResult(it.body))
            }
        })
    }
}

sealed class SignInScreenState {
    data class LoginWithPhoneResult(val baseEntity: BaseEntity) : SignInScreenState()
    data class LoginWithGmailResult(val baseEntity: BaseEntity) : SignInScreenState()
}