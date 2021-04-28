package fr.isen.mourier.androiderestaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.isen.mourier.androiderestaurant.databinding.ActivityDetailDeviceBinding

class DetailDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var deviceName = intent.getStringExtra("deviceName")
        if(deviceName == null)
        {
            binding.titleDetailDevice.text = "No Name"
        }
        else
        {
            binding.titleDetailDevice.text = deviceName.toString()
        }
    }
}