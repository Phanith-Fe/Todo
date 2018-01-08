package com.phanith.todo.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.phanith.todo.Adapter.TodoAdapter
import com.phanith.todo.Model.FirebaseTree
import com.phanith.todo.Model.KeyID
import com.phanith.todo.Model.TodoModel
import com.phanith.todo.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var listener: ValueEventListener
    lateinit var todos: ArrayList<TodoModel>
    lateinit var plusButton: Button

    //MARK: Initialize and getData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupProfileImageOnMainActivity()
        setupAddButtonOnMainActivity()
        getNotesFromFirebase()
    }

    //MARK: Save current date to firebase everytime user open the app
    override fun onStart() {
        super.onStart()

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child(FirebaseTree.Users.toString()).child(uid).child(KeyID.Active.toString()).setValue(getCurrentDate())
    }

    //MARK: Setup add button on the right bar
    private fun setupAddButtonOnMainActivity(){
        plusButton = findViewById<Button>(R.id.plus)
        plusButton.setOnClickListener {

            val intent = Intent(this, TodoActivity::class.java)
            val params = Bundle()
            params.putString(titleKey, "")
            params.putString(noteKey, "")
            params.putString(uidKey, "null")
            params.putString(isEditKey, "no")
            intent.putExtra(paramsKey, params)
            startActivity(intent)
        }
    }

    //MARK: Setup and get profile image on the left bar
    private  fun setupProfileImageOnMainActivity(){
        val profileImage = findViewById<CircleImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            val profileActivity = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(profileActivity)
        }

        val profileImageUrl = getProfileImageUrlString()
        if (profileImageUrl != "" ) {
            Picasso.with(this).load(profileImageUrl).into(profileImage)
        }
    }

    //MARK: Fetch notes from firebase and keep listening
    private fun getNotesFromFirebase(){

        todos = ArrayList<TodoModel>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = TodoAdapter(todos)

        mDatabase = FirebaseDatabase.getInstance().reference.child(FirebaseTree.Notes.toString()).child(FirebaseAuth.getInstance().uid)
        listener = object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot?) {

                todos.removeAll(todos)
                var currentTodo: TodoModel

                p0?.children?.forEach { action: DataSnapshot ->
                    val title = action.child("title").value.toString()
                    val description = action.child("description").value.toString()
                    val uid = action.key.toString()

                    currentTodo = TodoModel()
                    currentTodo.uid = uid
                    currentTodo.todoNote = description
                    currentTodo.todoTitle = title

                    if (!todos.contains(currentTodo)) {
                        todos.add(currentTodo)
                    }
                }
                recyclerView.adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        }
        mDatabase.addValueEventListener(listener)
    }

    //MARK: Get current date in string
    private fun getCurrentDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.getTime()).toString();
    }

    //MARK: Get profile image url from SharePre...
    private fun getProfileImageUrlString(): String {
        val preference = getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
        val editor = preference.getString(USER_PROFILE_IMAGE_URL, "")
        return  editor
    }

    companion object {
        private val paramsKey   = "paramsKey"
        private val titleKey    = "title"
        private val noteKey     = "note"
        private val uidKey      = "uid"
        private val isEditKey   = "isEdit"
    }
}



