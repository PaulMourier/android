package fr.isen.mourier.androiderestaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category (
        @SerializedName("name_fr") val name : String,
        @SerializedName("items") val items : List<Item>
):Serializable