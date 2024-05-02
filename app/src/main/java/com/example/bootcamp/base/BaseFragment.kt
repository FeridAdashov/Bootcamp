package com.example.bootcamp.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bootcamp.R
import com.example.bootcamp.listener.SimpleClickListener
import com.example.bootcamp.ui.viewModel.BaseViewModel
import com.example.data.managers.UserManager
import com.example.domain.Constants
import com.example.domain.entity.BaseEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
abstract class BaseFragment(layoutId: Int) : Fragment(layoutId), BaseFragmentListener {

    abstract val TAG: String

    abstract val withBottomBar: Boolean

    abstract val viewModel: BaseViewModel

    val mBaseActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.commonState.collectLatest {
                    setProgressBarVisibility(false)

                    when (it) {
                        is BaseViewModel.ScreenState.Error -> {
                            errorHappened(it.entity)
                        }

                        BaseViewModel.ScreenState.Initial -> {
                            setProgressBarVisibility(false)
                        }

                        BaseViewModel.ScreenState.Loading -> {
                            setProgressBarVisibility(true)
                        }

                        is BaseViewModel.ScreenState.Result<*> -> {}
                    }
                }
            }
        }
    }

    protected fun setProgressBarVisibility(boolean: Boolean) {
        mBaseActivity.setBaseProgressBarVisibility(boolean)
    }

    fun checkLogin() {
        if (UserManager.isLogged() && !UserManager.refreshToken().isNullOrEmpty()) {
//            mMainViewModel.refreshToken(mUserManager.refreshToken()!!)
        } else {
            findNavController().navigate(R.id.action_global_signInFragment)
        }
    }

    private fun errorHappened(it: BaseEntity) {
        when (it.code) {
            Constants.UNAUTHORIZED_EXCEPTION -> showDialogWithAction(
                getString(R.string.error),
                viewModel.getErrorMessage(requireContext(), it)
            )

            Constants.UNKNOWN_ERROR -> Log.d(
                "${Constants.COMMON_TAG}: $TAG",
                it.message.toString()
            )

            else -> showDialogWithAction(
                getString(R.string.error),
                viewModel.getErrorMessage(requireContext(), it)
            )
        }
    }

    fun showDialogWithAction(
        title: String,
        description: String,
        positiveClickListener: SimpleClickListener? = null,
        negativeClickListener: SimpleClickListener? = null,
        cancelable: Boolean = true,
        positiveBtnTitle: String = getString(R.string.OK),
        negativeBtnTitle: String = getString(R.string.cancel),
    ) {
        mBaseActivity.showDialogWithAction(
            title,
            description,
            positiveClickListener,
            negativeClickListener,
            cancelable,
            positiveBtnTitle,
            negativeBtnTitle
        )
    }

    fun showErrorDialog(
        title: String = getString(R.string.warning),
        description: String = getString(R.string.have_troubles)
    ) {
        showDialogWithAction(title, description)
    }

    fun showSnackBar(
        message: String?,
        @StringRes actionTitle: Int? = null,
        action: (() -> Unit)? = null
    ) {
        mBaseActivity.showSnackBar(message, actionTitle, action = action)
    }
}
