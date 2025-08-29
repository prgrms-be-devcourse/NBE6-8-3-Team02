package com.back.global.rsData

import com.fasterxml.jackson.annotation.JsonIgnore

data class RsData<T> (
    val resultCode: String,
    @JsonIgnore
    val statusCode: Int,
    val msg: String,
    val data: T? = null
) {
    constructor(resultCode: String, msg: String) : this(
        resultCode,
        resultCode.substringBefore("-").toInt(),
        msg,
        null
    )

    constructor(resultCode: String, msg: String, data: T?) : this(
        resultCode,
        resultCode.substringBefore("-").toInt(),
        msg,
        data
    )
}