package com.back.global.initData

import com.back.domain.account.entity.Account
import com.back.domain.account.repository.AccountRepository
import com.back.domain.asset.entity.Asset
import com.back.domain.asset.entity.AssetType
import com.back.domain.asset.repository.AssetRepository
import com.back.domain.goal.entity.Goal
import com.back.domain.goal.repository.GoalRepository
import com.back.domain.member.entity.Member
import com.back.domain.member.entity.MemberRole
import com.back.domain.member.repository.MemberRepository
import com.back.domain.notices.entity.Notice
import com.back.domain.notices.repository.NoticeRepository
import com.back.domain.transactions.entity.AccountTransaction
import com.back.domain.transactions.entity.Transaction
import com.back.domain.transactions.entity.TransactionType
import com.back.domain.transactions.repository.AccountTransactionRepository
import com.back.domain.transactions.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Profile("!test")
@Configuration
class BaseInitData(
    private val memberRepository: MemberRepository,
    private val accountRepository: AccountRepository,
    private val assetRepository: AssetRepository,
    private val transactionRepository: TransactionRepository,
    private val goalRepository: GoalRepository,
    private val accountTransactionRepository: AccountTransactionRepository,
    private val noticeRepository: NoticeRepository,
    //private val snapshotRepository: SnapshotRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Lazy
    @Autowired
    private lateinit var self: BaseInitData

    @Bean
    fun baseInitDataApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            self.initializeAllData()
        }
    }

    @Transactional
    fun initializeAllData() {
        try {
            // 1. 인증용 테스트 계정들 (최우선)
            createAuthTestMembers()

            // 2. 계정 데이터 (member에 의존)
            accountInit()

            // 3. 자산 데이터 (member에 의존)
            assetInit()

            // 4. 거래 내역 데이터 (asset에 의존)
            transactionInit()
            accountTransactionInit()

            // 5. 스냅샷 데이터 (member에 의존)
            snapShotInit()

            // 6. 목표 데이터 (member에 의존)
            goalInit()

            // 7. 공지사항 데이터 (member에 의존)
            noticeInit()

            println("모든 초기 데이터가 성공적으로 생성되었습니다.")
        } catch (e: Exception) {
            println("초기 데이터 생성 중 오류 발생: ${e.message}")
            e.printStackTrace()
            throw RuntimeException("초기 데이터 생성 실패", e)
        }
    }

    fun createAuthTestMembers() {
        // 관리자 계정 생성
        if (memberRepository.findByEmail("admin@test.com") == null) {
            memberRepository.save(
                Member(
                    "admin@test.com",
                    passwordEncoder.encode("admin123"),
                    "관리자",
                    "01000000000",
                    MemberRole.ADMIN
                )
            )
        }

        // 일반 사용자 계정 생성
        if (memberRepository.findByEmail("user1@test.com") == null) {
            memberRepository.save(
                Member(
                    "user1@test.com",
                    passwordEncoder.encode("user123"),
                    "유저1",
                    "01011111111",
                    MemberRole.USER
                )
            )
        }

        if (memberRepository.findByEmail("user2@test.com") == null) {
            memberRepository.save(
                Member(
                    "user2@test.com",
                    passwordEncoder.encode("user123"),
                    "유저2",
                    "01022222222",
                    MemberRole.USER
                )
            )
        }

        if (memberRepository.findByEmail("user3@test.com") == null) {
            memberRepository.save(
                Member(
                    "user3@test.com",
                    passwordEncoder.encode("user123"),
                    "유저3",
                    "01033333333",
                    MemberRole.USER
                )
            )
        }

        // 테스트용 추가 계정
        if (memberRepository.findByEmail("test@example.com") == null) {
            memberRepository.save(
                Member(
                    "test@example.com",
                    passwordEncoder.encode("test123"),
                    "테스트유저",
                    "01099999999",
                    MemberRole.USER
                )
            )
        }
    }

    fun accountInit() {
        if(accountRepository.count() > 0)
            return

        //유저1
        val user1 = memberRepository.findByEmail("user1@test.com")!!
        listOf(
            Account(user1, "1-111", 10000, "1-계좌1"),
            Account(user1, "1-222", 10000, "1-계좌2"),
        ).forEach { accountRepository.save(it) }

        //유저2
        val user2 = memberRepository.findByEmail("user2@test.com")!!
        listOf(
            Account(user2, "2-111", 10000, "2-계좌1"),
            Account(user2, "2-222", 10000, "2-계좌2"),
        ).forEach { accountRepository.save(it) }

        //유저3
        val user3 = memberRepository.findByEmail("user3@test.com")!!
        listOf(
            Account(user3, "3-111", 10000, "3-계좌1"),
            Account(user3, "3-222", 10000, "3-계좌2"),
        ).forEach { accountRepository.save(it) }
    }

    fun assetInit() {
        if (assetRepository.count() > 0)
            return

        //유저1
        val user1 = memberRepository.findByEmail("user1@test.com")!!
        listOf(
            Asset(user1, "KB 적금", AssetType.DEPOSIT, 140000),
            Asset(user1, "KB 예금", AssetType.DEPOSIT, 70000),
            Asset(user1, "S-Oil", AssetType.STOCK, 622000),
            Asset(user1, "삼성전자", AssetType.STOCK, 704000),
            Asset(user1, "SK하이닉스", AssetType.STOCK, 2620000),
            Asset(user1, "압구정 현대", AssetType.REAL_ESTATE, 11500000000),
            Asset(user1, "한남더힐", AssetType.REAL_ESTATE, 10000000000),
            Asset(user1, "롯데 시그니엘", AssetType.REAL_ESTATE, 7000000000)
        ).forEach { assetRepository.save(it) }

        //유저2
        val user2 = memberRepository.findByEmail("user2@test.com")!!
        listOf(
            Asset(user2, "2-예금1", AssetType.DEPOSIT, 10000),
            Asset(user2, "2-주식1", AssetType.STOCK, 20000),
            Asset(user2, "2-부동산1", AssetType.REAL_ESTATE, 30000)
        ).forEach { assetRepository.save(it) }

        //유저3
        val user3 = memberRepository.findByEmail("user3@test.com")!!
        listOf(
            Asset(user3, "3-예금1", AssetType.DEPOSIT, 10000),
            Asset(user3, "3-주식1", AssetType.STOCK, 20000),
            Asset(user3, "3-부동산1", AssetType.REAL_ESTATE, 30000)
        ).forEach { assetRepository.save(it) }
    }

    fun transactionInit() {
        if(transactionRepository.count() > 0)
            return

        //유저1
        val user1 = memberRepository.findByEmail("user1@test.com")!!
        val asset1 = assetRepository.findByMemberIdAndName(user1.id, "KB 적금")!!
        val asset2 = assetRepository.findByMemberIdAndName(user1.id, "삼성전자")!!
        val asset3 = assetRepository.findByMemberIdAndName(user1.id, "압구정 현대")!!
        listOf(
            Transaction(asset1, TransactionType.ADD, 30000, "적금 이자", LocalDateTime.of(2025, 7, 23, 0, 0, 0)),
            Transaction(asset2, TransactionType.REMOVE, 12000, "주가 하락", LocalDateTime.of(2025, 7, 1, 0, 0, 0)),
            Transaction(asset3, TransactionType.ADD, 30000, "부동산 가치 상승", LocalDateTime.of(2025, 7, 9, 0, 0, 0)),
        ).forEach { transactionRepository.save(it) }

        //유저2
        val user2 = memberRepository.findByEmail("user2@test.com")!!
        val asset4 = assetRepository.findByMemberIdAndName(user2.id, "2-예금1")!!
        listOf(
            Transaction(asset4, TransactionType.ADD, 24000, "2입금", LocalDateTime.of(2025, 7, 1, 0, 0, 0)),
        ).forEach { transactionRepository.save(it) }

        //유저3
        val user3 = memberRepository.findByEmail("user3@test.com")!!
        val asset5 = assetRepository.findByMemberIdAndName(user3.id, "3-예금1")!!
        listOf(
            Transaction(asset5, TransactionType.ADD, 71000, "3입금", LocalDateTime.of(2025, 7, 1, 0, 0, 0)),
        ).forEach { transactionRepository.save(it) }
    }

    fun accountTransactionInit() {
        if(accountTransactionRepository.count() > 0)
            return

        //유저1
        val account1 = accountRepository.findById(1).get()
        listOf(
            AccountTransaction(account1, TransactionType.ADD, 17000, "입금", LocalDateTime.of(2025, 7, 2, 0, 0, 0)),
            AccountTransaction(account1, TransactionType.ADD, 2000, "입금", LocalDateTime.of(2025, 7, 8, 0, 0, 0)),
            AccountTransaction(account1, TransactionType.REMOVE, 18000, "출금", LocalDateTime.of(2025, 7, 12, 0, 0, 0)),
            AccountTransaction(account1, TransactionType.REMOVE, 12000, "출금", LocalDateTime.of(2025, 7, 13, 0, 0, 0)),
            AccountTransaction(account1, TransactionType.ADD, 9000, "입금", LocalDateTime.of(2025, 7, 22, 0, 0, 0)),
        ).forEach { accountTransactionRepository.save(it) }

        //유저2
        val account2 = accountRepository.findById(3).get()
        listOf(
            AccountTransaction(account2, TransactionType.REMOVE, 21000, "2출금", LocalDateTime.of(2025, 7, 1, 0, 0, 0)),
        ).forEach { accountTransactionRepository.save(it) }

        //유저3
        val account3 = accountRepository.findById(3).get()
        listOf(
            AccountTransaction(account3, TransactionType.REMOVE, 30000, "3출금", LocalDateTime.of(2025, 7, 1, 0, 0, 0)),
        ).forEach { accountTransactionRepository.save(it) }
    }

    fun snapShotInit() {

    }

    fun goalInit() {
        if (goalRepository.count() > 0)
            return

        //유저1
        val user1 = memberRepository.findByEmail("user1@test.com")!!
        listOf(
            Goal(user1, "1-목표1", 10, 1000, LocalDateTime.of(2100, 1, 1, 0, 0, 0)),
            Goal(user1, "1-목표2", 20, 2000, LocalDateTime.of(2200, 1, 1, 0, 0, 0)),
        ).forEach { goalRepository.save(it) }

        //유저2
        val user2 = memberRepository.findByEmail("user2@test.com")!!
        listOf(
            Goal(user2, "2-목표1", 10, 1000, LocalDateTime.of(2100, 1, 1, 0, 0, 0)),
            Goal(user2, "2-목표2", 20, 2000, LocalDateTime.of(2200, 1, 1, 0, 0, 0)),
        ).forEach { goalRepository.save(it) }

        //유저3
        val user3 = memberRepository.findByEmail("user3@test.com")!!
        listOf(
            Goal(user3, "3-목표1", 10, 1000, LocalDateTime.of(2100, 1, 1, 0, 0, 0)),
            Goal(user3, "3-목표2", 20, 2000, LocalDateTime.of(2200, 1, 1, 0, 0, 0)),
        ).forEach { goalRepository.save(it) }
    }

    fun noticeInit() {
        if (noticeRepository.count() > 0)
            return

        val admin = memberRepository.findByEmail("admin@test.com")!!

        //공지사항
        listOf(
            Notice(
                admin,
                "자산관리 서비스 오픈 안내",
                """
                    안녕하세요! 자산관리 서비스가 정식 오픈되었습니다.
        
                    이제 여러분의 자산을 체계적으로 관리할 수 있습니다.
                    주요 기능:
                    - 자산 등록 및 관리
                    - 거래 내역 기록
                    - 목표 설정 및 추적
                    - 월별 스냅샷 기능
        
                    많은 관심과 이용 부탁드립니다!
                """.trimIndent(),
                156
            ),
            Notice(
                admin,
                "시스템 점검 안내",
                """
                    시스템 점검이 예정되어 있습니다.
        
                    점검 시간: 2025년 1월 15일 오전 2시 ~ 4시
                    점검 내용: 서버 업그레이드 및 성능 개선
        
                    점검 시간 동안 서비스 이용이 제한될 수 있습니다.
                    불편을 드려 죄송합니다.
                """.trimIndent(),
                89
            ),
            Notice(
                admin,
                "새로운 기능 업데이트",
                """
                    새로운 기능이 추가되었습니다!
        
                    추가된 기능:
                    1. 공지사항 시스템
                    2. 실시간 알림 기능
                    3. 데이터 백업 기능
                    4. 모바일 최적화
        
                    더 나은 서비스를 위해 계속 노력하겠습니다.
                """.trimIndent(),
                203
            ),
            Notice(
                admin,
                "개인정보 보호 정책 업데이트",
                """
                    개인정보 보호 정책이 업데이트되었습니다.
        
                    주요 변경사항:
                    - 데이터 암호화 강화
                    - 개인정보 수집 동의 절차 개선
                    - 데이터 보관 기간 명시
        
                    자세한 내용은 개인정보처리방침을 참고해 주세요.
                """.trimIndent(),
                67
            ),
            Notice(
                admin,
                "고객센터 운영 시간 안내",
                """
                    고객센터 운영 시간을 안내드립니다.
        
                    운영 시간:
                    - 평일: 오전 9시 ~ 오후 6시
                    - 토요일: 오전 9시 ~ 오후 1시
                    - 일요일 및 공휴일: 휴무
        
                    문의사항이 있으시면 언제든 연락주세요.
                """.trimIndent(),
                342
            ),
            Notice(
                admin,
                "계좌 등록 방법 안내",
                """
                    계좌를 등록하는 방법을 안내드립니다.
        
                    등록 방법:
                    1. 마이페이지 > 계좌 목록으로 이동
                    2. '계좌 추가' 버튼 클릭
                    3. 계좌 정보 입력 (은행명, 계좌번호, 잔액)
                    4. 저장 버튼 클릭
        
                    계좌 등록 후 거래 내역도 함께 관리할 수 있습니다.
                """.trimIndent(),
                134
            ),
            Notice(
                admin,
                "자산 추가 기능 업데이트",
                """
                    자산 추가 기능이 업데이트되었습니다.
        
                    개선된 기능:
                    1. 자동 완성 기능
                    2. 실시간 검증
                    3. 일괄 등록 기능
                    4. 데이터 백업
        
                    이제 더욱 직관적이고 편리하게 자산을 관리할 수 있습니다.
                """.trimIndent(),
                178
            ),
            Notice(
                admin,
                "목표 설정 가이드",
                """
                    자산 관리의 목표를 설정하는 방법을 안내드립니다.
        
                    목표 설정 방법:
                    1. 구체적이고 달성 가능한 목표 설정
                    2. 기한과 목표 금액 설정
                    3. 정기적인 목표 점검
                    4. 목표 달성 시 축하 메시지
        
                    더욱 효과적인 자산 관리를 해보세요.
                """.trimIndent(),
                95
            ),
            Notice(
                admin,
                "모바일 앱 출시 안내",
                """
                    자산관리 서비스 모바일 앱이 출시되었습니다!
        
                    모바일 앱 특징:
                    - 언제 어디서나 자산 확인
                    - 푸시 알림 기능
                    - 생체 인증 지원
                    - 오프라인 모드
        
                    앱스토어에서 다운로드하세요.
                """.trimIndent(),
                267
            ),
            Notice(
                admin,
                "데이터 백업 서비스 안내",
                """
                    데이터 백업 서비스가 시작되었습니다.
        
                    백업 서비스 특징:
                    - 자동 백업 (매일 오전 3시)
                    - 클라우드 저장소 활용
                    - 데이터 암호화
                    - 복원 기능 제공
        
                    안전한 데이터 보관을 약속드립니다.
                """.trimIndent(),
                112
            ),
            Notice(
                admin,
                "보안 강화 업데이트",
                """
                    보안이 강화되었습니다.
        
                    보안 강화 내용:
                    1. 2단계 인증 추가
                    2. 로그인 시도 제한
                    3. 의심스러운 활동 감지
                    4. 자동 로그아웃 기능
        
                    더욱 안전한 서비스 이용이 가능합니다.
                """.trimIndent(),
                189
            ),
            Notice(
                admin,
                "API 서비스 오픈",
                """
                    API 서비스가 오픈되었습니다.
        
                    API 서비스 특징:
                    - RESTful API 제공
                    - JSON 형식 데이터
                    - 인증 토큰 기반
                    - 상세한 문서 제공
        
                    개발자분들의 많은 관심 부탁드립니다.
                """.trimIndent(),
                76
            ),
            Notice(
                admin,
                "성능 최적화 완료",
                """
                    서비스 성능이 최적화되었습니다.
        
                    최적화 내용:
                    1. 페이지 로딩 속도 개선
                    2. 데이터베이스 쿼리 최적화
                    3. 캐싱 시스템 도입
                    4. CDN 서비스 적용
        
                    더욱 빠른 서비스 이용이 가능합니다.
                """.trimIndent(),
                145
            ),
            Notice(
                admin,
                "사용자 피드백 반영",
                """
                    사용자분들의 소중한 피드백을 반영했습니다.
        
                    반영된 피드백:
                    1. UI/UX 개선
                    2. 기능 추가 요청
                    3. 버그 수정
                    4. 성능 개선
        
                    앞으로도 많은 피드백 부탁드립니다.
                """.trimIndent(),
                98
            ),
            Notice(
                admin,
                "연말 감사 인사",
                """
                    2024년 한 해 동안 많은 관심과 사랑을 주셔서 감사합니다.
        
                    2024년 주요 성과:
                    - 서비스 오픈
                    - 사용자 10,000명 달성
                    - 기능 확장
                    - 보안 강화
        
                    2025년에도 더 나은 서비스로 보답하겠습니다.
                """.trimIndent(),
                234
            ),
            Notice(
                admin,
                "2025년 신년 인사",
                """
                    새해 복 많이 받으세요!
        
                    2025년에도 자산관리 서비스와 함께
                    건강하고 풍요로운 한 해 되시기 바랍니다.
        
                    새로운 기능과 개선사항으로
                    더욱 편리한 서비스를 제공하겠습니다.
                """.trimIndent(),
                567
            ),
            Notice(
                admin,
                "월별 리포트 기능 추가",
                """
                    월별 자산 리포트 기능이 추가되었습니다.
        
                    새로운 기능:
                    1. 월별 자산 변화 그래프
                    2. 수입/지출 분석
                    3. 목표 달성률 확인
                    4. PDF 다운로드
        
                    더욱 체계적인 자산 관리가 가능합니다.
                """.trimIndent(),
                189
            ),
            Notice(
                admin,
                "알림 설정 방법 안내",
                """
                    알림 설정 방법을 안내드립니다.
        
                    설정 가능한 알림:
                    1. 목표 달성 알림
                    2. 거래 내역 알림
                    3. 시스템 점검 알림
                    4. 보안 알림
        
                    설정 > 알림에서 원하는 알림을 선택하세요.
                """.trimIndent(),
                123
            ),
            Notice(
                admin,
                "데이터 내보내기 기능",
                """
                    데이터 내보내기 기능이 추가되었습니다.
        
                    지원 형식:
                    1. Excel (.xlsx)
                    2. CSV (.csv)
                    3. PDF (.pdf)
                    4. JSON (.json)
        
                    마이페이지 > 설정에서 이용하세요.
                """.trimIndent(),
                156
            ),
            Notice(
                admin,
                "다크 모드 지원",
                """
                    다크 모드가 지원됩니다.
        
                    다크 모드 특징:
                    1. 눈의 피로도 감소
                    2. 배터리 절약
                    3. 세련된 디자인
                    4. 자동 전환 기능
        
                    설정에서 테마를 변경하세요.
                """.trimIndent(),
                234
            ),
            Notice(
                admin,
                "키보드 단축키 안내",
                """
                    키보드 단축키를 안내드립니다.
        
                    주요 단축키:
                    Ctrl + N: 새 공지사항
                    Ctrl + S: 저장
                    Ctrl + F: 검색
                    Ctrl + Z: 실행 취소
        
                    더욱 빠른 작업이 가능합니다.
                """.trimIndent(),
                98
            ),
            Notice(
                admin,
                "브라우저 호환성 개선",
                """
                    브라우저 호환성이 개선되었습니다.
        
                    지원 브라우저:
                    1. Chrome (최신 버전)
                    2. Firefox (최신 버전)
                    3. Safari (최신 버전)
                    4. Edge (최신 버전)
        
                    모든 브라우저에서 원활한 이용이 가능합니다.
                """.trimIndent(),
                145
            ),
            Notice(
                admin,
                "접근성 개선",
                """
                    접근성이 개선되었습니다.
        
                    개선 사항:
                    1. 스크린 리더 지원
                    2. 키보드 네비게이션
                    3. 고대비 모드
                    4. 폰트 크기 조절
        
                    모든 사용자가 편리하게 이용할 수 있습니다.
                """.trimIndent(),
                167
            ),
            Notice(
                admin,
                "실시간 채팅 지원",
                """
                    실시간 채팅 지원이 시작되었습니다.
        
                    채팅 지원 특징:
                    1. 24시간 운영
                    2. 즉시 응답
                    3. 파일 첨부 가능
                    4. 화면 공유 기능
        
                    더욱 편리한 고객 지원을 제공합니다.
                """.trimIndent(),
                289
            ),
            Notice(
                admin,
                "소셜 로그인 추가",
                """
                    소셜 로그인이 추가되었습니다.
        
                    지원 서비스:
                    1. Google 로그인
                    2. Facebook 로그인
                    3. Apple 로그인
                    4. Kakao 로그인
        
                    더욱 간편한 로그인이 가능합니다.
                """.trimIndent(),
                198
            ),
            Notice(
                admin,
                "자동 백업 스케줄",
                """
                    자동 백업 스케줄이 설정되었습니다.
        
                    백업 스케줄:
                    1. 매일 오전 3시
                    2. 매주 일요일 오전 2시
                    3. 매월 1일 오전 1시
                    4. 수동 백업 가능
        
                    안전한 데이터 보관을 약속드립니다.
                """.trimIndent(),
                134
            ),
            Notice(
                admin,
                "데이터 분석 리포트",
                """
                    데이터 분석 리포트 기능이 추가되었습니다.
        
                    분석 내용:
                    1. 자산 변화 추이
                    2. 수입/지출 패턴
                    3. 목표 달성 분석
                    4. 예측 모델 제공
        
                    더욱 과학적인 자산 관리가 가능합니다.
                """.trimIndent(),
                223
            ),
            Notice(
                admin,
                "모바일 최적화 완료",
                """
                    모바일 최적화가 완료되었습니다.
        
                    최적화 내용:
                    1. 반응형 디자인
                    2. 터치 인터페이스
                    3. 빠른 로딩 속도
                    4. 오프라인 모드
        
                    모바일에서도 편리한 이용이 가능합니다.
                """.trimIndent(),
                345
            ),
            Notice(
                admin,
                "보안 인증 강화",
                """
                    보안 인증이 강화되었습니다.
        
                    강화된 보안:
                    1. 2단계 인증 (SMS, 이메일)
                    2. 생체 인증 지원
                    3. 로그인 시도 제한
                    4. 의심스러운 활동 감지
        
                    더욱 안전한 서비스 이용이 가능합니다.
                """.trimIndent(),
                178
            ),
            noticeRepository.save(
                Notice(
                    admin,
                    "고객 만족도 조사",
                    """
                    고객 만족도 조사에 참여해 주세요.
        
                    조사 내용:
                    1. 서비스 만족도
                    2. 기능 개선 제안
                    3. 추가 기능 요청
                    4. 불편사항 신고
        
                    여러분의 소중한 의견을 기다립니다.
                """.trimIndent(),
                    267
                )
            ),
        ).forEach { noticeRepository.save(it) }
    }
}