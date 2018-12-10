package com.dof.jaeseonlee.patient;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;
/**
 * Created by 이재선 on 2018-11-11.
 * bluetooth Communication 코드 참조 : ChangYeop-Yang/Android-Health1street
 */
public class bluetoothCommunicate extends Thread implements View.OnClickListener{
    private BluetoothAdapter bluetoothAdapter;
    private boolean scanning;
    private Handler handler;

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

    /* MARK - : Activity */
    private Activity mActivity = null;


    /* MARK - : Bluetooth Instance */
    private BluetoothGatt mDeviceBluetoothGatt = null;

    /* MARK - : Button */
    private Button aButtonList[] = null;

    /* MARK - : Boolean */
    private Boolean isVibrate = false;
    private Boolean isListeningHeartRate = false;

    /* MARK - : Mi-Band Manager Creator */
    public bluetoothCommunicate(final Context mContext, final Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        super.run();

        /* POINT - : Bluetooth Adapter */
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> mDevice = mBluetoothAdapter.getBondedDevices();

        /* POINT - : Connect Mi-Band */
        for (final BluetoothDevice mBluetooth : mDevice) {
            if (mBluetooth.getName() != null & mBluetooth.getName().contains(MI_NAME)) {
                mDeviceBluetoothGatt = mBluetooth.connectGatt(mContext, true, mBluetoothGattCallback);
                Log.e("hihihi", String.format("%s %s", mBluetooth.getAddress(), mBluetooth.getName()));
            }
        }

        /* POINT - : Setting Button Listener */
        aButtonList = new Button[]{(Button) mActivity.findViewById(R.id.homeHRMCheckButton), (Button) mActivity.findViewById(R.id.homeBluetoothDeviceConnectionButton), };
        for (final Button mButton : aButtonList) { mButton.setOnClickListener(this); }
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
    public Boolean startBandVibrate(final Button mButton) {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(ALERT_NOTIFICATION_SERVICE)
                .getCharacteristic(ALERT_NOTIFICATION_CHARACTERISTIC);

        bchar.setValue(new byte[]{2});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar))
        { Toast.makeText(mContext, "Failed start vibrate", Toast.LENGTH_SHORT).show(); return false; }

        return true;
    }

    /* MARK - : Stop Mi-Band Vibrate Method */
    public Boolean stopBandVibrate(final Button mButton) {

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
                    Snackbar.make(mActivity.findViewById(android.R.id.content), "Connect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothProfile.STATE_DISCONNECTED) :
                {
                    mDeviceBluetoothGatt.disconnect();
                    Snackbar.make(mActivity.findViewById(android.R.id.content), "Disconnect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
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
                setButtonText(aButtonList[0], String.format("%d BPM", value));

                final int mHeartBeatRate = value;

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /* POINT - : Heart Beat Rate */
                        final TextView mStateText = (TextView)mActivity.findViewById(R.id.homeHRMView);
                        final ImageView mStateImage = (ImageView)mActivity.findViewById(R.id.homeStatusPicture);
                        if (mHeartBeatRate > 50 && mHeartBeatRate < 100) { { Glide.with(mContext).load(R.drawable.normal_big).into(mStateImage); mStateText.setText("정상"); } }
                        else if (mHeartBeatRate != 0 ) { Glide.with(mContext).load(R.drawable.danger_big).into(mStateImage); mStateText.setText("응급"); }
                    }
                });
            }
        }
    };

    /* TODO - : Setting Button Text Method */
    private void setButtonText(final Button mButton, final String mSting) {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() { mButton.setText(mSting); }
        });
    }

    /* TODO - : OnClick Listener Method */
    @Override
    public void onClick(View view) {

        /* POINT - : Button */
        final Button mButton = (Button)view;

        switch (view.getId())
        {
            case (R.id.homeBluetoothDeviceConnectionButton) : { isVibrate = isVibrate ? stopBandVibrate(mButton) : startBandVibrate(mButton); break; }

            case (R.id.homeHRMCheckButton) :
            {
                /* POINT - : Check Heart Beat Rate */
                if (!isListeningHeartRate)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle("ERROR SCAN HRM");
                    alertDialogBuilder.setMessage("Heart Beat Rate(=HRM)을 측정할 수 없습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });;
                    alertDialogBuilder.show();
                    break;
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("심장박동수 측정");
                alertDialogBuilder.setMessage("Heart Beat Rate(=HRM)을 측정하고 있습니다.").setCancelable(false);
                alertDialogBuilder.show();
                startScanHeartRate();
                break;
            }
        }
    }

}
