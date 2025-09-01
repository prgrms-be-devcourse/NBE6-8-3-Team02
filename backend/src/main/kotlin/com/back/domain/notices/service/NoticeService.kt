package com.back.domain.notices.service

import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import com.back.domain.notices.dto.CreateNoticeRequestDto
import com.back.domain.notices.dto.DeleteNoticeRequestDto
import com.back.domain.notices.dto.NoticeResponseDto
import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.back.domain.notices.entity.Notice
import com.back.domain.notices.repository.NoticeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NoticeService (
    private val noticeRepository: NoticeRepository
) {
    fun createNotice(dto: CreateNoticeRequestDto, member: Member) : NoticeResponseDto {
        require(member.role == MemberRole.ADMIN) { "관리자만 공지사항을 작성할 수 있습니다." }

        val notice = Notice(
            member,
            dto.title,
            dto.content,
            0,
            dto.fileUrl,
        )
        val savedNotice: Notice = noticeRepository.save(notice)
        return NoticeResponseDto.from(savedNotice)
    }

    //Kotlin 스타일로, if 문 수정.
    fun getAllNotices(search: String, pageable: Pageable): Page<NoticeResponseDto> {
        val notices = search.takeIf { it.isNotBlank() }
            ?.let { noticeRepository.findByTitleContainingIgnoreCase(search, pageable) }
            ?: noticeRepository.findAll(pageable)

        return notices.map { notice -> NoticeResponseDto.from(notice) }
    }

    fun getNoticeById(id: Int): NoticeResponseDto {
        val notice: Notice = noticeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("공지사항을 찾을 수 없습니다.") }
        notice.incrementViews()
        noticeRepository.save(notice)
        return NoticeResponseDto.from(notice)
    }

    fun updateNotice(id: Int, dto: UpdateNoticeRequestDto, member: Member): NoticeResponseDto {
        require(member.role == MemberRole.ADMIN) { "관리자만 공지사항을 수정할 수 있습니다." }

        val notice = noticeRepository.findById(id).orElseThrow {
            IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        }

        notice.update(dto)

        val updatedNotice: Notice = noticeRepository.save(notice)
        return NoticeResponseDto.from(updatedNotice)
    }

    fun deleteNotice(dto: DeleteNoticeRequestDto, member: Member) {
        require(member.role == MemberRole.ADMIN) { "관리자만 공지사항을 삭제할 수 있습니다." }
        val notice: Notice = noticeRepository.findById(dto.id).orElseThrow {
            IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        }
        noticeRepository.delete(notice)
    }
}