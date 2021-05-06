package fr.isen.mourier.androiderestaurant

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mourier.androiderestauran.DetailDeviceAdapter
import fr.isen.mourier.androiderestaurant.databinding.ActivityDetailDeviceBinding

class DetailDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDeviceBinding
    var bluetoothGatt: BluetoothGatt? = null
    private  lateinit var ls : MutableList<BLEService>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice>("ble_device")
        binding.titleDetailDevice.text = device?.name ?: "Device Unknown"
        binding.statusDetailDevice.text = getString(R.string.ble_device_status, getString(R.string.ble_device_status_connecting))

        connectToDevice(device)
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                connectionStateChange(newState, gatt)

            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    binding.listService.adapter = DetailDeviceAdapter(
                        gatt,
                        gatt?.services?.map {
                            BLEService(
                                it.uuid.toString(),
                                it.characteristics
                            )
                        }?.toMutableList() ?: arrayListOf(), this@DetailDeviceActivity
                    )
                    binding.listService.layoutManager = LinearLayoutManager(this@DetailDeviceActivity)
                }
            }


            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
            }
        })
    }

    private fun connectionStateChange(newState: Int, gatt: BluetoothGatt?) {
        BLEConnexionState.getBLEConnexionStateFromState(newState)?.let {
            runOnUiThread {
                binding.statusDetailDevice.text =
                    getString(R.string.ble_device_status, getString(it.text))
            } //si non nul on fait ca
            if (it.state == BLEConnexionState.STATE_CONNECTED.state) {
                gatt?.discoverServices()
            }
        }
    }
}


