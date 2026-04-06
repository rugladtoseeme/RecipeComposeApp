package com.mycompany.recipecomposeapp.data.database.converter

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromString(str: String): List<String> = str.split("|||")

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(separator = "|||")
}