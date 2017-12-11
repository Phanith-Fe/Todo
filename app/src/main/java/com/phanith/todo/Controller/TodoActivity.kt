package com.phanith.todo.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.phanith.todo.R
import org.w3c.dom.Text

class TodoActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo2)

        instanceData()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}

private fun TodoActivity.instanceData() {
    var params = Bundle()
    val intent = getIntent()
    params = intent.getBundleExtra("kk")
    val title = findViewById<TextView>(R.id.title_todo)
    title.text = params.get("title").toString()

    val description = findViewById<TextView>(R.id.note_todo)
    description.text = params.get("note").toString()
}
