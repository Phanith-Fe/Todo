package com.phanith.todo.Adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phanith.todo.Controller.TodoActivity
import com.phanith.todo.Model.TodoModel
import com.phanith.todo.R

class TodoAdapter(val todoList: ArrayList<TodoModel>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class KK(val todoList: ArrayList<TodoModel>)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.activity_todo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val todo: TodoModel = todoList[position]
        holder?.textViewTitle?.text = todo.todoTitle
        holder?.textViewSubtitle?.text = todo.todoNote
        holder?.bindObject(todoList[position].uid)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private  var uid = ""

        val textViewTitle = itemView.findViewById<TextView>(R.id.todo_title)
        val textViewSubtitle = itemView.findViewById<TextView>(R.id.todo_note)

        init {
            this.itemView.setOnClickListener(this)
        }

        fun bindObject(uid: String) {
            this.uid = uid
        }

        override fun onClick(p0: View?) {
            val intent = Intent(itemView.context, TodoActivity::class.java)
            val params = Bundle()
            params.putString(title, textViewTitle.text.toString())
            params.putString(note, textViewSubtitle.text.toString())
            params.putString(uidKey, uid)
            params.putString(isEdit, "yes")
            intent.putExtra(paramsKey, params)
            itemView.context.startActivity(intent)

        }

        companion object {
            private val paramsKey = "paramsKey"
            private val title   = "title"
            private val note    = "note"
            private val uidKey  = "uid"
            private val isEdit  = "isEdit"
        }
    }


}