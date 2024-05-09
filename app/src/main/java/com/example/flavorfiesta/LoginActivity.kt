package com.example.flavorfiesta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminflavorfiesta.Models.UserModel
import com.example.flavorfiesta.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //        initializingFirebase Auth and Firebase Database
        auth = Firebase.auth
        database = Firebase.database.reference

//for pop up all accounts showing in screen
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()


//initiatlize firebase google auth
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginBtn.setOnClickListener {

//            getting all details and storing them in our variables
            email = binding.email.text.toString().trim()
            password = binding.pass.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill Valid Details", Toast.LENGTH_SHORT).show()

            } else {
                LoginUserAccount(email, password)
            }
        }
        binding.dontAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.googleLogin.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.fbLogin.setOnClickListener {

        }
    }

    private fun checkEmailExists(email: String) {
        val usersRef = database.child("user")
        usersRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email exists in the database
                        Log.d("LoginActivity", "Email exists in the database")
                        // Now you can proceed with further checks or actions
                        // For example, you can retrieve the user associated with this email
                        for (userSnapshot in dataSnapshot.children) {
                            val storedUser = userSnapshot.getValue(UserModel::class.java)
                            if (storedUser?.password == password) {
                                // Password also matches, proceed to update UI
                                updateUi(auth.currentUser)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login Successfully!❤️",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Password does not match
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Incorrect password. Please try again.🤷‍♂️",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        // Email does not exist in the database
                        Log.d("LoginActivity", "Email does not exist in the database")
                        Toast.makeText(
                            this@LoginActivity,
                            "Email not found! Please Sign Up first.😒",
                            Toast.LENGTH_SHORT
                        ).show()
                        // You may redirect to a registration page
                        redirectToRegistrationPage()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Log.e("LoginActivity", "Database error: ${databaseError.message}")
                    Toast.makeText(
                        this@LoginActivity,
                        "Database error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun LoginUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (true) {
                // Authentication successful
                val user = auth.currentUser
                // Check if email exists in the database
                checkEmailExists(email)
            }
        }
    }

    //    function for redirecting our Activities
    private fun redirectToRegistrationPage() {
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        intent.putExtra("preFilledEmail", email)
        startActivity(intent)
        finish()
    }

    //    for update ui our
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //    launcher for google sign-in :->
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                        authTask ->
                    if(authTask.isSuccessful){
                        updateUi(user = null)
                        Toast.makeText(this, "Google Authentication is Successful!😁", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Google Authentication is Failed!😒", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Google Authentication is Failed!😥", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //    cheack if user is already login so redirecting them to main activity
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}


