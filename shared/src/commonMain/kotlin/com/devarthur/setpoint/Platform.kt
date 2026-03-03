package com.devarthur.setpoint

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform