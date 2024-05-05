package com.example.bootcamp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.ConfirmOtpEntity
import com.example.domain.entity.OtpType
import com.example.domain.entity.RefreshTokenEntity
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
    private var mConfirmOtpJob: Job? = null
    private var mConfirmRegisterOtpJob: Job? = null
    private var mRefreshTokenJob: Job? = null

    fun loginWithPhone(phoneNumber: String) {
        launchWithIO {
            _commonState.emit(ScreenState.Loading)
        }

        mLoginWithPhoneJob?.cancel()
        mLoginWithPhoneJob = sendRequest({ authInteractor.loginWithPhone(phoneNumber) }, {
            launchWithIO {
                _commonState.emit(ScreenState.Initial)
            }

            when (it) {
                is RequestResult.Error -> {
                    launchWithIO {
                        _commonState.emit(ScreenState.Error(it.mapToBaseEntity()))
                    }
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

    fun confirmOtp(otpType: OtpType, value: String, otpCode: String) {
        launchWithIO {
            _commonState.emit(ScreenState.Loading)
        }

        mConfirmOtpJob?.cancel()
        mConfirmOtpJob = sendRequest({ authInteractor.confirmOtp(otpType, value, otpCode) }, {
            launchWithIO {
                _commonState.emit(ScreenState.Initial)
            }

            when (it) {
                is RequestResult.Error -> {
                    launchWithIO {
                        _commonState.emit(ScreenState.Error(it.mapToBaseEntity()))
                    }
                }

                is RequestResult.Success -> _screenState.value =
                    ScreenState.Result(OtpScreenState.OtpConfirmed(it.body))
            }
        })

    }

    fun confirmRegisterOtp(otpType: OtpType, value: String, otpCode: String) {
        mConfirmRegisterOtpJob?.cancel()
        mConfirmRegisterOtpJob =
            sendRequest({ authInteractor.confirmRegisterOtp(otpType, value, otpCode) }, {
                launchWithIO {
                    _commonState.emit(ScreenState.Initial)
                }

                when (it) {
                    is RequestResult.Error -> {
                        launchWithIO {
                            _commonState.emit(ScreenState.Error(it.mapToBaseEntity()))
                        }
                    }

                    is RequestResult.Success -> _screenState.value =
                        ScreenState.Result(OtpScreenState.OtpConfirmed(it.body))
                }
            })

    }

    fun getRefreshToken(refreshToken: String) {
        mRefreshTokenJob?.cancel()
        mRefreshTokenJob =
            sendRequest({ authInteractor.getRefreshToken(refreshToken) }, {
                launchWithIO {
                    _commonState.emit(ScreenState.Initial)
                }

                when (it) {
                    is RequestResult.Error -> {
                        launchWithIO {
                            _commonState.emit(ScreenState.Error(it.mapToBaseEntity()))
                        }
                    }

                    is RequestResult.Success -> _screenState.value =
                        ScreenState.Result(LaunchScreenState.RefreshToken(it.body))
                }
            })

    }
}

sealed class SignInScreenState {
    data class LoginWithPhoneResult(val baseEntity: BaseEntity) : SignInScreenState()
    data class LoginWithGmailResult(val baseEntity: BaseEntity) : SignInScreenState()
}

sealed class OtpScreenState {
    data class OtpConfirmed(val comOtpEntity: ConfirmOtpEntity) : OtpScreenState()
}

sealed class LaunchScreenState {
    data class RefreshToken(val refreshTokenEntity: RefreshTokenEntity) : LaunchScreenState()
}