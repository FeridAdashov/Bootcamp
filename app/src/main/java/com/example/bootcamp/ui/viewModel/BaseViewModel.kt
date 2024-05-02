package com.example.bootcamp.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bootcamp.R
import com.example.domain.Constants
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected var _commonState = MutableSharedFlow<ScreenState>()
    val commonState = _commonState.asSharedFlow()

    protected var _screenState =
        MutableStateFlow<ScreenState>(ScreenState.Initial)

    fun screenState(): StateFlow<ScreenState> = _screenState

    fun <T> sendRequest(
        call: suspend () -> RequestResult<T>,
        getData: (RequestResult<T>) -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            getData.invoke(call.invoke())
        }
    }

    fun launchWithIO(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        block.invoke()
    }

    fun launchWithMain(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.Main) {
        block.invoke()
    }


    fun getErrorMessage(context: Context, info: BaseEntity): String {
        return when (info.code) {
            Constants.SOCKET_EXCEPTION -> info.message
                ?: context.getString(R.string.socket_exception)

            Constants.SOCKET_TIMEOUT_EXCEPTION -> context.getString(R.string.timeout_exception)
            Constants.UNKNOWN_ERROR -> info.message ?: context.getString(R.string.unknown_error)
            Constants.CONNECTION_EXCEPTION -> context.getString(R.string.check_internet_connection)
            Constants.UNAUTHORIZED_EXCEPTION -> info.message
                ?: context.getString(R.string.user_not_found)

            Constants.WRONG_DATA_EXCEPTION -> context.getString(R.string.data_parse_error)
            Constants.SSL_EXCEPTION -> context.getString(R.string.ssl_exception)
            Constants.BAD_REQUEST -> info.message ?: context.getString(R.string.have_troubles)
            Constants.NOT_FOUND -> info.message ?: context.getString(R.string.data_not_found)
            else -> info.message ?: context.getString(R.string.have_troubles)
        }
    }

    sealed class ScreenState {
        data object Initial : ScreenState()
        data object Loading : ScreenState()
        data class Result<T>(val data: T) : ScreenState()
        data class Error(val entity: BaseEntity) : ScreenState()
    }
}