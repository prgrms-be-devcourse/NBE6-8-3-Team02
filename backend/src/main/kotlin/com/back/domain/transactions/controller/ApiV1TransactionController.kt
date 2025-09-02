package com.back.domain.transactions.controller

import com.back.domain.transactions.dto.CreateTransactionRequestDto
import com.back.domain.transactions.dto.TransactionDto
import com.back.domain.transactions.dto.UpdateTransactionRequestDto
import com.back.domain.transactions.service.TransactionService
import com.back.global.rsData.RsData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/transactions/asset")
@Tag(name = "ApiV1TransactionController", description = "자산 거래 컨트롤러")
class ApiV1TransactionController (
    private val transactionService: TransactionService,
) {
    //기존 프로젝트에선 return으로 RsData를 사용했으나.
    //ResponseEntity<RsData> 형태로 return값 변경.
    //추후 테스트 시 프론트엔드에서 코드 변경 필요.
    @PostMapping
    @Operation(summary = "거래 등록")
    fun createTransaction(
        @RequestBody dto: CreateTransactionRequestDto
    ): ResponseEntity<RsData<TransactionDto>> {
        val transaction = transactionService.createTransaction(dto)
        val transactionDto = TransactionDto(transaction)

        return ResponseEntity(
            RsData(
                "200-1",
                "거래가 등록되었습니다.",
                transactionDto
            ),
            HttpStatus.OK
        )
    }

    @GetMapping
    @Operation(summary = "거래 목록 조회")
    fun getTransaction() : ResponseEntity<RsData<List<TransactionDto>>> {
        val transactions = transactionService.findAll()
        val transactionDtos = transactions.map { TransactionDto(it) }
        return ResponseEntity(
            RsData(
                "200-1",
                "거래 목록을 조회했습니다.",
                transactionDtos
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}")
    @Operation(summary = "거래 단건 조회")
    fun getTransaction(
        @PathVariable id: Int
    ) : ResponseEntity<RsData<TransactionDto>> {
        val transaction = transactionService.findById(id)
        val transactionDto = TransactionDto(transaction)
        return ResponseEntity(
            RsData(
                "200-1",
                "${id}번 거래를 조회 했습니다.",
                transactionDto
            ),
            HttpStatus.OK
        )
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "거래 삭제")
    fun deleteTransaction(
        @PathVariable id: Int
    ) : ResponseEntity<RsData<TransactionDto>> {
        val transaction = transactionService.deleteById(id)
        val transactionDto = TransactionDto(transaction)
        return ResponseEntity(
            RsData(
                "200-1",
                "${id}번 거래를 삭제 했습니다.",
                transactionDto
            ),
            HttpStatus.OK
        )
    }

    @PutMapping("/{id}")
    @Operation(summary = "거래 수정")
    fun updateTransaction(
        @RequestBody dto: UpdateTransactionRequestDto
    ) : ResponseEntity<RsData<TransactionDto>> {
        val transaction = transactionService.updateById(dto)
        val transactionDto = TransactionDto(transaction)
        return ResponseEntity(
            RsData(
                "200-1",
                "${transactionDto.id}번 거래를 수정 했습니다.",
                transactionDto
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/search/{assetId}")
    fun getTransactionsByAsset(
        @PathVariable assetId: Int
    ) : ResponseEntity<RsData<List<TransactionDto>>> {
        val transactions = transactionService.findByAssetId(assetId)
        val transactionDtos = transactions.map { TransactionDto(it) }
        return ResponseEntity(
            RsData(
                "200-1",
                "${assetId}번 자산의 거래 목록을 조회 했습니다.",
                transactionDtos
            ),
            HttpStatus.OK
        )
    }

    @GetMapping("/search/bulk")
    @Operation(summary = "자산 거래 목록 일괄 조회")
    fun getTransactionsBulk(
        @RequestParam ids : List<Int>
    ) : ResponseEntity<RsData<Map<Int, List<TransactionDto>>>> {
        val result = transactionService.findTransactionsByAssetId(ids)
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