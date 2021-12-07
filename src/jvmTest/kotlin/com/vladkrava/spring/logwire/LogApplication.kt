package com.vladkrava.spring.logwire

import com.vladkrava.spring.logwire.annotations.Slf4jWire
import org.slf4j.Logger
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@Configuration
@SpringBootApplication
class LogApplication : CommandLineRunner {

    @Slf4jWire
    lateinit var log: Logger

    override fun run(vararg args: String?) {
        log.info("Log application entry")
    }
}

fun main(args: Array<String>) {
    runApplication<LogApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}