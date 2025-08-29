package com.back.global.security

import com.back.global.security.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val memberDetailService: CustomMemberDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getTokenFromCookie(request)

        if(token != null && jwtUtil.validateToken(token)) {
            val email = jwtUtil.getEmailFromToken(token)

            val memberDetails = memberDetailService.loadUserByUsername(email)

            val authenticationToken= UsernamePasswordAuthenticationToken(
                memberDetails,
                null,
                memberDetails.authorities
            )

            SecurityContextHolder.getContext().authentication = authenticationToken
        }

        filterChain.doFilter(request,response)
    }

    fun getTokenFromCookie(request:HttpServletRequest): String? {
        return request.cookies?.let{
            cookies->
            cookies.firstOrNull() { it.name == "accessToken" }?.value
        }
    }
}