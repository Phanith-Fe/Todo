package com.phanith.todo.Model

import android.os.Parcel
import android.os.Parcelable

class TodoModel {

    var todoTitle: String = ""
    var todoNote: String = ""
    var uid: String = ""

    constructor()

    constructor(title: String, subtitle: String, uid: String){
        this.todoTitle = title
        this.todoNote = subtitle
        this.uid = uid
    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(todoTitle)
//        parcel.writeString(todoNote)
//        parcel.writeString(uid)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<TodoModel> {
//        override fun createFromParcel(parcel: Parcel): TodoModel {
//            return TodoModel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<TodoModel?> {
//            return arrayOfNulls(size)
//        }
//    }
}
