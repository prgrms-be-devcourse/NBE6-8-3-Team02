package com.back.domain.goal.service

// import com.back.domain.member.entity.Member // Member 팀 작업 전까지 주석 처리
import com.back.domain.goal.dto.GoalRequestDto
import com.back.domain.goal.entity.Goal
import com.back.domain.goal.repository.GoalRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true) // 클래스 레벨에 @Transactional을 선언하여 모든 public 메소드에 적용
class GoalService(
    private val goalRepository: GoalRepository // 주 생성자를 통한 의존성 주입
) {

    fun findById(id: Long): Goal {
        return goalRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("존재하지 않는 목표입니다.")
    }

    /* Member 팀 작업 전까지 주석 처리
    fun findByMember(member: Member, page: Int, size: Int): List<Goal> {
        val pageable: Pageable = PageRequest.of(page, size)
        return goalRepository.findByMemberId(member.id!!, pageable).content
    }
    */

//    @Transactional
//    fun create(/*member: Member,*/ requestDto: GoalRequestDto): Goal {
//        // DTO의 확장 함수를 사용하여 Entity를 생성
//        val goal = requestDto.toEntity(/*member*/)
//        return goalRepository.save(goal)
//    }

    @Transactional
    fun modify(id: Long, requestDto: GoalRequestDto) {
        val goal = findById(id) // 기존 findById 메소드 재활용

        // Entity의 update 메소드를 사용하여 상태 변경
        goal.update(
            description = requestDto.description,
            targetAmount = requestDto.targetAmount,
            deadline = requestDto.deadline
        )
        // Spring Data JPA의 Dirty Checking에 의해 트랜잭션 종료 시 자동 업데이트
    }

    @Transactional
    fun delete(id: Long) {
        val goal = findById(id) // ID 존재 여부 확인
        goalRepository.delete(goal)
    }
}
