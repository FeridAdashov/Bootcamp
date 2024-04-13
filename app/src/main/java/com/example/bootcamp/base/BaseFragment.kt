package com.example.bootcamp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.data.managers.UserManager
//import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint
abstract class BaseFragment : Fragment(), BaseFragmentListener {

    abstract val TAG: String

    abstract val withBottomBar: Boolean

    val mBaseActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}
