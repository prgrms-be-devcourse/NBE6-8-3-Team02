package com.back.domain.notices.service

import com.back.domain.member.entity.Member
import com.back.domain.member.repository.MemberRepository
import com.back.domain.notices.dto.CreateNoticeRequestDto
import com.back.domain.notices.dto.DeleteNoticeRequestDto
import com.back.domain.notices.dto.UpdateNoticeRequestDto
import com.back.domain.notices.entity.Notice
import com.back.domain.notices.repository.NoticeRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoticeServiceTest(
    @Autowired private val noticeService: NoticeService,
    @Autowired private val noticeRepository: NoticeRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private lateinit var admin: Member
    private lateinit var user: Member
    private lateinit var notice1: Notice
    private lateinit var notice2: Notice

    @BeforeAll
    fun setUp() {
        admin = memberRepository.findByEmail("admintest@test.com")!!
        user = memberRepository.findByEmail("usertest@test.com")!!
        memberRepository.save(admin)
        memberRepository.save(user)

        notice1 = noticeRepository.save(
            Notice(admin, "공지 1", "내용 1", 0, "")
        )
        notice2 = noticeRepository.save(
            Notice(admin, "공지 2", "내용 2", 0, "")
        )
    }

    @Test
    @DisplayName("관리자만 공지사항 작성 가능 테스트")
    fun createNoticeTest() {
        val dto = CreateNoticeRequestDto("새 공지", "새 내용", "")
        val savedNotice = noticeService.createNotice(dto, admin)

        assertEquals(dto.title, savedNotice.title)
        assertEquals(dto.content, savedNotice.content)

        // 일반 회원으로 생성 시 예외 발생
        assertFailsWith<IllegalArgumentException> {
            noticeService.createNotice(dto, user)
        }
    }

    @Test
    @DisplayName("공지사항 전체 조회 테스트 (검색 포함)")
    fun getAllNoticesTest() {
        val pageable = PageRequest.of(0, 10)
        val allNotices = noticeService.getAllNotices("", pageable)
        val searchedNotices = noticeService.getAllNotices("공지 1", pageable)

        assertEquals(2, allNotices.totalElements)
        assertEquals(1, searchedNotices.totalElements)
        assertTrue(searchedNotices.content.all { it.title.contains("공지 1") })
    }

    @Test
    @DisplayName("공지사항 ID 조회 테스트")
    fun getNoticeByIdTest() {
        val noticeDto = noticeService.getNoticeById(notice1.id)
        assertEquals(notice1.title, noticeDto.title)
        assertEquals(1, noticeRepository.findById(notice1.id).get().views)
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    fun updateNoticeTest() {
        val dto = UpdateNoticeRequestDto("수정된 제목", "수정된 내용", "")

        val updatedNotice = noticeService.updateNotice(notice1.id, dto, admin)
        assertEquals(dto.title, updatedNotice.title)
        assertEquals(dto.content, updatedNotice.content)

        // 일반 회원으로 수정 시 예외 발생
        assertFailsWith<IllegalArgumentException> {
            noticeService.updateNotice(notice1.id, dto, user)
        }
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    fun deleteNoticeTest() {
        val dto = DeleteNoticeRequestDto(notice2.id)

        noticeService.deleteNotice(dto, admin)
        assertTrue(noticeRepository.findById(notice2.id).isEmpty)

        // 일반 회원으로 삭제 시 예외 발생
        assertFailsWith<IllegalArgumentException> {
            noticeService.deleteNotice(dto, user)
        }
    }
}