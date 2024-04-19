package com.example.bootcamp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bootcamp.R
import com.example.data.managers.UserManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseFragment(private val layoutId: Int) : Fragment(layoutId), BaseFragmentListener {

    abstract val TAG: String

    abstract val withBottomBar: Boolean

    val mBaseActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun checkLogin() {
        if (UserManager.isLogged() && !UserManager.refreshToken().isNullOrEmpty()) {
//            showProgress()
//            mMainViewModel.refreshToken(mUserManager.refreshToken()!!)
        } else {
            findNavController().navigate(R.id.action_to_signInFragment)
        }
    }

}
