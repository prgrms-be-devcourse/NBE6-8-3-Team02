package com.back.global.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    //Account 도메인 구현 필요.
    //@Pointcut("execution(* com.back.domain.account.controller..*(..))")
    fun accountControllerMethods() {}

    //위의 @PointCut 어노테이션이 먼저 해결 되어야 함.
    //@Before("accountControllerMethods()")
    fun logBefore(joinPoint: JoinPoint) {
        println("=== AOP 작동됨 ===")

        val methodName = joinPoint.signature.toShortString()
        val logMessage = StringBuilder("\n요청: $methodName")

        val args = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val parameters = methodSignature.method.parameters

        /*
        parameters.zip(args).forEach { (parameter, arg) ->
            parameter.annotations.forEach { annotation ->
                if(annotation.annotationClass == AuthenticatedPrincipal::class && arg is CustomUserDetails) {
                    logMessage.append("\n사용자: ${arg.username}")
                }
            }
        }
         */// CustomUserDetails, Member 구현 필요.
        println(logMessage.toString())
    }
}