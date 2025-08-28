package com.back.global.dto

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime


data class ErrorResponse(
    val status : Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: LocalDateTime,
){
    companion object {
        private val logger = KotlinLogging.logger {}

        fun buildErrorResponse(exception: Exception, request: WebRequest, status: HttpStatus):
                ResponseEntity<ErrorResponse> {
            val errorResponse = ErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                message = exception.message ?: "Unexpected error",
                path = request.getDescription(false).replace(
                    "uri=", ""
                ),
                timestamp = LocalDateTime.now()
            )
            logger.error{ errorResponse }

            return ResponseEntity(errorResponse, status)
        }
    }
}