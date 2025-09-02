package com.back.domain.member.dto

import java.time.LocalDateTime

data class AdminMemberResponse(
    val memberId:Int,
    val maskedEmail:String,
    val maskedName:String,
    val maskedPhone:String,
    val status:String,
    val createDate:LocalDateTime,
    val modifyDate: LocalDateTime
){
    companion object {
        fun String.maskEmail(): String {
            val atIndex = this.indexOf('@')
            if (atIndex <= 1) return "***${this.substring(atIndex)}"

            return "${this.first()}***${this.substring(atIndex)}"
        }

        fun String.maskName(): String {
            if (this.length <= 2) return "**"
            return this.first() + "*".repeat(this.length - 1)
        }

        //11자리 휴대 전화번호만 취급
        fun String.maskPhone(): String {
            val first = this.substring(0, 3)
            val last = this.substring(9)
            return "$first-****-$last"
        }
    }
}
