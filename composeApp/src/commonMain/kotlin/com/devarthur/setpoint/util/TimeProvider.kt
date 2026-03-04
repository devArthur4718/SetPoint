package com.devarthur.setpoint.util

expect object TimeProvider {
    fun currentTimeMillis(): Long
}
