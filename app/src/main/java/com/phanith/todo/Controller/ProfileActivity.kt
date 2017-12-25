package com.phanith.todo.Controller

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.phanith.todo.R
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.util.Log
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.phanith.todo.Model.FirebaseTree
import com.phanith.todo.R.id.signOut
import com.squareup.picasso.Picasso
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.phanith.todo.Model.User
import android.provider.Contacts.People
import android.text.method.TextKeyListener.clear
import android.widget.*
import org.json.JSONObject
import java.util.*

val USER_PROFILE: String = "USER_PROFILE"
val USER_PROFILE_IMAGE_URL: String = "URL"

class ProfileActivity : AppCompatActivity() {

    lateinit var english: RadioButton
    lateinit var khmer: RadioButton
    lateinit var mDatabase: DatabaseReference
    lateinit var currentUser: User
    lateinit var listener: ValueEventListener

    lateinit var username: TextView
    lateinit var active: TextView
    lateinit var email: TextView
    lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        instanceCurrentUser()
        findViewById<Button>(R.id.signOut).setOnClickListener {
            logout()
        }
        setupRadioButton()
        trackUserDataChanges()
    }

    private fun setProfileImageUrlString(value: String) {
        val preference = getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString(USER_PROFILE_IMAGE_URL, value)
        editor.apply()
    }

    private fun instanceCurrentUser() {
        currentUser = User()
    }

    private fun instanceProfile() {
        username = findViewById(R.id.name)
        username.text = currentUser.name

        profileImage = findViewById(R.id.profile_image)
        Picasso.with(this).load(currentUser.profileImageUrl).into(profileImage)

        active = findViewById(R.id.active)
        active.text = currentUser.active

        email = findViewById(R.id.email)
        email.text = currentUser.email
    }

    private fun trackUserDataChanges() {

        mDatabase = FirebaseDatabase.getInstance().reference.child(FirebaseTree.Users.toString()).child(FirebaseAuth.getInstance().uid)
        listener = object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot?) {

                currentUser.name = p0?.child("Username")?.value.toString()
                currentUser.uid = p0?.child("Uid")?.value.toString()
                currentUser.profileImageUrl = p0?.child("ProfileImageUrl")?.value.toString()
                currentUser.language = p0?.child("Language")?.value.toString()
                currentUser.active = p0?.child("Active")?.value.toString()
                currentUser.email = p0?.child("Email")?.value.toString()

                instanceProfile()
                setProfileImageUrlString(currentUser.profileImageUrl)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        }
        mDatabase.addListenerForSingleValueEvent(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatabase.removeEventListener(listener)
    }

    private fun logout() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        LoginManager.getInstance().logOut()
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
    }

    private fun englishChecked(value: Boolean) {
        Toast.makeText(this, if (value) "English checked" else "English unchecked", Toast.LENGTH_SHORT).show()
    }

    private fun khmerChecked(value: Boolean) {
        Toast.makeText(this, if (value) "Khmer checked" else "Khmer unchecked", Toast.LENGTH_SHORT).show()
    }


    private fun setupRadioButton() {
        english = findViewById(R.id.english)
        khmer = findViewById(R.id.khmer)

        khmer.isChecked = !english.isChecked

        english.setOnCheckedChangeListener { _, isChecked ->
            englishChecked(isChecked)
            val locale = Locale("en_US")
            Locale.setDefault(locale)
//            val config = Configuration()
//            config.locale = locale
//
            this.resources.configuration.setLocale(locale)

        }

        khmer.setOnCheckedChangeListener { _, isChecked ->
            khmerChecked(isChecked)
            val locale = Locale("km_rKH")
            Locale.setDefault(locale)
//            val config = Configuration()
//            config.locale = locale
//            this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
            this.resources.configuration.setLocale(locale)

        }
    }
}
