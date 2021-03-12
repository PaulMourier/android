package fr.isen.mourier.androiderestaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Item (
        @SerializedName("id") val id: Int,
        @SerializedName("name_fr") val name: String,
        @SerializedName("images") private val images: List<String>,
        @SerializedName("prices") private val prices: List<Price>
): Serializable