package com.concentrix.notificationsServer.repositories

import com.concentrix.notificationsServer.models.NotificationType
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationTypesRepository : JpaRepository<NotificationType, Long> {
    fun findByType(type: String): NotificationType?
}