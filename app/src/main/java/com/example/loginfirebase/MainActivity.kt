package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        signInButton.setOnClickListener { view -> onClickSignIn(view) }
        signUpButton.setOnClickListener { view -> onClickSignUp(view) }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            onValidAuth()
        }
    }

    fun onValidAuth() {
        val user = auth.currentUser
        val intent = Intent(baseContext, UserProfileActivity::class.java)
        intent.putExtra("email", user?.email)
        startActivity(intent)
    }

    fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }

    fun onClickSignIn(view: View?) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        validateInput(email, password)
        val TAG = "onClickSignIn"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    onValidAuth()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed." + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    fun onClickSignUp(view: View?) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        validateInput(email, password)
        val TAG = "onClickSignUp"
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext, "Authentication Success",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

    }
}
