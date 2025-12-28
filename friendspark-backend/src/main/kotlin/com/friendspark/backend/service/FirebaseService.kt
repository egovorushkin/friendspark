package com.friendspark.backend.service

import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Service

/**
 * Service for interacting with Firebase functionalities.
 */
@Service
class FirebaseService {
    fun addCustomClaims(uid: String, claims: Map<String, Any>) {
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims)
    }
}