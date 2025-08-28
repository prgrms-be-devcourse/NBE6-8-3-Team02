package com.back.global.globalExceptionHandler


import com.back.global.dto.ErrorResponse
import com.back.global.rsData.RsData
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException

import com.back.domain.member.exception.DuplicateEmailException
import com.back.domain.member.exception.NotFoundMemberException
import com.back.domain.member.exception.PasswordMisMatchException

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    val log: Logger = LoggerFactory.getLogger(this::class.java)
    lateinit var objectMapper: ObjectMapper
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<RsData<Void?>> {
        return ResponseEntity(
            RsData(
                "404-1",
                e.message ?: "NOT FOUND"
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<RsData<Void?>> {
        return ResponseEntity(
            RsData(
                "400-1",
                e.message ?: "NOT FOUND"
            ),
            HttpStatus.NOT_FOUND //NOT FOUND는 404 아닌가? 일단 원래 코드대로 작성
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<RsData<Void?>> {
        val errorMessage = e.bindingResult
            .fieldErrors
            .firstOrNull()
            ?.defaultMessage
            ?: "입력 값이 올바르지 않습니다."

        log.error("유효성 검증 실패: {}", errorMessage)
        return ResponseEntity(
            RsData(
                "400-2",
                errorMessage
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<RsData<Void?>> {
        return ResponseEntity(
            RsData(
                "403-1",
                e.message ?: "FORBIDDEN"
            ),
            HttpStatus.FORBIDDEN
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<RsData<Void?>> {
        log.error("서버 내부 오류: {}", e.message, e)

        var message = "서버 내부 오류가 발생했습니다. 관리자에게 문의해주세요."
        if(log.isDebugEnabled) {
            message = e.message ?: message
        }

        return ResponseEntity(
            RsData(
                "500-1",
                message
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )

    }


    /*
    AuthenticationException
    AccountNumberUnchangedException
    AccountDuplicateException
    AccountAccessDeniedException
    Account 도메인에 구현된 위 4개의 커스텀 Exception은 추후 따로 작성.
     */

    /*
    AuthenticationException
    Auth 도메인에 구현된 위 1개의 커스텀 Exception은 추후 따로 작성.
     */

    @ExceptionHandler
    fun handleNotFoundMemberException(ex: NotFoundMemberException, request: WebRequest)
            : ResponseEntity<ErrorResponse> {
        return ErrorResponse.buildErrorResponse(ex, request, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleDuplicateEmailException(ex: DuplicateEmailException, request: WebRequest)
            : ResponseEntity<ErrorResponse> {
        return ErrorResponse.buildErrorResponse(ex, request, HttpStatus.CONFLICT)
    }

    @ExceptionHandler
    fun handlePasswordMisMatchException(ex: PasswordMisMatchException, request: WebRequest)
            : ResponseEntity<ErrorResponse> {
        return ErrorResponse.buildErrorResponse(ex, request, HttpStatus.UNAUTHORIZED)
    }

}