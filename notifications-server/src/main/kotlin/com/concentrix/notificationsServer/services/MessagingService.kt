package com.concentrix.notificationsServer.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.zerodeplibs.webpush.PushSubscription
import com.zerodeplibs.webpush.VAPIDKeyPair
import com.zerodeplibs.webpush.httpclient.StandardHttpClientRequestPreparer
import org.jose4j.lang.JoseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.security.GeneralSecurityException
import java.time.LocalTime
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


data class Notification(
        val body: String,
        val title: String,
)

@Service
class MessagingService(
        private val objectMapper: ObjectMapper
) {

    val publicKey: String get() = vapidKeyPair.extractPublicKeyInUncompressedFormAsString()

    @Autowired
    private lateinit var vapidKeyPair: VAPIDKeyPair

    private var subscriptions: MutableSet<PushSubscription> = mutableSetOf()

    fun subscribe(subscription: PushSubscription) {
        subscriptions.add(subscription)
    }

    fun unsubscribe(endpoint: String) {
        subscriptions.removeIf { it.endpoint == endpoint }
    }

    fun sendNotification(subscription: PushSubscription, message: String): String {
        return try {
            println("Sending message")
            val notification = Notification("Test Message", "Some message")
            val httpClient = HttpClient.newBuilder().build()

            val request = StandardHttpClientRequestPreparer.getBuilder()
                    .pushSubscription(subscription)
                    .vapidJWTExpiresAfter(15, TimeUnit.MINUTES)
                    .vapidJWTSubject("mailto:example@test.com")
                    .pushMessage(objectMapper.writeValueAsString(notification))
                    .ttl(1, TimeUnit.HOURS)
                    .urgencyLow()
                    .topic("TestingPushMessaging")
                    .build(vapidKeyPair)
                    .toRequest()

            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            println(String.format("[Http Client] status code: %d", response.statusCode()))
            "Message Sent"

        } catch (e: Exception) {
            when (e) {
                is GeneralSecurityException, is IOException, is JoseException, is ExecutionException,
                is InterruptedException -> e.printStackTrace()
            }
            "Error Sending Message"
        }
    }

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