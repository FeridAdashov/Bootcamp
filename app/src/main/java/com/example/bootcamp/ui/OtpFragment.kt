package com.example.bootcamp.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentOtpBinding
import com.example.bootcamp.ui.viewModel.AuthViewModel
import com.example.bootcamp.ui.viewModel.AuthViewModelProvider
import com.example.bootcamp.ui.viewModel.BaseViewModel
import com.example.domain.interactors.AuthInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment : BaseFragment(R.layout.fragment_otp) {

    override val TAG: String
        get() = "Otp Fragment"
    override val withBottomBar: Boolean
        get() = false
    override val viewModel: BaseViewModel
        get() = mAuthViewModel

    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    private val mAuthViewModel: AuthViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            AuthViewModelProvider(mAuthInteractor)
        }
    )

    private lateinit var binding: FragmentOtpBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOtpBinding.bind(view)

        setupUI()
        initViewModel()
    }

    private fun setupUI() {

        binding.apply {
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                mAuthViewModel.screenState().collectLatest {
//                    if (it is BaseViewModel.ScreenState.Result<*> && it.data is SignInScreenState) {
//                        when (it.data) {
//
//                        }
//                    }
//                }
            }
        }
    }
}