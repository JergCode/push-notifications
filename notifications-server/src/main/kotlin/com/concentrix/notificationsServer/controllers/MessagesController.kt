package com.concentrix.notificationsServer.controllers

import com.concentrix.notificationsServer.models.PublicKey
import com.concentrix.notificationsServer.services.MessagingService
import com.zerodeplibs.webpush.PushSubscription
import nl.martijndwars.webpush.Subscription
import org.asynchttpclient.util.HttpConstants.ResponseStatusCodes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

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

}



