package com.example.mytestapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mytestapplication.databinding.FragmentFirstBinding
import com.ido.ble.BLEManager
import com.ido.ble.LocalDataManager
import com.ido.ble.bluetooth.connect.ConnectFailedReason
import com.ido.ble.bluetooth.device.BLEDevice
import com.ido.ble.callback.BindCallBack
import com.ido.ble.callback.ConnectCallBack

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val macHonor = "0c:b7:89:c2:63:33"
    private val macB9 = "c2:48:a2:4c:44:69" //
    private val macAmazon = "f7:9c:57:5e:0c:32" // ID155

    private val bindCB = object : BindCallBack.ICallBack{
        override fun onSuccess() {
            Log.d("TAG", "bindCB onSuccess")
        }

        override fun onFailed(p0: BindCallBack.BindFailedError?) {
            Log.d("TAG", "bindCB onFailed $p0")
        }

        override fun onCancel() {
            Log.d("TAG", "bindCB onCancel")
        }

        override fun onReject() {
            Log.d("TAG", "bindCB onReject")
        }

        override fun onNeedAuth(p0: Int) {
            Log.d("TAG", "bindCB onNeedAuth $p0")
        }

    }
    private val deviceCB = object : ConnectCallBack.ICallBack {
        override fun onConnectStart(p0: String?) {
            Log.d("TAG", "deviceCB onConnectStart $p0")
        }

        override fun onConnecting(p0: String?) {
            Log.d("TAG", "deviceCB onConnecting $p0")
        }

        override fun onRetry(p0: Int, p1: String?) {
            Log.d("TAG", "deviceCB onRetry $p0 $p1")
        }

        override fun onConnectSuccess(mac: String?) {
            val device = LocalDataManager.getCurrentDeviceInfo()

            if(!BLEManager.isBind()){
                BLEManager.bind()
            }
            Log.d("TAG", "deviceCB onConnectSuccess $mac $device")
        }

        override fun onConnectFailed(p0: ConnectFailedReason?, p1: String?) {
            Log.d("TAG", "deviceCB onConnectFailed $p0 $p1")
        }

        override fun onConnectBreak(p0: String?) {
            Log.d("TAG", "deviceCB onConnectBreak $p0")
        }

        override fun onInDfuMode(p0: BLEDevice?) {
            Log.d("TAG", "deviceCB onInDfuMode $p0")
        }

        override fun onDeviceInNotBindStatus(p0: String?) {
            Log.d("TAG", "onDeviceInNotBindStatus $p0")
        }

        override fun onInitCompleted(p0: String?) {
            Log.d("TAG", "onInitCompleted $p0")
            if(!BLEManager.isBind()){
                BLEManager.bind()
            }

        }

    }
    private val scanCB = object : com.ido.ble.callback.ScanCallBack.ICallBack {
        override fun onStart() {
            Log.d("TAG", "scanCB onStart")
        }

        override fun onFindDevice(p0: BLEDevice?) {
            Log.d("TAG", "scanCB onFindDevice $p0")
            if(macB9.toUpperCase() == p0?.mDeviceAddress) {
                BLEManager.stopScanDevices()
                BLEManager.connect(p0)
            }
        }

        override fun onScanFinished() {
            Log.d("TAG", "scanCB onScanFinished")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BLEManager.registerScanCallBack (scanCB)
        BLEManager.registerConnectCallBack(deviceCB)
        BLEManager.registerBindCallBack(bindCB)

        binding.buttonFirst.setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            if(BLEManager.isConnected()){
                BLEManager.unbind();
            } else {
                BLEManager.forceUnbind();
                BLEManager.disConnect()
                // App need to delete the device binding info from the device list
            }
            BLEManager.startScanDevices()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BLEManager.unregisterScanCallBack (scanCB)
        BLEManager.unregisterConnectCallBack(deviceCB)
        BLEManager.unregisterBindCallBack(bindCB)
        _binding = null
    }
}