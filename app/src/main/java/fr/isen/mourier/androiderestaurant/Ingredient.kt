package fr.isen.mourier.androiderestaurant

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Ingredient (
        @SerializedName("name_fr") val name: String
): Serializable