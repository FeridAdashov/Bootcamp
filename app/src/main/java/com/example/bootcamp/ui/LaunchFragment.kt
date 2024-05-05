package com.example.bootcamp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentLaunchBinding
import com.example.bootcamp.ui.viewModel.AuthViewModel
import com.example.bootcamp.ui.viewModel.AuthViewModelProvider
import com.example.bootcamp.ui.viewModel.BaseViewModel
import com.example.bootcamp.ui.viewModel.LaunchScreenState
import com.example.data.managers.UserManager
import com.example.domain.interactors.AuthInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LaunchFragment : BaseFragment(R.layout.fragment_launch) {
    override val TAG: String
        get() = "Launch Fragment"
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

    private lateinit var binding: FragmentLaunchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaunchBinding.bind(view)
        initViewModel()
        setupUI()
    }

    private fun checkLogin() {
        if (UserManager.isLogged()) {
            mAuthViewModel.getRefreshToken(UserManager.refreshToken()!!)
        } else {
            findNavController().navigate(R.id.action_launchFragment_to_signInFragment)
        }
    }

    private fun setupUI() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
        }, 500)

        binding.swipeToRefresh.setOnRefreshListener {
            checkLogin()
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mAuthViewModel.screenState().collectLatest {
                    if (it is BaseViewModel.ScreenState.Result<*> && it.data is LaunchScreenState) {
                        when (val data = it.data) {
                            is LaunchScreenState.RefreshToken -> {
                                with(data.refreshTokenEntity) {
                                    if (accessToken.isNullOrEmpty())
                                        showErrorDialog("Token is null")
                                    else {
                                        UserManager.setToken(accessToken!!)
                                        findNavController().navigate(R.id.action_launchFragment_to_dashboardFragment)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}