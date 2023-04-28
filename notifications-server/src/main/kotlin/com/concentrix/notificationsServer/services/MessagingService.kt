package com.concentrix.notificationsServer.services

import com.concentrix.notificationsServer.exceptions.NotificationNotSupportedException
import com.concentrix.notificationsServer.exceptions.RepositoryException
import com.concentrix.notificationsServer.models.Message
import com.concentrix.notificationsServer.models.Notification
import com.concentrix.notificationsServer.models.NotificationType
import com.concentrix.notificationsServer.models.Subscription
import com.concentrix.notificationsServer.repositories.NotificationTypesRepository
import com.concentrix.notificationsServer.repositories.SubscriptionsRepository
import com.concentrix.notificationsServer.repositories.UsersRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.zerodeplibs.webpush.PushSubscription
import com.zerodeplibs.webpush.VAPIDKeyPair
import com.zerodeplibs.webpush.httpclient.StandardHttpClientRequestPreparer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.security.GeneralSecurityException
import java.time.LocalTime
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


@Service
class MessagingService(
    private val objectMapper: ObjectMapper,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val usersRepository: UsersRepository,
    private val notificationTypesRepository: NotificationTypesRepository,
) {
    val publicKey: String get() = vapidKeyPair.extractPublicKeyInUncompressedFormAsString()

    @Autowired
    private lateinit var vapidKeyPair: VAPIDKeyPair

    private var subscriptions: MutableSet<PushSubscription> = mutableSetOf()

    fun subscribe(subscription: PushSubscription): ResponseEntity<String> {
        return try {
            val user = usersRepository.findUserById(1)
            println("USER: $user")
            if (user.subscriptions?.any { it.endpoint == subscription.endpoint } == false) {
                println("SUBSCRIBING")
                subscriptionsRepository.save(
                    Subscription(
                        endpoint = subscription.endpoint,
                        p256dhKey = subscription.keys.p256dh,
                        authKey = subscription.keys.auth,
                        user = user,
                    )
                )
            } else {
                println("SUBSCRIPTION ALREADY REGISTERED")
            }
            ResponseEntity.ok().body("Subscribed")
        } catch (ex: RepositoryException) {
            ResponseEntity.badRequest().body("Could not save subscription")
        }
    }

    fun unsubscribe(endpoint: String) {
        subscriptions.removeIf { it.endpoint == endpoint }
    }

    fun sendNotification(subscription: PushSubscription, message: String, type: NotificationType? = null): String {
        return try {
            println("Sending message")
            val notification = Message(type?.type ?: "Default Title", message)
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
                is GeneralSecurityException, is IOException, is ExecutionException,
                is InterruptedException, is NullPointerException, is IllegalStateException, is IllegalArgumentException -> e.printStackTrace()
            }
            "Error Sending Message"
        }
    }

    fun sendNotificationForUser(userId: Long, notification: Notification): ResponseEntity<String> {
        return try {
            val user = usersRepository.findUserById(userId)

            val notificationType = notification.type?.let { type ->
                val notificationType = notificationTypesRepository.findByType(type)?.also {
                    println(it)
                }
                    ?: throw NotificationNotSupportedException("Notification type $type is not supported")

                if (user.notificationTypes?.any { it == notificationType } == true) {
                    notificationType
                } else throw NotificationNotSupportedException("Notification type $type is not allowed by user")
            }

            user.subscriptions
                ?.map(Subscription::toPushSubscription)
                ?.forEach {
                    println(it)
                    sendNotification(it, notification.message, notificationType)
                }
            ResponseEntity.ok().body("Notification sent")
        } catch (ex: RepositoryException) {
            ResponseEntity.badRequest().body("Could send Notification: ${ex.message}")
        } catch (ex: NotificationNotSupportedException) {
            ResponseEntity.badRequest().body("Could send Notification: ${ex.message}")
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