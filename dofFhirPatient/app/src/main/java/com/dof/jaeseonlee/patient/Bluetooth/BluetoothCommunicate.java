package com.dof.jaeseonlee.patient.Bluetooth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dof.jaeseonlee.patient.Fragments.HomeFragment;
import com.dof.jaeseonlee.patient.R;
import com.dof.jaeseonlee.patient.DataProcess.SQLiteClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 이재선 on 2018-11-11.
 * bluetooth Communication 코드 참조 : ChangYeop-Yang/Android-Health1street
 */
public class BluetoothCommunicate extends AsyncTask<Integer, Void, String> {

    private static final int HRM_CHECK  = 0;
    private static final int DEVICE_CONNECTION = 1;


    /* MARK - : Xiaomi Mi-Band Basic UUID */
    public static final UUID BASIC_SERVICE = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");

    /* MARK - : Xiaomi Mi-Band AlertNotification UUID */
    public static final UUID ALERT_NOTIFICATION_SERVICE = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    public static final UUID ALERT_NOTIFICATION_CHARACTERISTIC = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    /* MARK - : Xiaomi Mi-Band HeartRate UUID */
    public static final UUID HEART_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID HEART_MEASUREMENT_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final UUID HEART_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID HEART_CONTROL_CHARACTERISTIC = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");


    /* MARK - : String */
    private final static String MI_NAME = "MI Band 2";

    /* MARK - : Context */
    private Context mContext = null;

    private View mView;

    private SQLiteClass sqLiteClass = null;


    /* MARK - : Bluetooth Instance */
    private BluetoothGatt mDeviceBluetoothGatt = null;

    /* MARK - : Button */
    private Button aButtonList[] = null;

    /* MARK - : Boolean */
    private Boolean isVibrate = false;
    private Boolean isListeningHeartRate = false;

    private TextView vHomeHRMTextView;
    private ImageView vHomeHRMStatusImaveView;

    private int mHeartBeatRate = 0;

    private ProgressDialog mProgressDialog = null;

    /* MARK - : Mi-Band Manager Creator */
    public BluetoothCommunicate(final Context mContext, final View view) {
        this.mContext = mContext;
        this.mView = view;
    }

    /* MARK - : Start Heart Beat Rate Method */
    public void startScanHeartRate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(HEART_SERVICE)
                .getCharacteristic(HEART_CONTROL_CHARACTERISTIC);
        bchar.setValue(new byte[]{21, 2, 1});

