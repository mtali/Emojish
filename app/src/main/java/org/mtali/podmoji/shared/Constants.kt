package org.mtali.podmoji.shared

val VALID_EMOJI_CHAR_TYPES = listOf(
    Character.NON_SPACING_MARK, // 6
    Character.DECIMAL_DIGIT_NUMBER, // 9
    Character.LETTER_NUMBER, // 10
    Character.OTHER_NUMBER, // 11
    Character.SPACE_SEPARATOR, // 12
    Character.FORMAT, // 16
    Character.SURROGATE, // 19
    Character.DASH_PUNCTUATION, // 20
    Character.START_PUNCTUATION, // 21
    Character.END_PUNCTUATION, // 22
    Character.CONNECTOR_PUNCTUATION, // 23
    Character.OTHER_PUNCTUATION, // 24
    Character.MATH_SYMBOL, // 25
    Character.CURRENCY_SYMBOL, //26
    Character.MODIFIER_SYMBOL, // 27
    Character.OTHER_SYMBOL // 28
).map { it.toInt() }.toSet()