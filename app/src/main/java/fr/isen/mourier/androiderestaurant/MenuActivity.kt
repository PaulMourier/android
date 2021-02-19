package fr.isen.mourier.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import fr.isen.mourier.androiderestaurant.databinding.ActivityHomeBinding
import fr.isen.mourier.androiderestaurant.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra("category")
        binding.categoryTitle.text = categoryName
        binding.listeMenu.adapter = CategoryAdapter(arrayOf("salade"))
    }
}