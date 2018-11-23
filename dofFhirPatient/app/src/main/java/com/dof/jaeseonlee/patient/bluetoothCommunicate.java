package com.dof.jaeseonlee.patient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.logging.Handler;

public class bluetoothCommunicate {
    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning;
    private Handler handler;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable){
      /*  if(enable){
            handler.postDelayed(new Runnable(){

                @Override
                public void run() {
                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            },SCAN_PERIOD);

            scanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        }else{
            scanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }*/
    }

}
