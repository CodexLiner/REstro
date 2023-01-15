package com.arunbamniya.restro

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType

class Constants {
      val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    companion object {
        @kotlin.jvm.JvmField
        var BASE_URL: String =  "http://10.0.2.2:8000/"
    }
}
