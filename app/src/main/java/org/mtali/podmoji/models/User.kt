package org.mtali.podmoji.models

import android.text.format.DateUtils

data class User(
    val id: String = "",
    val displayName: String = "",
    val emojis: String = "",
    private val emojiUpdatedAt: Long = -1
) {
    fun getUpdatedAt(): String {
        return if (emojiUpdatedAt == (-1).toLong()) {
            "none"
        } else {
            DateUtils.getRelativeTimeSpanString(emojiUpdatedAt).toString()
        }
    }
}