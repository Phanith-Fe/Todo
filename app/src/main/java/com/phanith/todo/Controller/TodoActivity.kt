package com.phanith.todo.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.phanith.todo.Model.FirebaseTree
import com.phanith.todo.Model.KeyID
import com.phanith.todo.Model.TodoModel
import com.phanith.todo.Model.User
import com.phanith.todo.R
import org.w3c.dom.Text

class TodoActivity : AppCompatActivity() {

    lateinit var description: TextView
    lateinit var title: TextView
    lateinit var mDatabase: DatabaseReference
    lateinit var currentNote: TodoModel
    lateinit var listener: ValueEventListener
    lateinit var uid: String
    lateinit var bottomButton: Button
    lateinit var isEdit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo2)


        isEdit = "no"
        mDatabase = FirebaseDatabase.getInstance().reference
        instanceData()
        instanceCurrentNote()
        setupBottomButton()
    }

    //MARK: Get data from intent, possible current data or dummy null data
    private fun instanceData() {

        description = findViewById<EditText>(R.id.note_todo)
        title = findViewById<EditText>(R.id.title_todo)

        var params = Bundle()
        val intent = getIntent()
        params = intent.getBundleExtra(paramsKey)
        title.text = params.get(titleKey).toString()
        description.text = params.get(noteKey).toString()
        uid = params.get(uidKey).toString()
        isEdit = params.get(isEditKey).toString()
    }

    //MARK: Initialize current note
    private fun instanceCurrentNote() {
        currentNote = TodoModel()
    }

    //MARK: Setup Save || Delete button
    private fun setupBottomButton(){
        bottomButton = findViewById<Button>(R.id.delete_button)

        if (isEdit == "no") {
            bottomButton.setText("Save")
            bottomButton.setBackgroundColor(Color.rgb(52, 152, 219))
        }else{
            bottomButton.setText("Delete")
            bottomButton.setBackgroundColor(Color.rgb(231, 76, 60))
        }

        bottomButton.setOnClickListener{

            if (title.text.toString() != "" && uid == "null" && isEdit == "no") {
                val childUpdates: HashMap<String, Any> = HashMap()
                childUpdates.put("title", title.text.toString())
                childUpdates.put("description", description.text.toString())
                mDatabase.child(FirebaseTree.Notes.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).push().updateChildren(childUpdates)
                super.onBackPressed()
            }else if (title.text.toString() != "" && isEdit == "yes" && uid != "null") {
                mDatabase.child(FirebaseTree.Notes.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).child(uid).removeValue()
                super.onBackPressed()
            }
            else{
                Toast.makeText(this, "Please input title and try again.", Toast.LENGTH_LONG).show()
            }

        }
    }

    //MARK: Handle onBackPressed
    override fun onBackPressed() {
        pushUserData(title.text.toString(), description.text.toString())
    }

    //MARK: Some logic to handle when onBackPressed fire
    private fun pushUserData(title: String, description: String) {
        if (title != "" && uid != "null") {
            val childUpdates: HashMap<String, Any> = HashMap()
            childUpdates.put("title", title)
            childUpdates.put("description", description)
            mDatabase.child(FirebaseTree.Notes.toString()).child(FirebaseAuth.getInstance().currentUser!!.uid).child(uid).updateChildren(childUpdates)
            super.onBackPressed()
        }else{
            super.onBackPressed()
        }
    }

    //MARK: Best practice var for easy access intent keyID
    companion object {
        private val paramsKey   = "paramsKey"
        private val titleKey    = "title"
        private val noteKey     = "note"
        private val uidKey      = "uid"
        private val isEditKey   = "isEdit"
    }
}


