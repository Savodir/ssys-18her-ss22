package be.ap.ss22;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //region Initializations
    private static final int READ_REQUEST_CODE = 42;
    String deviceName, address;
    private BluetoothAdapter bluetoothAdapter = null;
    Set<BluetoothDevice> pairedDevices;
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThread mConnectedThread;
    TextView txtGuessed;
    EditText editX, editY;
    Button btnDevSelect, btnConnect, btnCalibrate, btnGuess;
    Boolean startData = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editX = findViewById(R.id.edit_X);
        editY = findViewById(R.id.edit_Y);
        txtGuessed = findViewById(R.id.txt_guessed);
        btnDevSelect = findViewById(R.id.btn_deviceselector);
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setEnabled(false);
        btnCalibrate = findViewById(R.id.btn_calibrate);
        btnCalibrate.setEnabled(false);
        btnGuess = findViewById(R.id.btn_guess);
        btnGuess.setEnabled(false);
        ButtonSetup();
        bluetooth();
    }
    private void ButtonSetup() {
        btnDevSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                // getting list of devies which are already paired with our device
                final Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
                if (pairedDevice.size() > 0) {
                    for (BluetoothDevice device : pairedDevice) {
                        // device name
                        String deviceName = device.getName();
                        // device mac address
                        String deviceHardwareAddress = device.getAddress();
                        Log.d("aa", "Device name:" + deviceName + "Device Hardware/MAC Address:" + deviceHardwareAddress);
                        arrayAdapter.add(deviceName + " " + deviceHardwareAddress);
                    }
                }
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                builderSingle.setIcon(R.drawable.ic_launcher_background);
                builderSingle.setTitle("Select One Name:-");
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        address = strName.substring(strName.length() - 17);
                        Log.d("MAC Address", address);
                        Toast.makeText(getApplicationContext(), "Selected: " + strName, Toast.LENGTH_SHORT).show();
                        btnConnect.setEnabled(true);
                    }
                });
                builderSingle.show();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                try {
                    btSocket = createBluetoothSocket(device);
                    Log.d("socket", "socket");
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
                    try {
                        btSocket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    btSocket.connect();
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e2) {
                    }
                }
                mConnectedThread = new ConnectedThread(btSocket, bluetoothIn, MainActivity.this);
                mConnectedThread.start();
                if(!startData) {
                    mConnectedThread.write("s");
                    startData = true;
                }
                btnCalibrate.setEnabled(true);
            }
        });
        btnCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConnectedThread.write("x");
                btnGuess.setEnabled(true);
            }
        });
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtX = editX.getText().toString();
                String txtY = editY.getText().toString();
                if(txtX == "" || txtY == "") {
                    Toast.makeText(getBaseContext(), "Please fill in values", Toast.LENGTH_SHORT).show();
                }
                else {
                    mConnectedThread.write(txtX + "," + txtY);
                    mConnectedThread.write("z");
                }
            }
        });
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Toast.makeText(getApplicationContext(), deviceName, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
    private void bluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, READ_REQUEST_CODE);
            Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_SHORT).show();
        }
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
            }
        }
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                try {
                    if (msg.what == handlerState) {
                        String x, y, corner, distance1, distance3;
                        String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                        recDataString.append(readMessage);
                        //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                        if (endOfLineIndex > 0) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                            int dataLength = dataInPrint.length();                          //get length of data received
                       /*     Log.d("Data", recDataString.toString());
                            x = recDataString.toString();
                            x = x.substring(x.lastIndexOf("#") + 1);
                            x = x.substring(0, x.lastIndexOf("@"));
                            y = recDataString.toString();
                            y = y.substring(y.lastIndexOf("@") + 1);
                            y = y.substring(0, y.lastIndexOf("&"));
                            corner = recDataString.toString();
                            corner = corner.substring(corner.lastIndexOf("&") + 1);
                            corner = corner.substring(0, corner.lastIndexOf("*"));
                            distance1 = recDataString.toString();
                            distance1 = distance1.substring(distance1.lastIndexOf("*") + 1);
                            distance1 = distance1.substring(0, distance1.lastIndexOf("+"));
                            distance3 = recDataString.toString();
                            distance3 = distance3.substring(distance3.lastIndexOf("+") + 1);
                            distance3 = distance3.substring(0, distance3.lastIndexOf("~"));
                            Log.d("X", x);
                            recDataString.delete(0, recDataString.length());                    //clear all string data
                            dataInPrint = " ";
                            txtX.setText("X: " + x);
                            txtY.setText("Y:" + y);
                            robot.y = Float.valueOf(y);
                            robot.x = Float.valueOf(x);
                            robot.frontSensor = Float.valueOf(distance1);
                            robot.backSensor = Float.valueOf(distance3);
                            robot.y = Float.valueOf(y) + 10;
                            robot.x = Float.valueOf(x) + 10;
                            robot.heading = Float.valueOf(corner);
                            txtcorner.setText("Corner: " + corner);
                            txtDistance.setText("Front: " + distance1);
                            txtDistance3.setText("Back:" + distance3);
*/
                        }
                    }
                }
                catch(OutOfMemoryError e1) {
                    e1.printStackTrace();
                    Log.e("Memory exceptions","exceptions"+e1);
                }
                catch (StringIndexOutOfBoundsException e) {
                    Log.d("Error", "caught");
                }
            }
        };
    }
}
