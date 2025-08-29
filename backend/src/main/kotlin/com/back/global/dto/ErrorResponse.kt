package com.back.global.dto

import java.time.LocalDateTime

class ErrorResponse (
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: LocalDateTime
) {
    constructor(status: Int, error: String, message: String, path: String) : this(
        status,
        error,
        message,
        path,
        LocalDateTime.now()
    )

    override fun toString(): String {
        return "ErrorResponse{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", timestamp=" + timestamp +
                '}'
    }
}