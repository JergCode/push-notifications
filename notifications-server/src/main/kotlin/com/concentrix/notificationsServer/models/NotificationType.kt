package com.concentrix.notificationsServer.models

import javax.persistence.*

@Entity
@Table(name = "NOTIFICATION_TYPES")
data class NotificationType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val type: String = "",
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "NOTIFICATIONS_ALLOWED",
        joinColumns = [JoinColumn(name = "notification_type_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val users: List<User>? = null
)
