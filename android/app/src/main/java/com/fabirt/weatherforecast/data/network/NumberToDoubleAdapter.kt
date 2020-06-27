package com.fabirt.weatherforecast.data.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.Exception
import java.lang.reflect.Type

class NumberToDoubleAdapter : JsonDeserializer<Double> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Double {
        return try {
            if (json != null && json.isJsonPrimitive) {
                val number = json.asNumber
                number.toDouble()
            } else {
                0.0
            }
        } catch (e: Exception) {
            0.0
        }
    }
}