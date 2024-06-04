package com.example.bootcamp.base

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.bootcamp.R
import com.example.bootcamp.databinding.ActivityMainBinding
import com.example.bootcamp.listener.SimpleClickListener
import com.example.bootcamp.ui.navigation.TabManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityMainBinding: ActivityMainBinding
    val tabManager: TabManager by lazy { TabManager(this, mainActivityMainBinding) }


    companion object {
        init {
            System.loadLibrary("bootcamp")
        }

        external fun add(a: Int, b: Int): Int
        external fun subtract(a: Int, b: Int): Int
        external fun multiply(a: Int, b: Int): Int
        external fun divide(a: Float, b: Float): Float
        external fun apiKey(): String
    }

    data class User(val name: String, val age: Int, val w: Double)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        Firebase.database.reference.child("Ayaz/age")
//            .setValue(90.0)

//        Firebase.database.reference.child("Ayaz")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("DDDDDDDDD 1", snapshot.toString())
//                    Log.d("DDDDDDDDD 2", snapshot.child("age").toString())
//
//                    snapshot.children.forEach {
//                        Log.d("DDDDDDDDD 3", it.value.toString())
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })

//        Firebase.database.reference.child("Ayaz")
//            .addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d("DDDDDDDDD 1", snapshot.toString())
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d("DDDDDDDDD 2", snapshot.toString())
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                    Log.d("DDDDDDDDD 3", snapshot.toString())
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                    Log.d("DDDDDDDDD 4", snapshot.toString())
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//            })

//        Firebase.database.reference.child("Ayaz/age").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("DDDDDDDDD 5", snapshot.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })

//        Firebase.database.reference.child("Ayaz/age").removeValue()

        Firebase.database.reference.child("Ayaz/age").setValue(45)

        Log.d("DDDDDDDDD", add(3, 4).toString())
        Log.d("DDDDDDDDD", subtract(3, 4).toString())
        Log.d("DDDDDDDDD", multiply(3, 4).toString())
        Log.d("DDDDDDDDD", divide(10f, 4f).toString())

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

    fun showSnackBar(
        message: String?,
        @StringRes actionTitle: Int? = null,
        action: (() -> Unit)? = null
    ) {
        message?.let {
            mainActivityMainBinding.root.let { v ->
                Snackbar.make(v, it, Snackbar.LENGTH_LONG)
                    .setAction(actionTitle ?: R.string.close) {
                        action?.invoke()
                    }.show()
            }
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
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)

        if (positiveClickListener == null) {
            alertDialog.setPositiveButton(positiveBtnTitle) { dialog, _ ->
                dialog.cancel()
            }
        } else {
            alertDialog.setPositiveButton(positiveBtnTitle) { dialog, _ ->
                positiveClickListener.click()
                dialog.cancel()
            }
        }

        negativeClickListener?.let {
            alertDialog.setNegativeButton(negativeBtnTitle) { dialog, _ ->
                negativeClickListener.click()
                dialog.dismiss()
            }
        }

        alertDialog.setCancelable(cancelable)
        alertDialog.create().show()
    }
}