package com.example.bootcamp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentSignInBinding
import com.example.bootcamp.ui.viewModel.AuthViewModel
import com.example.bootcamp.ui.viewModel.AuthViewModelProvider
import com.example.bootcamp.ui.viewModel.BaseViewModel
import com.example.bootcamp.ui.viewModel.SignInScreenState
import com.example.common.extensions.hideKeyboard
import com.example.common.utils.CommonUtils
import com.example.data.managers.UserManager
import com.example.data.repositories.AuthRepositoryImpl
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
import com.example.domain.interactors.AuthInteractor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    override val TAG: String
        get() = "Sign In Fragment"
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

//    @Inject
//    lateinit var mAuthViewModel: AuthViewModel

    private var phone = ""

    private lateinit var binding: FragmentSignInBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)

        getView()?.isFocusableInTouchMode = true
        getView()?.requestFocus()
        getView()?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mBaseActivity.finish()
                true
            } else false
        }

        setupUI()
        initViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        UserManager.setToken("")

        binding.apply {
            tieMobileNumber.setOnFocusChangeListener { _, b ->
                tilMobileNumber.run {
                    if (b) {
                        hint = getString(R.string.mobile_number)
                        prefixText = "+994 "
                    } else {
                        if (tieMobileNumber.text.isNullOrEmpty()) {
                            hint = getString(R.string.mobile_number_with_994)
                            prefixText = ""
                        }
                    }
                }
            }

            tieMobileNumber.doOnTextChanged { text, _, _, count ->
                if (count == 1 && text?.length!! > 1) {
                    tieMobileNumber.setText(CommonUtils.maskPhoneNumber(text.toString()))
                    tieMobileNumber.setSelection(tieMobileNumber.text?.length ?: 0)
                }
            }

            tvVersion.text = getString(R.string.e_vekil_version) + " " + BuildConfig.VERSION_NAME

            btnLogin.setOnClickListener {
                val phoneEdt = tieMobileNumber.text.toString()
                if (phoneEdt.length == 12) {
                    phone = "994" + phoneEdt.replace(" ", "")

                    requireContext().hideKeyboard(tieMobileNumber)
                    showProgress()
                    mAuthViewModel.loginWithPhone(phone)
                } else tieMobileNumber.error = getString(R.string.fill_the_field)
            }

            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            btnSignInApple.setOnClickListener {
            }

            btnSignInGoogle.setOnClickListener {
            }

            btnSignInFacebook.setOnClickListener {
            }
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mAuthViewModel.screenState().collectLatest {
                    if (it is BaseViewModel.ScreenState.Result<*> && it.data is SignInScreenState) {
                        when (it.data) {
                            is SignInScreenState.LoginWithPhoneResult -> {
                                findNavController().navigate(
                                    R.id.action_signInFragment_to_otpFragment,
                                    Bundle().apply { putString("phone", phone) })
                            }
                        }
                    }
                }
            }
        }
    }
}