package com.back.domain.member.repository

import com.back.domain.member.entity.Member
import com.back.domain.member.entity.Snapshot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SnapshotRepository: JpaRepository<Snapshot, Long> {

    fun findByMemberAndYearAndMonth(member: Member, Year: Int, Month: Int) : Snapshot?
    fun findByMemberOrderByYearDescMonthDesc(member: Member):List<Snapshot>
}