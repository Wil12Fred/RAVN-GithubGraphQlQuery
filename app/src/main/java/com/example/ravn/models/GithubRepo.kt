package com.example.ravn.models

class GithubRepo{
    var id : Int
    var name : String
    var descripcion: String
    var owner : String
    var stringId: String

    constructor(id:Int,stringId:String, name:String,descripcion:String,owner:String){
        this.id = id
        this.stringId = stringId
        this.name = name
        this.descripcion = descripcion
        this.owner = owner
    }
}