        mDeviceBluetoothGatt.writeCharacteristic(bchar);
    }

    /* MARK - : Set Listen Heart Rate Method */
    public void listenHeartRate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(HEART_SERVICE)
                .getCharacteristic(HEART_MEASUREMENT_CHARACTERISTIC);

        mDeviceBluetoothGatt.setCharacteristicNotification(bchar, true);

        final BluetoothGattDescriptor descriptor = bchar.getDescriptor(HEART_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        mDeviceBluetoothGatt.writeDescriptor(descriptor);

        isListeningHeartRate = true;
    }

    /* MARK - : Start Mi-Band Vibrate Method */
    public Boolean startBandVibrate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(ALERT_NOTIFICATION_SERVICE)
                .getCharacteristic(ALERT_NOTIFICATION_CHARACTERISTIC);

        bchar.setValue(new byte[]{2});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar))
        { Toast.makeText(mContext, "Failed start vibrate", Toast.LENGTH_SHORT).show(); return false; }

        return true;
    }

    /* MARK - : Stop Mi-Band Vibrate Method */
    public Boolean stopBandVibrate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(ALERT_NOTIFICATION_SERVICE)
                .getCharacteristic(ALERT_NOTIFICATION_CHARACTERISTIC);

        bchar.setValue(new byte[]{0});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar))
        { Toast.makeText(mContext, "Failed stop vibrate", Toast.LENGTH_SHORT).show(); return true; }

        return false;
    }

    /* TODO - : BluetoothGattCallback Method */
    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.v("hihihi", "onConnectionStateChange");

            switch (newState)
            {
                case (BluetoothProfile.STATE_CONNECTED) :
                {
                    mDeviceBluetoothGatt.discoverServices();
                    break;
                }
                case (BluetoothProfile.STATE_DISCONNECTED) :
                {
                    mDeviceBluetoothGatt.disconnect();
                    break;
                }
            }
        }
        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.v("hihihi", "onServicesDiscovered");
            listenHeartRate();
        }

        @Override
        public void onCharacteristicChanged(final BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            /* Heart Beat Rate */
            final String mUUID = characteristic.getUuid().toString();
            if (mUUID.equals(HEART_MEASUREMENT_CHARACTERISTIC.toString())) {

                int value = 0;//convertByteToInt(characteristic.getValue());
                for(int i=0; i<characteristic.getValue().length; i++) { value = (value << 8) | characteristic.getValue()[i]; }
                mHeartBeatRate = value;
                HomeFragment.hrm = mHeartBeatRate;

            }
        }
    };


    @Override
    protected void onPreExecute(){
        sqLiteClass =  new SQLiteClass(mContext, "DataSet.db", null, 1);
        vHomeHRMTextView = (TextView)mView.findViewById(R.id.homeHRMView);
        vHomeHRMStatusImaveView = (ImageView)mView.findViewById(R.id.homeStatusPicture);
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("작업 처리중...");
        mProgressDialog.show();

        super.onPreExecute();;
    }


    @Override
    protected String doInBackground(Integer... integers) {
        Log.e("doinBackbround","읭 " + integers);
        /* POINT - : Check Heart Beat Rate */
        if(integers[0] == HRM_CHECK){
            if (!isListeningHeartRate)
            {
                return "SCAN_HRM_ERROR";
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("심장박동수 측정");
            alertDialogBuilder.setMessage("Heart Beat Rate(=HRM)을 측정하고 있습니다.").setPositiveButton("확인",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.show();
            startScanHeartRate();
            return "SCAN_HRM_OK";
        }

        /* POINT - Connection Check */
        else {
            if (!isListeningHeartRate) {
                /* POINT - : Bluetooth Adapter */
                final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                final Set<BluetoothDevice> mDevice = mBluetoothAdapter.getBondedDevices();

                /* POINT - : Connect Mi-Band */
                for (final BluetoothDevice mBluetooth : mDevice) {
                    if (mBluetooth.getName() != null & mBluetooth.getName().contains(MI_NAME)) {
                        mDeviceBluetoothGatt = mBluetooth.connectGatt(mContext, true, mBluetoothGattCallback);
                    }
                }
                if(!isListeningHeartRate)
                    return "UNABLE_CONNECT_DEVICE";
                else return "CONNECT_DEVICE";
            }
        }

        return "CONNECTED_ALREADY";
    }


    @Override
    protected void onPostExecute(String strings) {
        mProgressDialog.dismiss();
        Log.e("bluetoothCommunication", strings);


        if(strings.equals("SCAN_HRM_OK")){
            vHomeHRMTextView.setText(Integer.toString(mHeartBeatRate));

            if (mHeartBeatRate > 50 && mHeartBeatRate < 100) {
                Glide.with(mView).load(R.drawable.normal_big).into(vHomeHRMStatusImaveView);
            }
            else if (mHeartBeatRate != 0 ) {
                Glide.with(mView).load(R.drawable.danger_big).into(vHomeHRMStatusImaveView);
            }
            else {
                Glide.with(mView).load(R.drawable.none).into(vHomeHRMStatusImaveView);
            }
            Toast.makeText(mContext, "HRM = " + mHeartBeatRate, Toast.LENGTH_SHORT).show();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            sqLiteClass.addDate(mHeartBeatRate,simpleDateFormat.format(new Date()));
        }

        else if(strings.equals("SCAN_HRM_ERROR")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("ERROR SCAN HRM");
            alertDialogBuilder.setMessage("Heart Beat Rate(=HRM)을 측정할 수 없습니다.\n연결상태를 확인해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialogBuilder.show();
            Toast.makeText(mContext, "심박수를 측정할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        else if(strings.equals("CONNECT_DEVICE")){
            Toast.makeText(mContext, "Mi-band와 연결되었습니다.", Toast.LENGTH_SHORT).show();
        }

        else if(strings.equals("UNABLE_CONNECT_DEVICE")){
            Toast.makeText(mContext, "주변에 연결가능한 Mi-band가 없습니다.", Toast.LENGTH_SHORT).show();
        }

        /* Point : Already Connected */
        else{
            Toast.makeText(mContext, "Mi-band와 이미 연결되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
