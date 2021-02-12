package fr.isen.mourier.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import fr.isen.mourier.androiderestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val monIntent : Intent = Intent(this, EntreesActivity::class.java)

        binding.entreesAction.setOnClickListener{
            //l'utilisateur a cliqué sur entrees
            val text = "Entrées"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            startActivity(monIntent)
        }
        binding.platsAction.setOnClickListener{
            //l'utilisateur a cliqué sur plats

            val text = "Plats"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
        binding.dessertsAction.setOnClickListener{
            //l'utilisateur a ckiqué sur desserts
            val text = "Desserts"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }

    }
}