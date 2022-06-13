package com.example.capstonedermapps.model

data class Users (
    val id: String?,
    val name: String?,
    val age: String?,
    val gender : String?,
    val email: String)
{
    constructor():this("","","","","")
}
