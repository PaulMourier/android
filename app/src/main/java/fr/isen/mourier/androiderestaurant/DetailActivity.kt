package fr.isen.mourier.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.squareup.picasso.Picasso
import fr.isen.mourier.androiderestaurant.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dish = intent.getSerializableExtra("items") as Item
        binding.detailTitle.text = dish.name
        //binding.detailIngredients.text = dish?.ingredients?.map{it.name}.toString()
        binding.detailIngredients.text = dish.ingredients.map{ it.name }.joinToString(" | ")
        //binding.detailIngredients.text = dish?.ingredients[0].name
        //Log.i("Ingredients", "${dish?.ingredients?.map{ it.name }.joinToString(" | ")}")

        if(dish.images[0].isNullOrEmpty()){
            Picasso.get().load("https://img.icons8.com/carbon-copy/2x/no-image.png").into(binding.detailImage)
        }else {
            Picasso.get().load(dish.images[0]).into(binding.detailImage)
        }
    }
}