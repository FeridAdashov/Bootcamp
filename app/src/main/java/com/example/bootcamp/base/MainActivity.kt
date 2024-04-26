package com.example.bootcamp.base

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.bootcamp.R
import com.example.bootcamp.databinding.ActivityMainBinding
import com.example.bootcamp.ui.navigation.TabManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityMainBinding: ActivityMainBinding
    val tabManager: TabManager by lazy { TabManager(this, mainActivityMainBinding) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
    }

    private fun setupUI() {
        tabManager.currentController = tabManager.navDashboardController

        mainActivityMainBinding.bottomBarView.setOnItemSelectedListener { item ->
            tabManager.switchTab(item.itemId)
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                tabManager.onBackPressed()
            }
        })

        mainActivityMainBinding.fabBottomMenuAdd.setOnClickListener {
            tabManager.openSearchPageForAddBooking()
        }
    }

    fun setBaseProgressBarVisibility(boolean: Boolean) {
        mainActivityMainBinding.baseProgressBar.isVisible = boolean
    }
}