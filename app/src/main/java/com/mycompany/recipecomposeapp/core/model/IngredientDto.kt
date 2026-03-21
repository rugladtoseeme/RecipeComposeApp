package com.mycompany.recipecomposeapp.core.model

import android.os.Parcelable
import com.mycompany.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.roundToInt


@Parcelize
@Serializable
sealed class Quantity : Parcelable {

    @Serializable
    data class Measured(val amount: Double, val unit: String) : Quantity() {
        override fun toString() = "${
            if (amount % 1.0 <= 0.1 || amount % 1.0 >= 0.9) {
                amount.roundToInt().toString()
            } else {
                amount.toString() 
            }
        } ${unit}"
    }

    @Serializable
    object ByTaste : Quantity() {
        override fun toString() = "по вкусу"
    }

}

@Serializable
data class IngredientDto(@SerialName("description") val name: String, @Serializable(with = QuantitySerializer::class) val quantity: Quantity)

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    quantity = quantity
)

object QuantitySerializer : KSerializer<Quantity> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Quantity")

    override fun deserialize(decoder: Decoder): Quantity {
        return try {
            val jsonElement = decoder.decodeSerializableValue(JsonElement.serializer())

            when {
                jsonElement is JsonObject -> {
                    val amount = jsonElement["amount"]?.jsonPrimitive?.double
                        ?: throw SerializationException("Missing amount")
                    val unit = jsonElement["unit"]?.jsonPrimitive?.content
                        ?: throw SerializationException("Missing unit")
                    Quantity.Measured(amount, unit)
                }

                jsonElement is JsonPrimitive && jsonElement.isString -> {
                    val stringValue = jsonElement.content
                    when (stringValue.lowercase()) {
                        "по вкусу", "to taste" -> Quantity.ByTaste
                        else -> {
                            parseQuantityFromString(stringValue) ?: Quantity.ByTaste
                        }
                    }
                }

                else -> throw SerializationException("Unexpected JSON type for Quantity")
            }
        } catch (e: Exception) {
            Quantity.ByTaste
        }
    }

    private fun parseQuantityFromString(str: String): Quantity.Measured? {
        val regex = Regex("""^(\d+(?:\.\d+)?)\s*([а-яa-z]+)$""")
        val match = regex.find(str.trim())
        return match?.let {
            val amount = it.groupValues[1].toDoubleOrNull() ?: return null
            val unit = it.groupValues[2]
            Quantity.Measured(amount, unit)
        }
    }

    override fun serialize(encoder: Encoder, value: Quantity) {
        when (value) {
            is Quantity.Measured -> {
                encoder.encodeSerializableValue(
                    JsonElement.serializer(),
                    JsonObject(mapOf(
                        "amount" to JsonPrimitive(value.amount),
                        "unit" to JsonPrimitive(value.unit)
                    ))
                )
            }
            Quantity.ByTaste -> {
                encoder.encodeSerializableValue(
                    JsonElement.serializer(),
                    JsonPrimitive("по вкусу")
                )
            }
        }
    }

}