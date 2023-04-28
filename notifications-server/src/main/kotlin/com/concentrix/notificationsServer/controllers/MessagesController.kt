package com.concentrix.notificationsServer.controllers

import com.concentrix.notificationsServer.models.Notification
import com.concentrix.notificationsServer.models.PublicKey
import com.concentrix.notificationsServer.services.MessagingService
import com.zerodeplibs.webpush.PushSubscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
@CrossOrigin
class MessagesController {
    @Autowired
    lateinit var messagingService: MessagingService

    @GetMapping("/publicKey")
    fun getPublicKey() = PublicKey(messagingService.publicKey)

    @PostMapping("/subscribe")
    fun subscribe(@RequestBody subscription: PushSubscription) {
        println(subscription)
        messagingService.subscribe(subscription)
    }

    @PostMapping("/unsubscribe")
    fun unsubscribe(@RequestBody endpoint: String) = messagingService::unsubscribe

    @GetMapping("/sendNotification")
    fun sendNotification() {
        messagingService.sendNotifications()
    }

    @PostMapping("/sendNotification/{user}")
    fun sendNotificationToUser(
        @PathVariable(value = "user") userId: Long?,
        @RequestBody notification: Notification
    ): ResponseEntity<String> {
        return userId?.let {
             messagingService.sendNotificationForUser(userId, notification)
        } ?: ResponseEntity.badRequest().build()
    }
}



