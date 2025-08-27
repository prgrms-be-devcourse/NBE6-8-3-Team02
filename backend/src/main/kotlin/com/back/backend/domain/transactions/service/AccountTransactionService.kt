package com.back.backend.domain.transactions.service

import com.back.backend.domain.transactions.entity.AccountTransaction
import com.back.backend.domain.transactions.repository.AccountTransactionRepository
import org.springframework.stereotype.Service

@Service
class AccountTransactionService (
    private val accountTransactionRepository: AccountTransactionRepository
    //private val accountService: AccountTransactionService,
    // Account 도메인이 완료 되어야 함.
    // 추가로, 원래 프로젝트에선 Repository, Service 둘다 선언되어 있었으나,
    // SRP 원칙에 맞게 서비스만 사용하도록 변경.
) {
    /*
    @Transactional
    fun createAccountTransactions(dto: CreateAccTracRequestDto, member: Member): AccountTransaction {
        val account: Account = accountService.getAccount(dto.accountId, member)
        val accTrans: AccountTransaction = AccountTransaction(
            account = account,
            type = TransactionType.valueOf(dto.type),
            amount = dto.amount,
            content = dto.content,
            date = LocalDateTime.parse(dto.date)
        )
        accountTransactionRepository.save(accTrans)
        account.upDateBalance(TransactionType.valueOf(dto.type, dto.amount))
        return accTrans
    }
     */// Account, Member 도메인이 완성되어야 함.

    /*
    fun findByAccountId(accountId: Int, member: Member): List<AccountTransaction> {
        val account: Account = accountService.getAccount(dto.accountId, member).orElseThrow {
            IllegalArgumentException("존재하지 않는 계좌입니다.")
        }
        return accountTransactionRepository.findByAccountId(accountId)
    }
     */// Account 도메인이 완성되어야 함.

    fun count() : Long {
        return accountTransactionRepository.count()
    }

    fun flush() {
        accountTransactionRepository.flush()
    }

    fun findAll() : List<AccountTransaction> {
        return accountTransactionRepository.findAll()
    }

    /*
    fun findAccTransactionsByAccountIds(accountIds: List<Int>): Map<Int, List<AccountTransactionDto>> {
        val allTransactions: List<AccountTransaction> = accountTransactionRepository.findByAccountIdIn(accountIds)

        return allTransactions
            .groupBy { it.account.id }
            .mapValues { entry -> entry.value.map { AccountTransactionDto(it) } }
    }
     */// Account 도메인이 완성되어야 함.
}