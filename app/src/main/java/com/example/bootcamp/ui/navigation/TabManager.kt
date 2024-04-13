package com.example.bootcamp.ui.navigation

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bootcamp.R
import com.example.bootcamp.base.MainActivity
import com.example.bootcamp.databinding.ActivityMainBinding

class TabManager(
    private val mainActivity: MainActivity,
    private val mainActivityBinding: ActivityMainBinding
) {

    private val startDestinations = mapOf(
        R.id.dashboardFragment to R.id.dashboardFragment,
        R.id.historyFragment to R.id.historyFragment,
        R.id.settingsFragment to R.id.settingsFragment,
        R.id.calendarFragment to R.id.calendarFragment,
    )

    private var currentTabId: Int = R.id.dashboardFragment

    var currentController: NavController? = null

    var currentContainer: View? = null

    private var tabHistory = TabHistory().apply { push(R.id.dashboardFragment) }

    val navDashboardController: NavController by lazy {
        mainActivity.findNavController(R.id.dashboardTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(R.id.launchFragment)
            }
        }
    }

    private val navHistoryController: NavController by lazy {
        mainActivity.findNavController(R.id.historyTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(startDestinations.getValue(R.id.historyFragment))
            }
        }
    }

    private val navSettingsController: NavController by lazy {
        mainActivity.findNavController(R.id.settingsTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(startDestinations.getValue(R.id.settingsFragment))
            }
        }
    }

    private val navCalendarController: NavController by lazy {
        mainActivity.findNavController(R.id.calendarTab).apply {
            graph = navInflater.inflate(R.navigation.nav_graph).apply {
                setStartDestination(startDestinations.getValue(R.id.calendarFragment))
            }
        }
    }

    private val dashboardsTabContainer: View by lazy { mainActivityBinding.dashboardContainer }
    private val historyTabContainer: View by lazy { mainActivityBinding.historyContainer }
    private val settingsTabContainer: View by lazy { mainActivityBinding.settingsContainer }
    private val calendarTabContainer: View by lazy { mainActivityBinding.calendarContainer }


    fun onBackPressed() {
        currentController?.let {
            if (it.currentDestination == null || it.currentDestination?.id == startDestinations.getValue(
                    currentTabId
                )
            ) {
                if (tabHistory.size > 1) {
                    val tabId = tabHistory.popPrevious()
                    switchTab(tabId, false)
                    mainActivityBinding.bottomBarView.menu.findItem(tabId).isChecked = true
                } else {
                    mainActivity.finish()
                }
            }
            it.popBackStack()
        } ?: run {
            mainActivity.finish()
        }
    }

    fun switchTab(tabId: Int, addToHistory: Boolean = false) {
        currentTabId = tabId

        when (tabId) {
            R.id.dashboardFragment -> {
                currentController = navDashboardController
                currentController?.navigate(R.id.dashboardFragment)
                invisibleTabContainerExcept(dashboardsTabContainer)
            }

            R.id.calendarFragment -> {
                currentController = navCalendarController
                invisibleTabContainerExcept(calendarTabContainer)
            }

            R.id.historyFragment -> {
                currentController = navHistoryController
                invisibleTabContainerExcept(historyTabContainer)
            }

            R.id.settingsFragment -> {
                currentController = navSettingsController
                invisibleTabContainerExcept(settingsTabContainer)
            }
        }
        if (addToHistory) {
            tabHistory.push(tabId)
        }
    }

    fun openSearchPageForAddBooking() {
        currentController?.let {
//            SearchFragment.navigate(it, SearchFragment.Companion.PageType.ADD_BOOKING)
        }
    }

    private fun invisibleTabContainerExcept(container: View) {
        dashboardsTabContainer.isInvisible = true
        historyTabContainer.isInvisible = true
        settingsTabContainer.isInvisible = true
        calendarTabContainer.isInvisible = true

        container.isVisible = true
        currentContainer = container
    }
}