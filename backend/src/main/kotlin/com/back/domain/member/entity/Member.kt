package com.back.domain.member.entity

import com.back.domain.member.exception.PasswordMisMatchException
import com.back.domain.member.exception.UnchangedMemberDetailsException
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.hibernate.annotations.DynamicUpdate
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@DynamicUpdate
class Member(
    val email: String,
    var password: String,
    var name: String,
    var phoneNumber: String,
    @Enumerated(EnumType.STRING)
    var role: MemberRole= MemberRole.USER,
    var isActive: Boolean = true,
    var isDeleted: Boolean = false,
): BaseEntity(){

    fun updateName(newName:String){
        if (this.name == newName) {
            throw UnchangedMemberDetailsException("같은 이름으로 수정할 수 없습니다.")
        }
        this.name = newName
    }

    fun updatePhoneNumber(newPhoneNumber: String){
        if(this.phoneNumber == newPhoneNumber){
            throw UnchangedMemberDetailsException("같은 전화번호로 수정할 수 없습니다.")
        }
        this.phoneNumber = newPhoneNumber
    }

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun softDelete(){
        this.isDeleted = true
    }


    fun activate(){
        this.isActive = true
    }

    fun deactivate(){
        this.isActive = false

    }
}