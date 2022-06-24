package com.frhnfath.storyappfix.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryModel(
    var name: String? = null,
    var image: String? = null,
    var description: String? = null,
    var createAt: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
) : Parcelable
