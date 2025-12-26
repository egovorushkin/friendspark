package com.friendspark.backend.controller

import com.friendspark.backend.dto.user.UserDetailsDTO
import com.friendspark.backend.dto.user.UserUpdateDTO
import com.friendspark.backend.entity.User
import com.friendspark.backend.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<UserDetailsDTO> = userService.getAllUsers()

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal user: User): Map<String, Any?> {
        return mapOf(
            "id" to user.id,
            "email" to user.email
        )
    }

    @GetMapping("/{id}")
    fun getUserDetails(@PathVariable id: UUID): UserDetailsDTO? = userService.getUserById(id)

    @GetMapping("/nearby")
    fun findNearbyUsers(@RequestParam geohashPrefix: String): List<UserDetailsDTO> =
        userService.findNearbyUsers(geohashPrefix)

    @PutMapping("/me")
    fun updateProfile(
        @AuthenticationPrincipal uid: String,
        @RequestBody updateDTO: UserUpdateDTO
    ): UserDetailsDTO = userService.updateUserProfile(uid, updateDTO)
}