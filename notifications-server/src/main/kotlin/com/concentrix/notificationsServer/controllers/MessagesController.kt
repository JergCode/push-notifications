package com.concentrix.notificationsServer.controllers

import com.concentrix.notificationsServer.models.PublicKey
import com.concentrix.notificationsServer.services.MessagingService
import nl.martijndwars.webpush.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
    fun subscribe(@RequestBody subscription: Subscription) {
        println(subscription.str())
        messagingService.subscribe(subscription)
    }

    @PostMapping("/unsubscribe")
    fun unsubscribe(@RequestBody endpoint: String) = messagingService::unsubscribe

}

public fun Subscription.str(): String {
    return """
        endpoint: ${this.endpoint},
        keys: {
            auth: ${this.keys?.auth},
            p256dh: ${this.keys?.p256dh}
        }        
        """.trimIndent()
}


