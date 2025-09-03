package com.back.domain.member.service

import com.back.domain.member.dto.SnapshotResponse
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.Snapshot
import com.back.domain.member.extension.toSnapshotResponse
import com.back.domain.member.repository.MemberRepository
import com.back.domain.member.repository.SnapshotRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class SnapshotService(private val snapshotRepository: SnapshotRepository) {

    @Transactional
    fun saveMonthlySnapshot(member: Member, totalAsset: Long) {
        val now = YearMonth.now()
        val year = now.year
        val month = now.monthValue

        val snapshot = snapshotRepository.findByMemberAndYearAndMonth(member, year, month)

        if (snapshot != null) {
            snapshot.updateTotalAsset(totalAsset)
        }

        val newSnapshot = Snapshot(
            member=member,
            year=year,
            month=month,
            totalAsset=totalAsset,
        )

        snapshotRepository.save(newSnapshot)
    }

    @Transactional
    fun getSnapshots(member: Member): List<SnapshotResponse> {
        return snapshotRepository.findByMemberOrderByYearDescMonthDesc(member)
            .map { it.toSnapshotResponse() }
    }
}