package com.sillymarvellouschad.medguard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform