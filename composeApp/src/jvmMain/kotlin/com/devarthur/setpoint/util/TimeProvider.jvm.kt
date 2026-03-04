package com.devarthur.setpoint.util

actual object TimeProvider {
    actual fun currentTimeMillis(): Long = System.currentTimeMillis()
}
