package com.comtrade.bluetoothmessenger.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageView = (TextView) findViewById(R.id.message);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // check if the device supports Bluetooth.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.toast_no_bluetooth, Toast.LENGTH_LONG).show();
            messageView.setText(R.string.msg_no_bluetooth);
            return;
        }

        // check if the Bluetooth adapter is enabled
        if (!bluetoothAdapter.isEnabled()) {
            messageView.setText(R.string.msg_enabling_bluetooth);
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            return;
        } else {
            messageView.setText(R.string.msg_bluetooth_activated);
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_devices_list_element);
        ListView devicesList = (ListView) findViewById(R.id.list_devices);
        devicesList.setAdapter(arrayAdapter);
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices != null && pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.toast_bluetooth_activated, Toast.LENGTH_LONG).show();
                messageView.setText(R.string.msg_bluetooth_activated);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.toast_bluetooth_not_activated, Toast.LENGTH_LONG).show();
                messageView.setText(R.string.msg_bluetooth_not_activated);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
