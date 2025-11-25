package com.friendspark.backend.service

import com.friendspark.backend.entity.User
import com.friendspark.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    fun getAllUsers(): List<User> = userRepository.findAll()
    fun getUserById(id: UUID): User? = userRepository.findById(id).orElse(null)
    fun createUser(user: User): User = userRepository.save(user)
    fun deleteUser(id: UUID) = userRepository.deleteById(id)
}
