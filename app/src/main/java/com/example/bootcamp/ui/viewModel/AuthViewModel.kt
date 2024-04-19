package com.example.bootcamp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
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

    private var loginWithPhoneLiveData = MutableStateFlow<RequestResult<BaseEntity>?>(null)

    fun loginWithPhoneLiveData(): StateFlow<RequestResult<BaseEntity>?> =
        loginWithPhoneLiveData


    private var mLoginWithPhoneJob: Job? = null

    fun loginWithPhone(phoneNumber: String) {
        mLoginWithPhoneJob?.cancel()
        mLoginWithPhoneJob = sendRequest({ authInteractor.loginWithPhone(phoneNumber) }, {
            loginWithPhoneLiveData.postValue(it)
        })
    }

}

sealed class AuthScreenState {
    data object InitialState : FactScreenState()
    data object LoadingState : FactScreenState()
    data class ResultStateFact(val fact: FactEntity) : FactScreenState()
    data class ResultStateBoard(val boardEntity: BoredEntity) : FactScreenState()
    data class ErrorState(val message: String) : FactScreenState()
}