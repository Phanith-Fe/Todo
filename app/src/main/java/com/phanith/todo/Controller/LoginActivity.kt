package com.phanith.todo.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.facebook.*
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.phanith.todo.R
//import android.support.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.phanith.todo.Model.User
import java.util.*
import com.google.firebase.database.*
import com.phanith.todo.Model.FirebaseTree
import com.phanith.todo.Model.KeyID
import org.json.JSONObject
import java.security.Key
import kotlin.collections.HashMap


class LoginActivity : AppCompatActivity() {

    val SIGN_IN_ID: Int = 123
    lateinit var mAuth: FirebaseAuth
    lateinit var callbackManager: CallbackManager
    lateinit var mDatabase: DatabaseReference
    lateinit var currentUser: User

    var isInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true
            }else {
                Log.d("kk","Already Initialized");
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        mDatabase = FirebaseDatabase.getInstance().reference

        setupFirebaseLogin()
        setupGoogleLogin()
        setupFacebookLogin()
        instanceCurrentUserData()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_ID) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                currentUser.name = account.displayName.toString()
                currentUser.profileImageUrl = account.photoUrl.toString()
                currentUser.email = account.email.toString()
                currentUser.active = "Should be in SharedPreference"
                currentUser.language = "Should be in SharedPreference"

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseLogin(credential)
            } catch (e: ApiException) {
                Toast.makeText(this,"Sign in to firebase failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun LoginActivity.instanceCurrentUserData(){
    currentUser = User()

    currentUser.name = ""
    currentUser.language = ""
    currentUser.active = ""
    currentUser.email = ""
    currentUser.uid = ""
    currentUser.profileImageUrl = ""
}

private fun LoginActivity.pushUserData(_user: User) {

    mDatabase = FirebaseDatabase.getInstance().reference
    var childUpdates: HashMap<String, Any> = HashMap()

    childUpdates.put(KeyID.Username.toString(), _user.name)
    childUpdates.put(KeyID.Email.toString(), _user.email)
    childUpdates.put(KeyID.Active.toString(), _user.active)
    childUpdates.put(KeyID.Language.toString(), _user.language)
    childUpdates.put(KeyID.ProfileImageUrl.toString(), _user.profileImageUrl)
    _user.uid?.let { childUpdates.put(KeyID.Uid.toString(), it) }

    mDatabase.child(FirebaseTree.Users.toString()).child(_user.uid).updateChildren(childUpdates)

}

// Setup Facebook Login
private fun LoginActivity.setupFacebookLogin(){

    FacebookSdk.sdkInitialize(applicationContext)
    callbackManager = CallbackManager.Factory.create()
    val loginButton: LoginButton = findViewById(R.id.facebook_login)
    loginButton.setReadPermissions("email", "public_profile")
    loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

        override fun onSuccess(loginResult: LoginResult) {

            val accessToken = AccessToken.getCurrentAccessToken()
            val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->

                try {

                    if (`object`.has("name")) {
                        currentUser.name = `object`.getString("name")
                    }

                    if (`object`.has("email")) {
                        currentUser.email = `object`.getString("email")
                    }

                    if (`object`.has("picture")) {
                        currentUser.profileImageUrl = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                    }

                    currentUser.active = "Should be in SharedPreference"
                    currentUser.language = "Should be in SharedPreference"

                    val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                    firebaseLogin(credential)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val parameters = Bundle()
            parameters.putString("fields", "id,name,link,email,picture,gender, birthday")
            request.parameters = parameters
            request.executeAsync()

//            val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
//            firebaseLogin(credential)
        }

        override fun onCancel() {

        }

        override fun onError(exception: FacebookException) {

        }
    })


}


// Setup Google login
private fun LoginActivity.setupGoogleLogin(){

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
    signInButton.setSize(SignInButton.SIZE_STANDARD)

    signInButton.setOnClickListener {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_ID)
    }
}


// Setup Firebase login
private fun LoginActivity.setupFirebaseLogin(){
    mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser
    if (currentUser != null) {
        getMainActivity(currentUser)
    }
}

// Intent to MainActivity
private fun LoginActivity.getMainActivity(_user: FirebaseUser?){
    val intent = Intent(applicationContext, MainActivity::class.java)
    startActivity(intent)
    finish()
}

private fun LoginActivity.firebaseLogin(credential: AuthCredential) {
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    if (mAuth.currentUser?.uid != null) {
                        currentUser.uid = mAuth.currentUser?.uid!!
                        Log.d("uid", currentUser.uid)
                        pushUserData(currentUser)
                        getMainActivity(mAuth.currentUser)
                    }
                }else{
                    getMainActivity(null)
                }
            }
}



