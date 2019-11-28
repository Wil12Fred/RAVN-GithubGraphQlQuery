package com.example.ravn.models

class GithubUser{
    var id : Int
    var group : String
    var names : String
    var perc : Int
    var username: String
    var state = true

    constructor(id:Int,group:String,names:String,perc:Int, username:String){
        this.id = id
        this.group = group
        this.names = names
        this.perc = perc
        this.username = username
    }
}