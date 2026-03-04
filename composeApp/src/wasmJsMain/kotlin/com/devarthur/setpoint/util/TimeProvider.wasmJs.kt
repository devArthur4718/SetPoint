package com.devarthur.setpoint.util

import kotlin.js.Date

actual object TimeProvider {
    actual fun currentTimeMillis(): Long = Date().getTime().toLong()
}
