package com.example.whatsappclone.models

import android.widget.ImageView
import java.io.Serializable
import java.util.Date


data class ContactData(

    val name:String,
    val message:String,
    val image:String,
    val date:Date

):Serializable

