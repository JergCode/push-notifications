package com.concentrix.notificationsServer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class NotificationsServerApplication

fun main(args: Array<String>) {
    runApplication<NotificationsServerApplication>(*args)
}
