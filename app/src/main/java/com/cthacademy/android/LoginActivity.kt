package com.cthacademy.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.javiersantos.appupdater.AppUpdater
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize


class LoginActivity : AppCompatActivity() {

    private lateinit var loading : RelativeLayout
    private fun initDebug() {
        // [START appcheck_initialize_debug]
        Firebase.initialize(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        // [END appcheck_initialize_debug]
    }
    private fun init() {
        // [START appcheck_initialize]
        Firebase.initialize (this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        // [END appcheck_initialize]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        FirebaseApp.initializeApp(this)
//        init()
        initDebug()
        // Initialize Firebase Authentication
        val mAuth = FirebaseAuth.getInstance()
        loading = findViewById(R.id.Loading)

        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // Retrieve the login state
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val phone = sharedPreferences.getString("user", null)

        if (isLoggedIn) {
            loading.visibility = View.VISIBLE
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("users").document(phone.toString())
            userDocRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // if the document exists, get the user's data as a custom data class
                    val name = document.getString("name")
                    val date = document.getString("date")
                    if (name != null) {
                        if (date != null) {
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("name", name)
                                putExtra("date", date)
                                putExtra("phone", phone)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
        } else {
            // User is not signed in, show sign in screen or do something else
            val switchLoginFragment = SwitchLoginFragment()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.login_frame, switchLoginFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

}