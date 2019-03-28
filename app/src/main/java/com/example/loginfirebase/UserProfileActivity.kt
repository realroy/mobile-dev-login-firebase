package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        auth = FirebaseAuth.getInstance()
        titleTextView.text = String.format("Welcome, %s ", auth.currentUser?.email)

        signOutButton.setOnClickListener { onSignOut() }
        newPostbutton.setOnClickListener { onCreateNewPost() }
        viewPostsButton.setOnClickListener { onViewPosts() }

    }

    private fun onCreateNewPost() {
        val intent = Intent(this, NewPostActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onViewPosts() {
        val intent = Intent(this, ViewPostsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onSignOut() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
