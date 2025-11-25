package com.friendspark

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform