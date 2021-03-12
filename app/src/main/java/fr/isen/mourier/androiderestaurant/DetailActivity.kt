package fr.isen.mourier.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import fr.isen.mourier.androiderestaurant.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("items")
        binding.detailTitle.text = name
        //val nom =  intent.getStringExtra("dish")
        //Toast.makeText(this, intent.getStringExtra("name") ?:"", Toast.LENGTH_LONG).show()
        //binding.detailTitle.text = nom

    }
}