package org.mtali.podmoji.shared

fun isInputEmoji(input: String?): Boolean {
    if (input == null)
        return false
    for (inputChar in input) {
        val type = Character.getType(inputChar)
        if (!VALID_EMOJI_CHAR_TYPES.contains(type)) return false
    }
    return true
}