package com.back.domain.transactions.service

import com.back.domain.account.service.AccountService
import com.back.domain.member.entity.Member
import com.back.domain.transactions.dto.AccountTransactionDto
import com.back.domain.transactions.dto.CreateAccTracRequestDto
import com.back.domain.transactions.entity.AccountTransaction
import com.back.domain.transactions.entity.TransactionType
import com.back.domain.transactions.repository.AccountTransactionRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccountTransactionService (
    private val accountTransactionRepository: AccountTransactionRepository,
    private val accountService: AccountService
) {
    @Transactional
    fun createAccountTransactions(dto: CreateAccTracRequestDto, member: Member): AccountTransaction {
        val account = accountService.getAccount(dto.accountId, member)
        val accTrans = AccountTransaction(
            account = account,
            type = TransactionType.valueOf(dto.type),
            amount = dto.amount,
            content = dto.content,
            date = LocalDateTime.parse(dto.date)
        )
        accountTransactionRepository.save(accTrans)
        account.updateBalance(TransactionType.valueOf(dto.type), dto.amount)
        return accTrans
    }

    fun findByAccountId(accountId: Int, member: Member): List<AccountTransaction> {
        accountService.getAccount(accountId, member)
        return accountTransactionRepository.findByAccountId(accountId)
    }

    //테스트용
    fun findById(id: Int): AccountTransaction {
        return accountTransactionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 거래입니다.") }
    }

    fun count() : Long {
        return accountTransactionRepository.count()
    }

    fun flush() {
        accountTransactionRepository.flush()
    }

    fun findAll() : List<AccountTransaction> {
        return accountTransactionRepository.findAll()
    }

    fun deleteById(id: Int) : AccountTransaction {
        val accountTransaction = accountTransactionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("존재하지 않는 거래입니다.") }
        accountTransactionRepository.deleteById(id)
        return accountTransaction
    }

    fun findAccTransactionsByAccountIds(accountIds: List<Int>): Map<Int, List<AccountTransactionDto>> {
        val allTransactions: List<AccountTransaction> = accountTransactionRepository.findByAccountIdIn(accountIds)

        return allTransactions
            .groupBy { it.account.id }
            .mapValues { entry -> entry.value.map { AccountTransactionDto(it) } }
    }
}