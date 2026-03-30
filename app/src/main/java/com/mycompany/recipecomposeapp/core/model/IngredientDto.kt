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

    @Serializable
    data class Some(val amount: String, val unit: String) : Quantity() {
        override fun toString() = "$amount $unit"
    }

}

@Serializable(with = IngredientSerializer::class)
data class IngredientDto(
    @SerialName("description") val name: String,
    @Serializable val quantity: Quantity
)

fun IngredientDto.toUiModel() = IngredientUiModel(
    title = name,
    quantity = quantity
)

object IngredientSerializer : KSerializer<IngredientDto> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("IngredientDto")

    override fun deserialize(decoder: Decoder): IngredientDto {
        return try {
            val jsonElement = decoder.decodeSerializableValue(JsonElement.serializer())

            when {
                jsonElement is JsonObject -> {

                    val amountString = jsonElement["quantity"]?.jsonPrimitive?.content
                        ?: throw SerializationException("Missing quantity")

                    val amountDouble = amountString.toDoubleOrNull()

                    val unit = jsonElement["unitOfMeasure"]?.jsonPrimitive?.content
                        ?: throw SerializationException("Missing unit")

                    val name = jsonElement["description"]?.jsonPrimitive?.content ?: ""

                    if (amountDouble != null) IngredientDto(
                        name = name,
                        quantity = Quantity.Measured(amountDouble, unit)
                    )
                    else if (amountString.equals("по вкусу", ignoreCase = true)) IngredientDto(
                        name = name,
                        quantity = Quantity.ByTaste
                    )
                    else IngredientDto(
                        name = name,
                        quantity = Quantity.Some(amountString, unit)
                    )
                }

                else -> throw SerializationException("Unexpected JSON type for IngredientDto")
            }
        } catch (e: Exception) {
            IngredientDto("", quantity = Quantity.ByTaste)
        }
    }

    override fun serialize(encoder: Encoder, value: IngredientDto) {
        throw UnsupportedOperationException("Serialization is not supported")
    }
}