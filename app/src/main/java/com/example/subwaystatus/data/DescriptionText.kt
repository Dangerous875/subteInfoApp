package com.example.subwaystatus.data

data class DescriptionText(val translation: Array<Translation>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DescriptionText

        if (!translation.contentEquals(other.translation)) return false

        return true
    }

    override fun hashCode(): Int {
        return translation.contentHashCode()
    }
}
