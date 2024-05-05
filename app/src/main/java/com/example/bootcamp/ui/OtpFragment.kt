package com.example.bootcamp.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentOtpBinding
import com.example.bootcamp.ui.viewModel.AuthViewModel
import com.example.bootcamp.ui.viewModel.AuthViewModelProvider
import com.example.bootcamp.ui.viewModel.BaseViewModel
import com.example.bootcamp.ui.viewModel.OtpScreenState
import com.example.common.widgets.otp.OTPListener
import com.example.data.managers.UserManager
import com.example.domain.entity.OtpType
import com.example.domain.interactors.AuthInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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

    private var otp = ""

    private val args: OtpFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOtpBinding.bind(view)

        setupUI()
        initViewModel()
    }

    private fun setupUI() {

        binding.apply {
            otpView.otpListener = object : OTPListener {
                override fun onInteractionListener() {
                }

                override fun onOTPComplete(otp: String) {
                    if (otp.trim().isNotBlank()) {
                        this@OtpFragment.otp = otp

                        if (args.isRegister)
                            mAuthViewModel.confirmRegisterOtp(OtpType.PHONE, args.phone, otp)
                        else mAuthViewModel.confirmOtp(OtpType.PHONE, args.phone, otp)
                    }
                }
            }
            otpView.requestFocusOTP()

            btnResendOtp.setOnClickListener {
                otpView.setOTP("")
                mAuthViewModel.loginWithPhone(args.phone)
            }

            btnBack.setOnClickListener {
                mBaseActivity.onBackPressed()
            }

        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mAuthViewModel.screenState().collectLatest {
                    if (it is BaseViewModel.ScreenState.Result<*> && it.data is OtpScreenState) {
                        when (val data = it.data) {
                            is OtpScreenState.OtpConfirmed -> {
                                with(data.comOtpEntity) {
                                    if (body == null || body?.access.isNullOrEmpty() || body?.refresh.isNullOrEmpty())
                                        showErrorDialog()
                                    else {
                                        UserManager.setToken(body?.access!!)
                                        UserManager.setRefreshToken(body?.refresh!!)

                                        binding.otpView.showSuccess()

                                        delay(1000)

                                        findNavController().navigate(R.id.action_otpFragment_to_setupPinFragment)
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