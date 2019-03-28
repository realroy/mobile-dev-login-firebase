package com.example.loginfirebase

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_new_post.*
import java.io.IOException


class NewPostActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mStorageRef: StorageReference
    private val REQUEST_PICK_PHOTO = 1
    private lateinit var imageUri: Uri
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        mAuth = FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference

        progressDialog = ProgressDialog(this)
        val database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    databaseReference.setValue("users")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(baseContext, p0.message.toString(), Toast.LENGTH_LONG).show()
            }
        })
        val usersRef = databaseReference.child("users").ref
        val uid = mAuth.currentUser?.uid.toString()
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    usersRef.setValue(uid)
                    usersRef.child(uid).setValue("images")
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        selectButton.setOnClickListener { onSelectFromGallery() }
        newPostButton.setOnClickListener { onUpload() }
    }

    fun assignPathString(name: String): String {
        val unixTime = System.currentTimeMillis() / 1000L
        return "images/$unixTime-$name.jpg"
    }

    fun onUpload() {
        val name = nameEditText.text.toString()
        if (name.isEmpty()) {
            return
        }
        progressDialog.setMessage("Uploading image ...")
        progressDialog.show()
        val pathString = assignPathString(name)
        mStorageRef
            .child(pathString)
            .putFile(this.imageUri)
            .addOnSuccessListener { t -> onUploadSuccess(t) }
            .addOnFailureListener { e ->
                {
                    progressDialog.setMessage("Upload failed.")
                    progressDialog.dismiss()
                    Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()

                }
            }
    }

    private fun onUploadSuccess(t: UploadTask.TaskSnapshot) {
        progressDialog.setMessage("Add image path to database.")
        databaseReference
            .child("users")
            .child(mAuth.currentUser?.uid.toString())
            .child("images")
                
            .push(t.metadata?.path)
            .setValue(t.metadata?.path)
            .addOnCompleteListener {
                Toast.makeText(
                    baseContext,
                    "Image has uploaded successfully.",
                    Toast.LENGTH_LONG
                ).show()
            }
        progressDialog.dismiss()
    }


    private fun onSelectFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_PHOTO)
    }

    private fun isImage(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return true
        return mimeType.startsWith("image/")
    }


    private fun importPhoto(uri: Uri): Boolean {
        if (!isImage(baseContext, uri)) {
            return false
        }
        return try {
            imageUri = uri
            imageView.setImageURI(uri)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
                val clipData = data?.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        importPhoto(uri)

                    }
                } else { // handle single photo
                    val uri = data?.data
                    if (uri != null) importPhoto(uri)
                }
            }
        }
    }

}
