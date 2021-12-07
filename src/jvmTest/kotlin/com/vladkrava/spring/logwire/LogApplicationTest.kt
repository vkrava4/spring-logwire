package com.vladkrava.spring.logwire

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [LogApplication::class])
class LogApplicationTest {

    @Test
    fun `should load application context without failures`() {
        // Do nothing here
    }
}