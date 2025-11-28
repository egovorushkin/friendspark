package com.friendspark.backend.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecuredController {

    @GetMapping("/secured/data")
    fun getSecuredData(): String {
        // The principal is the Firebase UID (string) set by the FirebaseTokenFilter
        val firebaseUid = (SecurityContextHolder.getContext().authentication?.principal ?: getSecuredData()) as String

        // Use the UID to fetch data specific to this user (Step 7 in diagram)
        return "Hello, secured user with UID: $firebaseUid! Your data is safe."
    }
}