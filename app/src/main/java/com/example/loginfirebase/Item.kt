package com.example.loginfirebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Item(val title: String? = "", val url: String? = "")
