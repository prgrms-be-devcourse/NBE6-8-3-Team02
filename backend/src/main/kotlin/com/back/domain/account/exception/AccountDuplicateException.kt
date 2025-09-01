package com.back.domain.account.exception

class AccountDuplicateException(
    message: String = "이미 존재하는 계좌입니다."
) : RuntimeException(message)


