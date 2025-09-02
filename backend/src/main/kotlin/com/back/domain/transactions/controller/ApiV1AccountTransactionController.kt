package com.back.domain.transactions.controller

import com.back.domain.transactions.dto.AccountTransactionDto
import com.back.domain.transactions.dto.CreateAccTracRequestDto
import com.back.domain.transactions.service.AccountTransactionService
import com.back.global.rsData.RsData
import com.back.global.security.CustomMemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/transactions/account")
@Tag(name = "ApiV1AccountTransactionController", description = "계좌 거래 컨트롤러")
class ApiV1AccountTransactionController (
    @Autowired private val accountTransactionService: AccountTransactionService,
) {
    @PostMapping
    @Operation(summary = "거래 등록")
    fun createTransaction(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @RequestBody dto: CreateAccTracRequestDto
    ): ResponseEntity<RsData<AccountTransactionDto>> {
        val member = memberDetails.getMember()
        val accountTransaction = accountTransactionService.createAccountTransactions(dto, member)
        return ResponseEntity(
            RsData(
                "200-1",
                "거래가 등록되었습니다",
                AccountTransactionDto(accountTransaction)
            ),
            HttpStatus.OK
        )
    }

    @GetMapping
    @Operation(summary = "거래 목록 조회")
    fun getTransactions(): ResponseEntity<RsData<List<AccountTransactionDto>>> {
        val accountTransactions = accountTransactionService.findAll()
        val accountTransactionDtos = accountTransactions.map { AccountTransactionDto(it) }
        return ResponseEntity(
            RsData(
                "200-1",
                "거래 목록을 조회했습니다.",
                accountTransactionDtos
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}")
    @Operation(summary = "거래 단건 조회")
    fun getAccountTransaction(
        @PathVariable id: Int
    ): ResponseEntity<RsData<AccountTransactionDto>> {
        val accountTransaction = accountTransactionService.findById(id)
        val accountTransactionDto = AccountTransactionDto(accountTransaction)
        return ResponseEntity(
            RsData(
                "200-1",
                "${id}번 거래를 조회 했습니다.",
                accountTransactionDto
            ),
            HttpStatus.OK
        )
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "거래 삭제")
    fun deleteTransaction(
        @PathVariable id: Int
    ): ResponseEntity<RsData<AccountTransactionDto>> {
        val accountTransaction = accountTransactionService.deleteById(id)
        val accountTransactionDto = AccountTransactionDto(accountTransaction)
        return ResponseEntity(
            RsData(
                "200-1",
                "${id}번 거래를 삭제 했습니다.",
                accountTransactionDto
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/search/{accountId}")
    @Operation(summary = "특정 계좌의 거래 목록 조회")
    fun getTransactionsByAccountId(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @PathVariable accountId: Int
    ): ResponseEntity<RsData<List<AccountTransactionDto>>> {
        val member = memberDetails.getMember()
        val accountTransactions = accountTransactionService.findByAccountId(accountId, member)
        val accountTransactionDtos = accountTransactions.map { AccountTransactionDto(it) }
        return ResponseEntity(
            RsData(
                "200-1",
                "${accountId}번 계좌의 거래 목록을 조회 했습니다.",
                accountTransactionDtos
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/search/bulk")
    @Operation(summary = "계좌 거래 목록 일괄 조회")
    fun getAccTransactionsBulk(
        @RequestParam ids: List<Int>
    ): ResponseEntity<RsData<Map<Int, List<AccountTransactionDto>>>> {
        val result = accountTransactionService.findAccTransactionsByAccountIds(ids)
        return ResponseEntity(
            RsData(
                "200-1",
                "자산 거래를 일괄 조회 했습니다.",
                result
            ),
            HttpStatus.OK
        )
    }
}