package com.phanith.todo.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.widget.Toast
import com.phanith.todo.Adapter.TodoAdapter
import com.phanith.todo.Datasource.Datasource
import com.phanith.todo.Model.TodoModel
import com.phanith.todo.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profileImage = findViewById<CircleImageView>(R.id.profileImage)
        profileImage.setOnClickListener {
            val profileActivity = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(profileActivity)
        }

        val profileImageUrl = getProfileImageUrlString()
        if (profileImageUrl != "" ) {
            Picasso.with(this).load(profileImageUrl).into(profileImage)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val todos = ArrayList<TodoModel>()

        todos.add(TodoModel("haha", "sjflsdjfljjdsfklfjsdlfjfsdfds fsdlfjsdljflsdkf fjdlsjfsdljfsdf fjdlsjflsdjfl sfjdlsjfsld sjflsdjfljjdsfklfjsdlfjfsdfds fsdlfjsdljflsdkf fjdlsjfsdljfsdf fjdlsjflsdjfl sfjdlsjfsld sjflsdjfljjdsfklfjsdlfjfsdfds fsdlfjsdljflsdkf fjdlsjfsdljfsdf fjdlsjflsdjfl sfjdlsjfsld sfj slfjs"))
        todos.add(TodoModel("haha", "hoso"))
        todos.add(TodoModel("haha", "hoso"))
        todos.add(TodoModel("haha", "hoso"))
        todos.add(TodoModel("haha", "hoso"))
        todos.add(TodoModel("haha", "hoso"))

        recyclerView.adapter = TodoAdapter(todos)

    }
}

private fun MainActivity.getProfileImageUrlString(): String {

    val preference = getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
    val editor = preference.getString(USER_PROFILE_IMAGE_URL, "")

    return  editor
}
