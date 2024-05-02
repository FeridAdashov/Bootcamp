package com.example.bootcamp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.bootcamp.R
import com.example.bootcamp.base.BaseFragment
import com.example.bootcamp.databinding.FragmentLaunchBinding
import com.example.bootcamp.ui.viewModel.BaseViewModel


class LaunchFragment : BaseFragment(R.layout.fragment_launch) {
    override val TAG: String
        get() = "Launch Fragment"
    override val withBottomBar: Boolean
        get() = false

    override val viewModel: BaseViewModel
        get() = BaseViewModel()

    private lateinit var binding: FragmentLaunchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaunchBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkLogin()
        }, 500)

        binding.swipeToRefresh.setOnRefreshListener {
            checkLogin()
        }
    }
}