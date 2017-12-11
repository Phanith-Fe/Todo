package com.phanith.todo.Model

class TodoModel {

    var todoTitle: String = ""
    var todoNote: String = ""

    constructor()

    constructor(title: String, subtitle: String){
        this.todoTitle = title
        this.todoNote = subtitle
    }
}
