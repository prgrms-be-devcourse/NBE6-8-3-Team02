package com.back.domain.account.exception

class AccountNumberUnchangedException(
    message: String = "계좌번호가 이전과 동일합니다."
) : RuntimeException(message)


