package com.back.domain.member.controller

import com.back.domain.member.dto.SnapshotResponse
import com.back.domain.member.service.MemberService
import com.back.domain.member.service.SnapshotService
import com.back.global.security.CustomMemberDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter

@RestController
@RequestMapping("/api/v1/snapshot")
class SnapshotController(
    private val snapshotService: SnapshotService,
    private val memberService: MemberService
) {
    @PostMapping("/save")
    fun saveSnapshot(
        @AuthenticationPrincipal memberDetails: CustomMemberDetails,
        @RequestParam totalAsset: Long
    ) :ResponseEntity<Long> {
        val member = memberDetails.getMember()

        snapshotService.saveMonthlySnapshot(member, totalAsset)
        return ResponseEntity.ok(totalAsset)
    }

    @GetMapping
    fun getSnapshots(@AuthenticationPrincipal memberDetails: CustomMemberDetails)
    : ResponseEntity<List<SnapshotResponse>>{
        val member = memberDetails.getMember()

        val response = snapshotService.getSnapshots(member)
        return ResponseEntity.ok(response)
    }
}