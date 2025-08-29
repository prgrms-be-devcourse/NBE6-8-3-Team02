package com.back.domain.member.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.annotations.DynamicUpdate

@Entity
@DynamicUpdate
class Member(
    val email: String,
    val password: String,
    var name: String,
    var phoneNumber: String,
    @Enumerated(EnumType.STRING)
    var role: MemberRole= MemberRole.USER,
    var isActive: Boolean = true,
    var isDeleted: Boolean = false,
): BaseEntity(){

    fun updateName(name:String){
        this.name = name
    }

    fun updatePhoneNumber(phoneNumber: String){
        this.phoneNumber = phoneNumber
    }

    fun softDelete(){
        this.isDeleted = true
    }
}