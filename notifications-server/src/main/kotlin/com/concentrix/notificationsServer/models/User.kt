package com.concentrix.notificationsServer.models

import javax.persistence.*

@Entity
@Table(name = "USERS")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val name: String = "",
    @Column(name = "last_name") val lastName: String = "",
    val email: String = "",
    @OneToMany(mappedBy = "user")
    val subscriptions: List<Subscription>? = null,
    @ManyToMany(mappedBy = "users")
    val notificationTypes: List<NotificationType>? = null,
) {
    override fun toString(): String {
        return "User(id=$id, name='$name', lastName='$lastName', email='$email', subscriptions=${subscriptions?.map { it.endpoint }}, notificationTypes=${notificationTypes?.map { it.type }})"
    }
}
