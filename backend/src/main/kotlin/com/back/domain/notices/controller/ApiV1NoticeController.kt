package com.back.domain.notices.controller

import com.back.domain.notices.dto.CreateNoticeRequestDto
import com.back.domain.notices.dto.DeleteNoticeRequestDto
import com.back.domain.notices.dto.NoticeResponseDto
import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.back.domain.notices.service.NoticeService
import com.back.global.rsData.RsData
import com.back.global.security.CustomMemberDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notices")
@Tag(name = "ApiV1NoticeController", description = "공지사항 컨트롤러")
class ApiV1NoticeController (
    private val noticeService: NoticeService,
) {
   @GetMapping
   @Operation(summary = "공지사항 전체 조회 (검색 기능 + 페이징 포함")
   fun getAllNotices(
       @RequestParam(required = false, defaultValue = "" ) search: String,
       @RequestParam(required = false, defaultValue = "0") page: Int,
       @RequestParam(required = false, defaultValue = "10") pageSize: Int,
   ): ResponseEntity<RsData<Page<NoticeResponseDto>>> {
       val pageable = PageRequest.of(page, pageSize)
       val notices = noticeService.getAllNotices(search, pageable)
       return ResponseEntity(
           RsData(
               "200-1",
               "공지사항 목록을 조회했습니다.",
               notices
           ),
           HttpStatus.OK
       )
   }

    @GetMapping("{id}")
    @Operation(summary = "공지사항 단건 조회")
    fun getNotice(
        @PathVariable id: Int
    ) : ResponseEntity<RsData<NoticeResponseDto>> {
        val notice = noticeService.getNoticeById(id)
        return ResponseEntity(
            RsData(
                "200-1",
                "공지사항을 조회했습니다.",
                notice
            ),
            HttpStatus.OK
        )
    }

    @PostMapping
    @Operation(summary = "공지사항 생성")
    fun createNotice(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @Valid @RequestBody dto: CreateNoticeRequestDto
    ) : ResponseEntity<RsData<NoticeResponseDto>> {
        val notice = noticeService.createNotice(dto, memberDetails.getMember())
        return ResponseEntity(
            RsData(
                "200-1",
                "공지사항이 생성되었습니다.",
                notice
            ),
            HttpStatus.OK
        )
    }

    @PutMapping("/{id}")
    @Operation(summary = "공지사항 수정")
    fun updateNotice(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @PathVariable id: Int,
        @Valid @RequestBody dto: UpdateNoticeRequestDto
    ) : ResponseEntity<RsData<NoticeResponseDto>> {
        val notice = noticeService.updateNotice(id, dto, memberDetails.getMember())
        return ResponseEntity(
            RsData(
                "200-1",
                "공지사항이 수정되었습니다.",
                notice
            ),
            HttpStatus.OK
        )
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "공지사항 삭제")
    fun deleteNotice(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @PathVariable id: Int,
    ) : ResponseEntity<RsData<NoticeResponseDto>> {
        val notice = noticeService.deleteNotice(DeleteNoticeRequestDto(id), memberDetails.getMember())
        val noticeDto = NoticeResponseDto(notice)
        return ResponseEntity(
            RsData(
                "200-1",
                "공지사항이 삭제되었습니다.",
                noticeDto
            ),
            HttpStatus.OK
        )
    }
}