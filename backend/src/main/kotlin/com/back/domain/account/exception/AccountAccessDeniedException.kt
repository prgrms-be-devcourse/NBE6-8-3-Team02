package com.back.domain.account.exception

class AccountAccessDeniedException(
    message: String = "계좌 소유자가 아닙니다."
) : RuntimeException(message)


