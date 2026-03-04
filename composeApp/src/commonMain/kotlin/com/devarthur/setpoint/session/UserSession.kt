package com.devarthur.setpoint.session

import com.devarthur.setpoint.domain.Role

data class UserSession(
    val userId: String,
    val role: Role,
)
