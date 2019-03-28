package com.example.loginfirebase

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_view_posts.*
import java.util.*

class ViewPostsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var dataset = arrayListOf<Item>()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recycleAdapter: RecycleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_posts)


        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val uid = mAuth.currentUser?.uid.toString()

        recycleAdapter = RecycleAdapter(dataset)
        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recycleAdapter
        }
        recycleAdapter.notifyDataSetChanged()

        databaseReference.child("users").child(uid).child("images").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset = arrayListOf()
                for (p in dataSnapshot.children) {
                    dataset.add(p.getValue(Item::class.java)!!)
                }
                recycleAdapter = RecycleAdapter(dataset)
                recycleAdapter.notifyDataSetChanged()
                Log.d("a", recycleAdapter.getItemId(0).toString())
                recycleView.adapter = recycleAdapter
                Log.d("b", "b")
            }

        })



    }

    fun fetch() {

    }

}
