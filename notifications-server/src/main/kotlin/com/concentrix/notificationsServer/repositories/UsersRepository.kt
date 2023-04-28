package com.concentrix.notificationsServer.repositories

import com.concentrix.notificationsServer.exceptions.RepositoryException
import com.concentrix.notificationsServer.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UsersRepository : JpaRepository<User, Long> {
    fun findUserById(id: Long): User {
        return findById(id).orElseThrow {
            RepositoryException("User $id not found")
        }
    }
}