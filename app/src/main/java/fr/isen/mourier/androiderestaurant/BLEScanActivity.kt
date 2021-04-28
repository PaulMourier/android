package fr.isen.mourier.androiderestaurant

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mourier.androiderestaurant.databinding.ActivityBLEScanBinding



class BLEScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBLEScanBinding
    private var isScanning = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private val handler = Handler()

    private val SCAN_PERIOD: Long = 10000
    private lateinit var adapter: LeDeviceListAdapter
    var listeDevices: MutableList<BluetoothDevice> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBLEScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter = getSystemService(BluetoothManager::class.java)?.adapter

        startBLEIfPossible()
        isDeviceHasBLESupport()

        binding.bleScanPlayPauseAction.setOnClickListener {
            togglePlayPauseAction()
        }
    }

    private fun startBLEIfPossible() {
        when {
            !isDeviceHasBLESupport() || bluetoothAdapter == null -> {
                Toast.makeText(
                    this,
                    "Cet appareil n'est pas compatible avec le Bluetooth Low Energy",
                    Toast.LENGTH_SHORT
                ).show()
            }
            !(bluetoothAdapter?.isEnabled ?: false)-> { //bluetoothAdapter.isEnabled est a false donc pas activé
                //je dois activer le bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE) //lance une activité qui permet de gerer l'activation du bluetooth
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT) //on attend un resultat quand il a fini
            }
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
            }
            else -> {
                bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
                loadDevices(listeDevices)
                //scanLeDevice()
                //Log.i("test", "${ListDevices}")
                //youpi on peut faire du ble
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK)
            startBLEIfPossible()
    }

    private fun isDeviceHasBLESupport(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    private fun togglePlayPauseAction() {
        isScanning = !isScanning
        if(isScanning) {
            binding.textBle.text = getString(R.string.textBlePause)
            binding.bleScanPlayPauseAction.setImageResource(R.drawable.ic_baseline_pause_24)
            binding.progressBar.visibility = View.VISIBLE
            binding.divider.visibility = View.INVISIBLE
            scanLeDevice()

        }
        else {
            binding.textBle.text = getString(R.string.textBlePlay)
            binding.bleScanPlayPauseAction.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.progressBar.visibility = View.INVISIBLE
            binding.divider.visibility = View.VISIBLE
        }
    }

    private fun loadDevices(listDevices: MutableList<BluetoothDevice>?){
        listDevices?.let{
            adapter = LeDeviceListAdapter(it) {device ->
                val intent = Intent(this, DetailDeviceActivity::class.java)
                intent.putExtra("deviceName",device.name)
                startActivity(intent)
            }
            binding.recyclerDevices.layoutManager = LinearLayoutManager(this)
            binding.recyclerDevices.adapter = adapter
        }
    }



    //private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    //private var scanning = false
    //private val handler = Handler()
    //private val ListDevices = mutableListOf<BluetoothDevice>()

    // Stops scanning after 10 seconds.
    //private val SCAN_PERIOD: Long = 10000

    private fun scanLeDevice() {
        bluetoothLeScanner?.let { scanner ->
            if (!scanning) { // Stops scanning after a pre-defined scan period.
                handler.postDelayed({
                    scanning = false
                    scanner.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                scanning = true
                scanner.startScan(leScanCallback)
            } else {
                scanning = false
                scanner.stopScan(leScanCallback)
                //Log.i("test", "okokokokokok")
                //Log.i("mytag", "${ListDevices}")
            }
        }
    }

    //private val leDeviceListAdapter = LeDeviceListAdapter(ListDevices)
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            //leDeviceListAdapter.addDevice(result.device)
            //leDeviceListAdapter.notifyDataSetChanged()
            adapter.addDevice(result.device)
            adapter.notifyDataSetChanged()
        }
    }


    companion object {
        const private val REQUEST_ENABLE_BT = 33
        const private val REQUEST_PERMISSION_LOCATION = 22
    }
}