package com.back.domain.account.controller

import com.back.domain.account.dto.AccountDtoKt
import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.entity.Account
import com.back.domain.account.service.AccountService
import com.back.domain.member.entity.Member
import com.back.global.security.CustomMemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account", description = "계좌 컨트롤러")
class ApiV1AccountController(
    private val accountService: AccountService
) {

    @PostMapping
    @Operation(summary = "계좌 등록", description = "새로운 계좌 등록")
    fun createAccount(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @RequestBody rqCreateAccountDto: RqCreateAccountDto
    ): ResponseEntity<AccountDtoKt> {
        val member: Member = userDetails.getMember()
        val account: Account = accountService.createAccount(rqCreateAccountDto, member)
        val accountDto = AccountDtoKt(account)
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto)
    }

    @GetMapping
    @Operation(summary = "계좌 다건 조회", description = "계좌 다건 조회")
    fun getAccounts(
        @AuthenticationPrincipal userDetails: CustomMemberDetails
    ): ResponseEntity<List<AccountDtoKt>> {
        val member: Member = userDetails.getMember()
        val accounts: List<Account> = accountService.getAccountsByMemberId(member)
        val accountDtos = accounts.map { AccountDtoKt(it) }
        return ResponseEntity.status(HttpStatus.OK).body(accountDtos)
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "계좌 단건 조회", description = "계좌 단건 조회")
    fun getAccount(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @PathVariable accountId: Int
    ): ResponseEntity<AccountDtoKt> {
        val member: Member = userDetails.getMember()
        val account: Account = accountService.getAccount(accountId, member)
        val accountDto = AccountDtoKt(account)
        return ResponseEntity.status(HttpStatus.OK).body(accountDto)
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "계좌 수정", description = "계좌 수정")
    fun updateAccount(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @PathVariable accountId: Int,
        @RequestBody rqUpdateAccountDto: RqUpdateAccountDto
    ): ResponseEntity<Void> {
        val member: Member = userDetails.getMember()
        accountService.updateAccount(accountId, member, rqUpdateAccountDto)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "계좌 삭제", description = "계좌 삭제")
    fun deleteAccount(
        @AuthenticationPrincipal userDetails: CustomMemberDetails,
        @PathVariable accountId: Int
    ): ResponseEntity<Void> {
        val member: Member = userDetails.getMember()
        accountService.deleteAccount(accountId, member)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}



