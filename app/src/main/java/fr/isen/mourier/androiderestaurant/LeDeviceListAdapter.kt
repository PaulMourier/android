package fr.isen.mourier.androiderestaurant

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.mourier.androiderestaurant.databinding.CellCategoryBinding
import fr.isen.mourier.androiderestaurant.databinding.CellDeviceBinding

class LeDeviceListAdapter (private val devices: MutableList<BluetoothDevice>, private val onDeviceClickListener: (BluetoothDevice) -> Unit): RecyclerView.Adapter<LeDeviceListAdapter.DeviceViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = CellDeviceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        if(devices[position].name == null)
        {
            holder.addressDevice.text = devices[position].toString()
            holder.title.text = "No name"
        }
        else
        {
            holder.title.text = devices[position].name
            holder.addressDevice.text = devices[position].toString()
        }
        holder.layout.setOnClickListener{
            onDeviceClickListener(devices[position])
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun addDevice(device: BluetoothDevice)
    {
        if (!devices.contains(device)){
            devices.add(device)}
    }

    class DeviceViewHolder(binding: ConstraintLayout) : RecyclerView.ViewHolder(binding) {
        val title: TextView = binding.findViewById(R.id.cellDeviceTitle)
        val addressDevice: TextView = binding.findViewById(R.id.cellDeviceAddress)
        val layout = binding.findViewById<View>(R.id.cellLayout)
    }
}