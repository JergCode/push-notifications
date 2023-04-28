package com.concentrix.notificationsServer.models

data class Notification(
    val message: String,
    val type: String? = null,
)

data class Message(
    val title: String,
    val body: String,
)