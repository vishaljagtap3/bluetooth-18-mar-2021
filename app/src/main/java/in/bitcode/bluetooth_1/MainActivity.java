package in.bitcode.bluetooth_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;


    BroadcastReceiver brBluetooth = new BroadcastReceiver() {
        @SuppressLint("NewApi")
        @Override
        public void onReceive(Context context, Intent intent) {

            mt(intent.getAction());

            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    mt("Discovery started!");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    mt("Discovery finished!");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mt("-----------------------");
                    mt("name: " + newDevice.getName());
                    mt("Address: " + newDevice.getAddress());
                    mt("type: " + newDevice.getType());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(brBluetooth, intentFilter);


        /*Intent intent1 = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent1.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent1);*/


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            mt("Bluetooth not available!");
            finish();
        }

        if(!bluetoothAdapter.isEnabled()) {
            //bluetoothAdapter.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
        else {
            //bluetoothInfo();
            scanDevices();
        }
    }

    private void scanDevices() {
        mt( (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON ) +"");
        mt(bluetoothAdapter.startDiscovery()  + " startDiscovery");
    }


    private void bluetoothInfo() {
        mt("Name: " + bluetoothAdapter.getName());
        mt("Address: " + bluetoothAdapter.getAddress());
        mt("State: " + bluetoothAdapter.getState());
        bluetoothAdapter.setName("Vishal.Jagtap");

        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice bd: devices) {
            mt("Name: " + bd.getName());
            mt("Address: " + bd.getAddress());
            mt("Bond state: " + bd.getBondState());
            //mt("Type: " + bd.getType());
            mt("Type: " + bd.getBluetoothClass().getDeviceClass());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //bluetoothInfo();
            scanDevices();
        } else {
            mt("Bluetooth not enabled, try again!");
        }
    }

    private void mt(String text) {
        Log.e("tag", text);
    }
}