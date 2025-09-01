/*
package com.back.domain.account.entity;

import com.back.domain.account.dto.RqCreateAccountDto;
import com.back.domain.account.exception.AccountAccessDeniedException;
import com.back.domain.account.exception.AccountNumberUnchangedException;
import com.back.domain.member.entity.Member;
import com.back.domain.transactions.entity.TransactionType;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Account extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Member member;
    private String accountNumber;
    private Long balance;
    private String name;
    private boolean isDeleted; // 계좌 삭제 여부

    public Account(Member member, String accountNumber, Long balance, String name) {
        this.member = member;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.name = name;
        this.isDeleted = false; // 기본값은 false로 설정
    }

    public static Account create(RqCreateAccountDto rqCreateAccountDto,Member member){
        return Account.builder()
                .name(rqCreateAccountDto.getName())
                .accountNumber(rqCreateAccountDto.getAccountNumber())
                .balance(rqCreateAccountDto.getBalance())
                .member(member)
                .build();
    }

    public Account updateBalance(TransactionType  type, Long amount){
        if(type==TransactionType.ADD){
            this.balance = (this.balance + amount);
        }else if(type==TransactionType.REMOVE){
            if(getBalance()<amount){
                throw new IllegalArgumentException("잔액이 부족합니다.");
            }
            this.balance = this.getBalance() - amount;
        }
        return this;
    }

    public void validateOwner(Member member) {
        if(!this.member.equals(member)){
            throw new AccountAccessDeniedException();
        }
    }

    public void updateAccountNumber(String newAccountNumber) {
        if (this.accountNumber.equals(newAccountNumber)) {
            throw new AccountNumberUnchangedException();
        }
        this.accountNumber=newAccountNumber;
    }

    public void deleteAccount() {
        this.isDeleted = true; // 계좌 삭제 상태로 변경
    }
}
*/
