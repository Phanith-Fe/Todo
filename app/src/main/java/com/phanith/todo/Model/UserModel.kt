package com.phanith.todo.Model

import org.json.JSONObject

class User {

     var name: String = ""
     var email: String = ""
     var active: String = ""
     var language: String = ""
     var profileImageUrl: String = ""
     var uid: String = ""

     constructor(name: String,
                 email: String,
                 active: String,
                 language: String,
                 profileImageUrl: String,
                 uid: String){

          this.name = name
          this.email = email
          this.active = active
          this.language = language
          this.profileImageUrl = profileImageUrl
          this.uid = uid
     }

     constructor()
}

enum class FirebaseTree {
     Users,
     Notes;
}

enum class KeyID {
     Username,
     Email,
     Active,
     ProfileImageUrl,
     Language,
     Uid;
}


