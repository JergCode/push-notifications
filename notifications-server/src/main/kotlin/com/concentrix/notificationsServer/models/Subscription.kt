package com.concentrix.notificationsServer.models

import com.zerodeplibs.webpush.PushSubscription
import com.zerodeplibs.webpush.PushSubscription.Keys
import javax.persistence.*

@Entity
@Table(name = "SUBSCRIPTIONS")
data class Subscription(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    val endpoint: String = "",
    @Column(name = "p256dh_key")
    val p256dhKey: String = "",
    @Column(name = "auth_key")
    val authKey: String = "",
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,
) {
    override fun toString(): String {
        return "Subscription(id=$id, endpoint='$endpoint', p256dhKey='$p256dhKey', authKey='$authKey', user=${user?.email})"
    }

    fun toPushSubscription(): PushSubscription {
        val keys = Keys().apply {
            p256dh = p256dhKey
            auth = authKey
        }
        return PushSubscription().apply {
            this.endpoint = this@Subscription.endpoint
            this.keys = keys
        }
    }
}