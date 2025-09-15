package com.example.lab1.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userName: String,
    val email: String,
    val password: String,
    val sex: Boolean,
) : Parcelable
