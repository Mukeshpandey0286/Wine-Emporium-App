package com.example.flavorfiesta

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminflavorfiesta.Models.UserModel
import com.example.flavorfiesta.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference
//for pop up all accounts showing in screen
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

//initiatlize firebase google auth
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.signupBtn.setOnClickListener {
            // Getting all details and storing them in variables
            userName = binding.username.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.pass.text.toString().trim()

            createAccount(email,password)

        }

        binding.googleLogin.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        binding.alredyAcc.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Account created Successfully!😎", Toast.LENGTH_SHORT).show()
                    saveUserData()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Account creation Failed!😒", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //    for update ui our
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Save data in the database
    private fun saveUserData() {
        // Getting all details and storing them in variables
        userName = binding.username.text.toString()
        email = binding.email.text.toString().trim()
        password = binding.pass.text.toString().trim()

        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Save data in the Firebase database
        database.child("user").child(userId).setValue(user)
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
                        Toast.makeText(this, "Google Authentication is Failed!😥", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Please try again!🤦‍♂️", Toast.LENGTH_SHORT).show()
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
