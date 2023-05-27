package com.example.myapplication.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cast(
    @Json(name = "profile_path")
    val profilePath: String?,
) : Parcelable