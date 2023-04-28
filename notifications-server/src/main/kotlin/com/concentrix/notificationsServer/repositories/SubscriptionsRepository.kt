package com.concentrix.notificationsServer.repositories

import com.concentrix.notificationsServer.models.Subscription
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionsRepository : JpaRepository<Subscription, Long>