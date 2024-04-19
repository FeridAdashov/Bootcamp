package com.example.bootcamp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.domain.entity.RequestResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    fun <T> sendRequest(
        call: suspend () -> RequestResult<T>,
        getData: (RequestResult<T>) -> Unit
    ): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            getData.invoke(call.invoke())
        }
    }
}