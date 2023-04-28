package com.concentrix.notificationsServer.exceptions

import java.lang.Exception

data class RepositoryException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

data class NotificationNotSupportedException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)