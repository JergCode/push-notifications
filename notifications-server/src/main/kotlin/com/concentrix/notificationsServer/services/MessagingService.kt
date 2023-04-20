package com.concentrix.notificationsServer.services

import com.concentrix.notificationsServer.controllers.str
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jose4j.lang.JoseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.Security
import java.time.LocalTime
import java.util.concurrent.ExecutionException
import javax.annotation.PostConstruct
import kotlin.math.log

@Service
class MessagingService {
    @Value("\${vapid.public.key}")
    private lateinit var _publicKey: String
    val publicKey: String get() = _publicKey

    @Value("\${vapid.private.key}")
    private lateinit var privateKey: String

    private lateinit var pushService: PushService
    private var subscriptions: MutableList<Subscription> = mutableListOf()

    @PostConstruct
    private fun init() {
        Security.addProvider(BouncyCastleProvider())
        pushService = PushService(_publicKey, privateKey)
    }

    fun subscribe(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    fun unsubscribe(endpoint: String) {
        subscriptions = subscriptions.filter { it.endpoint == endpoint }.toMutableList()
    }

    fun sendNotification(subscription: Subscription, message: String) {
        try {
            val response = pushService.send(Notification(subscription, message))
        } catch (e: Exception) {
            when (e) {
                is GeneralSecurityException, is IOException, is JoseException, is ExecutionException,
                is InterruptedException -> e.printStackTrace()
            }
        }
    }

    @Scheduled(fixedRate = 15000)
    fun sendNotifications() {
        val json = """
            {
              "title": "Server says hello!",
              "body": "It is now: %s"
            }
        """.trimIndent().format(LocalTime.now())

        subscriptions.forEach { subscription ->
            sendNotification(subscription, json)
        }
    }
}