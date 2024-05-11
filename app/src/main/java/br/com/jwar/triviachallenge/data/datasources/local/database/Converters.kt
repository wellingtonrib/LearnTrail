package br.com.jwar.triviachallenge.data.datasources.local.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split("|")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString("|")
    }
}