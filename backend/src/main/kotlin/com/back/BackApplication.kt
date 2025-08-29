package com.back

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BackApplication

private val logger = KotlinLogging.logger {}
fun main(args: Array<String>) {
    logger.info("애플리케이션 시작을 알립니다.")
    logger.debug("DEBUG 레벨이 제대로 작동하는지 확인합니다.")
    runApplication<BackApplication>(*args)
}
