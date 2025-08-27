package com.back.domain.member.controller

import com.back.domain.member.dto.MemberSignUpRequest
import com.back.domain.member.dto.MemberSignUpResponse
import com.back.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class ApiV1MemberController(private val memberService: MemberService){

    @PostMapping("/signup")
    fun signUp(@RequestBody request: MemberSignUpRequest): ResponseEntity<MemberSignUpResponse> {
        val response = memberService.join(request)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}