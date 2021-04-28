package fr.isen.mourier.androiderestaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Item (
        @SerializedName("id") val id: String,
        @SerializedName("name_fr") val name: String,
        @SerializedName("images")  val images: List<String>,
        @SerializedName("ingredients") val ingredients: List<Ingredient>,
        @SerializedName("prices") val prices: List<Price>
): Serializable {
    fun getAffichagePrice() = prices[0].price+"€"
    fun getFirstPicture() = if(images.isNotEmpty() && images[0].isNotEmpty()) {
        images[0]
    } else {
        null
    }
}