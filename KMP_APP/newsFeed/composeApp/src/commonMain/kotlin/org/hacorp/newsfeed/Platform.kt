package org.hacorp.newsfeed

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform