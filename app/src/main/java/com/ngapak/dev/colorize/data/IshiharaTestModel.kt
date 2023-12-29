package com.ngapak.dev.colorize.data


data class IshiharaTestModel(
    val id: Int,
    val imgUrl: String,
    val answerTrue: String, // True
    val answerFalse: String? = null, // False
    val unreadable: String? = "Can't Read", // Unreadable / X
    val otherNumber: String? = "Other Number", // Other Number
)
