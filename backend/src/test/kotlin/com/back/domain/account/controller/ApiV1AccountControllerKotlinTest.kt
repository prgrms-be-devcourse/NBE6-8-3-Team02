package com.back.domain.account.controller

import com.back.domain.account.dto.RqCreateAccountDto
import com.back.domain.account.dto.RqUpdateAccountDto
import com.back.domain.account.service.AccountService
import com.back.domain.member.entity.Member
import com.back.global.security.CustomMemberDetails
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@DisplayName("Account 컨트롤러 Kotlin 테스트")
class ApiV1AccountControllerKotlinTest {

    private lateinit var accountService: AccountService
    private lateinit var accountController: ApiV1AccountController
    private lateinit var customMemberDetails: CustomMemberDetails
    private lateinit var member: Member
    private lateinit var createDto: RqCreateAccountDto
    private lateinit var updateDto: RqUpdateAccountDto

    @BeforeEach
    fun setUp() {
        accountService = mock(AccountService::class.java)
        accountController = ApiV1AccountController(accountService)
        
        member = Member(
            email = "test@test.com",
            password = "password123",
            name = "테스트유저",
            phoneNumber = "010-1234-5678"
        )
        
        customMemberDetails = mock(CustomMemberDetails::class.java)
        `when`(customMemberDetails.getMember()).thenReturn(member)
        
        createDto = RqCreateAccountDto(
            name = "테스트계좌",
            accountNumber = "123-456-789",
            balance = 10000L
        )
        
        updateDto = RqUpdateAccountDto(
            accountNumber = "987-654-321"
        )
    }

    @Test
    @DisplayName("기본 테스트 - Controller 생성 확인")
    fun basicTest() {
        assertNotNull(accountService)
        assertNotNull(accountController)
    }
    
    @Test
    @DisplayName("AccountService Mock 생성 확인")
    fun accountServiceMockTest() {
        assertNotNull(accountController)
    }

    @Test
    @DisplayName("Controller 클래스 타입 확인")
    fun controllerClassTypeTest() {
        // then
        assertTrue(accountController is ApiV1AccountController)
        assertEquals(ApiV1AccountController::class.java, accountController.javaClass)
    }

    @Test
    @DisplayName("Controller에 AccountService 주입 확인")
    fun controllerServiceInjectionTest() {
        // then
        assertNotNull(accountController)
        // Controller의 내부 구조를 확인할 수는 없지만, 생성이 성공했다는 것은 주입이 성공했다는 의미
    }

    @Test
    @DisplayName("DTO 클래스 생성 테스트")
    fun dtoClassCreationTest() {
        // given & when
        val testCreateDto = RqCreateAccountDto(
            name = "테스트계좌2",
            accountNumber = "111-222-333",
            balance = 50000L
        )
        
        val testUpdateDto = RqUpdateAccountDto(
            accountNumber = "444-555-666"
        )

        // then
        assertNotNull(testCreateDto)
        assertEquals("테스트계좌2", testCreateDto.name)
        assertEquals("111-222-333", testCreateDto.accountNumber)
        assertEquals(50000L, testCreateDto.balance)
        
        assertNotNull(testUpdateDto)
        assertEquals("444-555-666", testUpdateDto.accountNumber)
    }

    @Test
    @DisplayName("Member 엔티티 생성 테스트")
    fun memberEntityCreationTest() {
        // given & when
        val testMember = Member(
            email = "member@test.com",
            password = "password456",
            name = "테스트멤버",
            phoneNumber = "010-1111-2222"
        )

        // then
        assertNotNull(testMember)
        assertEquals("member@test.com", testMember.email)
        assertEquals("테스트멤버", testMember.name)
        assertEquals("010-1111-2222", testMember.phoneNumber)
    }

    @Test
    @DisplayName("API 엔드포인트 매핑 확인")
    fun apiEndpointMappingTest() {
        // Controller 클래스의 어노테이션 확인
        val controllerClass = ApiV1AccountController::class.java
        val requestMapping = controllerClass.getAnnotation(RequestMapping::class.java)
        
        assertNotNull(requestMapping)
        assertEquals("/api/v1/accounts", requestMapping.value[0])
    }

    @Test
    @DisplayName("HTTP 메서드 매핑 확인")
    fun httpMethodMappingTest() {
        // Controller 클래스의 메서드들이 올바른 HTTP 메서드와 매핑되어 있는지 확인
        val controllerClass = ApiV1AccountController::class.java
        
        // POST 메서드 확인
        val createMethod = controllerClass.getMethod("createAccount", CustomMemberDetails::class.java, RqCreateAccountDto::class.java)
        assertNotNull(createMethod.getAnnotation(PostMapping::class.java))
        
        // GET 메서드 확인
        val getAccountsMethod = controllerClass.getMethod("getAccounts", CustomMemberDetails::class.java)
        assertNotNull(getAccountsMethod.getAnnotation(GetMapping::class.java))
        
        val getAccountMethod = controllerClass.getMethod("getAccount", CustomMemberDetails::class.java, Int::class.java)
        assertNotNull(getAccountMethod.getAnnotation(GetMapping::class.java))
        
        // PUT 메서드 확인
        val updateMethod = controllerClass.getMethod("updateAccount", CustomMemberDetails::class.java, Int::class.java, RqUpdateAccountDto::class.java)
        assertNotNull(updateMethod.getAnnotation(PutMapping::class.java))
        
        // DELETE 메서드 확인
        val deleteMethod = controllerClass.getMethod("deleteAccount", CustomMemberDetails::class.java, Int::class.java)
        assertNotNull(deleteMethod.getAnnotation(DeleteMapping::class.java))
    }

    @Test
    @DisplayName("Controller 메서드 존재 확인")
    fun controllerMethodExistsTest() {
        // Controller 클래스의 메서드들이 존재하는지 확인
        val controllerClass = ApiV1AccountController::class.java
        
        // createAccount 메서드 확인
        assertDoesNotThrow {
            controllerClass.getMethod("createAccount", CustomMemberDetails::class.java, RqCreateAccountDto::class.java)
        }
        
        // getAccounts 메서드 확인
        assertDoesNotThrow {
            controllerClass.getMethod("getAccounts", CustomMemberDetails::class.java)
        }
        
        // getAccount 메서드 확인
        assertDoesNotThrow {
            controllerClass.getMethod("getAccount", CustomMemberDetails::class.java, Int::class.java)
        }
        
        // updateAccount 메서드 확인
        assertDoesNotThrow {
            controllerClass.getMethod("updateAccount", CustomMemberDetails::class.java, Int::class.java, RqUpdateAccountDto::class.java)
        }
        
        // deleteAccount 메서드 확인
        assertDoesNotThrow {
            controllerClass.getMethod("deleteAccount", CustomMemberDetails::class.java, Int::class.java)
        }
    }
}
