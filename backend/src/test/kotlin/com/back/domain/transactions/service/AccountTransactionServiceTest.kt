package com.back.domain.transactions.service

import com.back.domain.transactions.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTransactionServiceTest (
    @Autowired private val transactionRepository: TransactionRepository,
    @Autowired private val transactionService: TransactionService,
) {
    //Account 도메인이 완성되어야 추가 가능.
}