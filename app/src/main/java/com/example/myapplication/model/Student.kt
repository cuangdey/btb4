package com.example.myapplication.model

import java.io.Serializable

data class Student(
    val id: Long = -1L,
    val studentCode: String,
    val name: String,
    val email: String,
    val avatar: String
) : Serializable
