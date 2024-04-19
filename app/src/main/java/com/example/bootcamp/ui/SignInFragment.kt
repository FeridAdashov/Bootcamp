package com.example.bootcamp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentSignInBinding
import com.example.bootcamp.ui.viewModel.AuthViewModel
import com.example.bootcamp.ui.viewModel.AuthViewModelProvider
import com.example.common.extensions.hideKeyboard
import com.example.common.utils.CommonUtils
import com.example.data.managers.UserManager
import com.example.domain.Constants
import com.example.domain.entity.BaseEntity
import com.example.domain.interactors.AuthInteractor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    override val TAG: String
        get() = "Sign In Fragment"
    override val withBottomBar: Boolean
        get() = false

    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    private val mAuthViewModel: AuthViewModel by navGraphViewModels(
        R.id.nav_graph,
        factoryProducer = {
            AuthViewModelProvider(mAuthInteractor)
        }
    )

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
        fun errorHappened(it: BaseEntity) {
            hideProgress()

            when (it.code) {
                Constants.UNAUTHORIZED_EXCEPTION -> showDialogWithAction(
                    getString(R.string.error),
                    mAuthViewModel.getErrorMessage(requireContext(), it)
                )

                Constants.UNKNOWN_ERROR -> Log.d(
                    "${Constants.COMMON_TAG}: $TAG",
                    it.message.toString()
                )

                else -> showDialogWithAction(
                    getString(R.string.error),
                    mAuthViewModel.getErrorMessage(requireContext(), it)
                )
            }
        }

        mAuthViewModel.loginWithPhoneLiveData().observe(viewLifecycleOwner) { baseEntity ->
            hideProgress()

            if (baseEntity is RequestResult.Success) {
                when (baseEntity.code) {
                    200 -> {
                        findNavController().navigate(
                            R.id.action_signInFragment_to_otpFragment,
                            Bundle().apply { putString("phone", phone) })
                    }

                    else -> showSnackBar(
                        mAuthViewModel.getErrorMessage(
                            requireContext(),
                            baseEntity.body
                        )
                    )
                }
            } else if (baseEntity is RequestResult.Error)
                errorHappened(BaseEntity(baseEntity.code, baseEntity.message))
        }
    }
}