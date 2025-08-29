package com.back.global.dto

import java.time.LocalDateTime

class ErrorResponse (
    private var status: Int,
    private var error: String,
    private var message: String,
    private var path: String,
    private var timestamp: LocalDateTime
) {
    init {
        this.status = status
        this.error = error
        this.message = message
        this.path = path
        this.timestamp = timestamp
    }

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