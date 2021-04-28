package fr.isen.mourier.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener
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
        binding.carouselView.pageCount = dish.images.size

        var imageListener : ImageListener = object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView){
                if(dish.images[0].isNullOrEmpty()){
                    Picasso.get().load("http://www.essdetbol.ru/images/no_photo.png").into(imageView)
                }else {
                    //Picasso.get().load(dish.images[0]).into(binding.detailImage)
                    Picasso.get().load(dish.images[position]).into(imageView)
                }
            }
        }

        binding.carouselView.setImageListener(imageListener)

        var nombre = 0
        var prix : Float = dish.prices[0].price.toFloat()
        var prixTotal : Float = 0F

        binding.boutonPlus.setOnClickListener {
            nombre += 1
            prixTotal = nombre*prix
            binding.quantite.text = nombre.toString()
            binding.total.text = "Total = ${prixTotal} €"
        }

        binding.boutonMoins.setOnClickListener {
            nombre -= 1
            if ( nombre < 0) { nombre = 0 }
            prixTotal = nombre*prix
            binding.quantite.text = nombre.toString()
            binding.total.text = "Total = ${prixTotal} €"
        }
    }
}