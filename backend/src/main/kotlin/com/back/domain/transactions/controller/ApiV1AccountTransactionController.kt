package com.back.domain.transactions.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactions/account")
@Tag(name = "NoticeController", description = "계좌 거래 컨트롤러")
class ApiV1AccountTransactionController {
}