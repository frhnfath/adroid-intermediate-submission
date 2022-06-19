package com.frhnfath.storyappfix.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var token: String? = null,
    var isLogin: Boolean = false
) : Parcelable