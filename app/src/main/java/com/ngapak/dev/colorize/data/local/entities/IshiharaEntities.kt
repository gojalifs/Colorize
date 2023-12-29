package com.ngapak.dev.colorize.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("ishihara")
data class IshiharaEntities(
    @PrimaryKey
    val id: Int,
    val imgUrl: String,
    val imgPath: String? = null,
    val answerTrue: String,
    val answerFalse: String,
    val protan: String? = null,
    val deutan: String? = null,
    val otherNumber: String? = "Other Number",
    val unreadable: String? = "Unreadable",
)
