package com.back.domain.notices.service

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
class NoticeService {
    private lateinit var noticeRepository: NoticeRepository

    fun createNotice(dto: CreateNoticeRequestDto, /*member: Member*/) : NoticeResponseDto {
        //if(member.role != MemberRole.ADMIN) throw IllegalArgumentException("관리자만 공지사항을 작성할 수 있습니다.")
        //Member 도메인 완성 필요

        val notice: Notice = Notice(
            //member
            title = dto.title,
            content = dto.content,
            views = 0,
            fileUrl = dto.fileUrl,
        )

        val savedNotice: Notice = noticeRepository.save(notice)
        return NoticeResponseDto.from(savedNotice)
    }

    //Kotlin 스타일로, if 문 수정.
    fun getAllNotices(search: String, pageable: Pageable): Page<NoticeResponseDto> {
        var notices: Page<Notice>
        if(search.isNotBlank()) {notices = noticeRepository.findByTitleContainingIgnoreCase(search, pageable)}
        else notices = noticeRepository.findAll(pageable)

        return notices.map { notice -> NoticeResponseDto.from(notice) }
    }

    fun getNoticeById(id: Int): NoticeResponseDto {
        val notice: Notice = noticeRepository.findById(id).orElseThrow {
            IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        }
        notice.incrementViews()
        noticeRepository.save(notice)
        return NoticeResponseDto.from(notice)
    }

    fun updateNotice(id: Int, dto: UpdateNoticeRequestDto, /*member: Member*/): NoticeResponseDto {
        val notice = noticeRepository.findById(id).orElseThrow {
            IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        }

        //if(member.role != MemberRole.ADMIN) { throw IllegalArgumentException("관리자만 공지사항을 수정할 수 있습니다.") }

        //Kotlin 스타일로, 공백 체크같은 걸 깔끔하게.
        //Notice의 상태를 업데이트 하는 로직, 엔티티 내부의 함수로 변경하는 게 좋지 않을까?
        //if(dto.title.isNotBlank()) {notice.title = dto.title}
        //if(dto.content.isNotBlank()) {notice.content = dto.content}
        //if(dto.fileUrl.isNotBlank()) {notice.fileUrl = dto.fileUrl}
        notice.update(dto)

        val updatedNotice: Notice = noticeRepository.save(notice)
        return NoticeResponseDto.from(updatedNotice)
    }

    fun deleteNotice(dto: DeleteNoticeRequestDto, /*member: Member*/) {
        val notice: Notice = noticeRepository.findById(dto.id).orElseThrow {
            IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        }
        //if(member.role != MemberRole.ADMIN) { throw IllegalArgumentException("관리자만 공지사항을 삭제할 수 있습니다.") }
        noticeRepository.delete(notice)
    }
}