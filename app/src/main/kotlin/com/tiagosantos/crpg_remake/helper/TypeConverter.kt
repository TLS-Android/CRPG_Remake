package com.tiagosantos.crpg_remake.helper

object TypeConverter {
    fun toString(value: Int): String = value.toString()

    fun toInt(value: String?): Int {
        return if (!value.isNullOrEmpty()) value.toInt() else 0
    }
}
