package com.cthacademy.android.modal

import java.io.Serializable

data class UserProfile(
    val avatar: String? = null,
    val name: String? = null,
    val date: String? = null,
    val point: Long? = null,
    val rank: Long? = null,
    val perm: Long? = null
): Serializable
