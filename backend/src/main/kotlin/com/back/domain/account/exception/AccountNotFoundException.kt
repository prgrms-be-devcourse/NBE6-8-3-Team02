package com.back.domain.account.exception

class AccountNotFoundException(
    message: String = "계좌를 찾을 수 없습니다."
) : RuntimeException(message)


