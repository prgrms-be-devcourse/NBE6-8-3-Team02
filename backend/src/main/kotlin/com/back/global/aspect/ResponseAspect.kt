package com.back.global.aspect

import jakarta.servlet.http.HttpServletResponse
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class ResponseAspect {
    private lateinit var response: HttpServletResponse

    fun ResponseAspect(response: HttpServletResponse) {
        this.response = response
    }

    @Around("""
                execution(public com.back.global.rsData.RsData *(..)) &&
                (
                    within(@org.springframework.stereotype.Controller *) ||
                    within(@org.springframework.web.bind.annotation.RestController *)
                ) &&
                (
                    @annotation(org.springframework.web.bind.annotation.GetMapping) ||
                    @annotation(org.springframework.web.bind.annotation.PostMapping) ||
                    @annotation(org.springframework.web.bind.annotation.PutMapping) ||
                    @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
                    @annotation(org.springframework.web.bind.annotation.RequestMapping)
                )
            """)
    @Throws(Throwable::class) // throw 대체
    fun handleResponse(joinPoint: ProceedingJoinPoint): Any?{ // kotlin에선 Object 대신 Any? 사용.
        val proceed = joinPoint.proceed()
        /*
        val rsData = proceed as? RsData<*> ?: return proceed
        response.status = rsData.statusCode()
         *///RsData 구현 필요.
        return proceed
    }
}