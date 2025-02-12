package fr.isen.mourier.androiderestauran

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.mourier.androiderestaurant.BLEService
import fr.isen.mourier.androiderestaurant.R

class DetailDeviceAdapter(
    private val gatt: BluetoothGatt?,
    private val serviceList: MutableList<BLEService>, private val context: Context
) :
    ExpandableRecyclerViewAdapter<DetailDeviceAdapter.ServiceViewHolder, DetailDeviceAdapter.CharacteristicViewHolder>(
        serviceList
    ) {
    private var enabled : Boolean = false
    class ServiceViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceUUID: TextView = itemView.findViewById(R.id.serviceInfo)
        private val serviceArrow = itemView.findViewById<ImageView>(R.id.serviceArrow)

        override fun expand() {
            super.expand()
            serviceArrow.animate().rotation(-180f).setDuration(400L).start()
        }

        override fun collapse() {
            super.collapse()
            serviceArrow.animate().rotation(0f).setDuration(400L).start()
        }
    }
    class CharacteristicViewHolder(itemView: View) : ChildViewHolder(itemView) {
        val characteristicUUID: TextView = itemView.findViewById(R.id.uuid)
        val characteristicProperties: TextView = itemView.findViewById(R.id.propriete)
        val characteristicValue: TextView = itemView.findViewById(R.id.valeur)
        val characteristicNom : TextView = itemView.findViewById(R.id.characteristicName)
        val characteristicReadAction: Button = itemView.findViewById(R.id.buttonLecture)
        val characteristicWriteAction: Button = itemView.findViewById(R.id.buttonEcriture)
        val characteristicNotifyAction: Button = itemView.findViewById(R.id.buttonNotification)
    }
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cell_service, //correpond au parent, expandable de base
                parent,
                false
            )
        )
    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder =
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cell_caracteristic,
                parent,
                false
            )
        )
    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic: BluetoothGattCharacteristic = (group as BLEService).items[childIndex]!!
        val title = BleUuidAttribut.getBLEAttributeFromUUID(group.title).title
        val uuid = "UUID : ${characteristic?.uuid}"
        holder.characteristicUUID.text = uuid
        holder.characteristicNom.text = title
        holder.characteristicProperties.text = "proprietes : ${proprieties(characteristic.properties)}"
        holder.characteristicWriteAction.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            val editView = View.inflate(context, R.layout.saisie , null)
            alertDialog.setView(editView)
            alertDialog.setPositiveButton("Valider") { _, _ ->
                val popup : TextView = editView.findViewById(R.id.saisieText)
                val texte = popup.text.toString().toByteArray()
                characteristic.setValue(texte)
                val result1 = gatt?.writeCharacteristic(characteristic)
                Log.d("erreur : ", result1.toString())
                val result = gatt?.readCharacteristic(characteristic)
                Log.d("erreur : ", result.toString())
            }
            alertDialog.setNegativeButton("Annuler") { alertDialog, _ -> alertDialog.cancel() }
            alertDialog.create()
            alertDialog.show()
        }
        holder.characteristicReadAction.setOnClickListener {
            val result = gatt?.readCharacteristic(characteristic)
            if (characteristic.value != null) {
                val texteRecu = String(characteristic.value)
                holder.characteristicValue.text = "Valeur : ${ texteRecu}"
            }
        }
        holder.characteristicNotifyAction.setOnClickListener {
            if (!enabled) {
                enabled = true
                gatt?.setCharacteristicNotification(characteristic, true)
                if (characteristic.descriptors.size > 0) {
                    val descriptors = characteristic.descriptors
                    for (descriptor in descriptors) {
                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            descriptor.value =
                                if (enabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                        } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            descriptor.value =
                                if (enabled) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                        }
                        gatt?.writeDescriptor(descriptor)
                    }
                }
            } else {
                enabled = false
                gatt?.setCharacteristicNotification(characteristic, false)
            }
        }
        if (characteristic.uuid.toString() == BleUuidAttribut.getBLEAttributeFromUUID(characteristic.uuid.toString()).uuid && enabled) {
            if (characteristic.value == null) {
                holder.characteristicValue.text = "Valeur : 0"
            }
            else {
                var temp = (characteristic.value).toString()
                holder.characteristicValue.text = "Valeur : ${byteArrayToHexString(characteristic.value)}"
            }
        }

    }
    private fun byteArrayToHexString(array: ByteArray): String {
        val result = StringBuilder(array.size * 2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte)
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1)
        return result.toString()
    }
    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = BleUuidAttribut.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text = title
        holder.serviceUUID.text = group.title
    }

    private fun proprieties(property: Int): StringBuilder {
        val sb = StringBuilder()
        if (property and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            sb.append("Ecrire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            sb.append(" Lire")
        }
        if (property and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            sb.append(" Notifier")
        }
        if (sb.isEmpty()) sb.append("Aucune")
        return sb
    }
    enum class BleUuidAttribut(val uuid: String, val title: String) {
        ACCES_GENERIQUE("00001800-0000-1000-8000-00805f9b34fb", "Accès générique"),
        ATTRIBUT_GENERIQUE("00001801-0000-1000-8000-00805f9b34fb", "Attribut générique"),
        SERVICE_SPECIFIQUE("466c1234-f593-11e8-8eb2-f2801f1b9fd1", "Service Spécifique "),
        SERVICE_SPE2("466c9abc-f593-11e8-8eb2-f2801f1b9fd1", "Service Spécifique "),
        UNKNOW_SERVICE("", "Inconnu");
        companion object {
            fun getBLEAttributeFromUUID(uuid: String) = values().firstOrNull {
                it.uuid == uuid
            } ?: UNKNOW_SERVICE
        }
    }
}