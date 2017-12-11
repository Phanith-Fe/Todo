package com.phanith.todo.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phanith.todo.Controller.TodoActivity
import com.phanith.todo.Datasource.Datasource
import com.phanith.todo.Model.TodoModel
import com.phanith.todo.R

class TodoAdapter(val todoList: ArrayList<TodoModel>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.activity_todo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val todo: TodoModel = todoList[position]
        holder?.textViewTitle?.text = todo.todoTitle
        holder?.textViewSubtitle?.text = todo.todoNote
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val textViewTitle = itemView.findViewById<TextView>(R.id.todo_title)
        val textViewSubtitle = itemView.findViewById<TextView>(R.id.todo_note)

        init {
            this.itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val intent = Intent(itemView.context, TodoActivity::class.java)
            val params = Bundle()
            params.putString("title", textViewTitle.text.toString())
            params.putString("note", textViewSubtitle.text.toString())
            intent.putExtra("kk", params)
            itemView.context.startActivity(intent)
        }
    }


}