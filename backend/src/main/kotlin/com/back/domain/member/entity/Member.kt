package com.back.domain.member.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Member(
    val email: String,
    val password: String,
    var name: String,
    var phoneNumber: String,
    @Enumerated(EnumType.STRING)
    val role: MemberRole= MemberRole.USER,
    val isActive: Boolean = true,
    val isDeleted: Boolean = false,
): BaseEntity(){

    fun updateName(name:String){
        this.name = name
    }

    fun updatePhoneNumber(phoneNumber: String){
        this.phoneNumber = phoneNumber
    }
}