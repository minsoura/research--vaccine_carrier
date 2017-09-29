package xyz.minsoura.vaccinator.carrierPage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by min on 2017-01-05.
 */
public class BluetoothService extends Service{

    private static final String TAG ="BluetoothService";
    private String SERVER_URL = "http://blossome.be/FileTransferTanzania.php";
    String selectedFilePath="";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String ACTION_STRING_CONNECTION_ON = "ConnectionOn";
    private static final String ACTION_STRING_CONNECTION_OFF = "ConnectionOff";

    private static final int REQUEST_CONNECT_DEVICE =1;
    private static final int REQUEST_ENABLE_BT =2;

    private BluetoothAdapter btAdapter;

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public static Boolean retryConnection = true;
    public static Boolean isConnectionLost = false;
    BluetoothDevice lastDevice;
    String address;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();

    private Activity mActivity;
    private Handler mHandler;
    UploadTask uploadTask;
    ProgressDialog dialog;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private int mState;

    public BluetoothService(Activity ac, Handler h){
        mActivity = ac;
        mHandler = h;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }

    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        connect(lastDevice);
                    }
                });
            }
        };
        timer.schedule(timerTask,1000,5000);
    }
    public boolean getDeviceState(){
        Log.d(TAG, "CHECK the Bluetooth support");
        if(btAdapter == null){
            Log.d(TAG, "Bluetooth is not available");
            return false;
        }else {
            Log.d(TAG, "Bluetooth is available");
            return true;
        }
    }

    public void enableBluetooth(){
        Log.i(TAG, "Check the enabled Bluetooth");
        if(btAdapter.isEnabled()){
            Log.d(TAG, "Bluetooth Enabled Now");
            scanDevice();
            retryConnection = true;
        }else{
            Log.d(TAG, "Bluetooth Enable Request");
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }

    public void scanDevice(){
        Log.d(TAG, "Scan Device");
        if(mState ==2 || mState ==3){
        }else{
            Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
            mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        }
    }

    public void getDeviceInfo(Intent data){
        address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        lastDevice = btAdapter.getRemoteDevice(address);
        Log.d(TAG, "GET DEVICE INFO \n" + "address: " + address);
        connect(lastDevice);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        public ConnectThread(BluetoothDevice device){
            mmDevice = device;
            BluetoothSocket tmp = null;
            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            }catch (IOException e){
                Log.e(TAG, "create() failed", e);
                if(!isConnectionLost){
                    dialog.dismiss();
                }
            }
            mmSocket = tmp;
        }
        public void run(){
            Log.i(TAG, "BEGIN_CONNECT_THREAD");
            setName("ConnectThread");
            btAdapter.cancelDiscovery();
            try{
                mmSocket.connect();
                Log.d(TAG, "Connect Success");
            }catch (IOException e){
                connectionFailed();
                Log.d(TAG, "Connection Failed");
                runOnUiThreadToast("Could Not Connect..");
                if(!isConnectionLost){
                    dialog.dismiss();
                }
                try{
                    mmSocket.close();
                }catch (IOException e2){
                    Log.e(TAG, "unable to close() socket during connection");
                }
                BluetoothService.this.start();
                return;
            }
            synchronized(BluetoothService.this){
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice);
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch(IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }catch(IOException e){
                Log.e(TAG, "tmp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run(){
            Log.i(TAG, "BEGIN mConnectedThread");
            if(!isConnectionLost){
                dialog.dismiss();
            }
            messageTrigger(ACTION_STRING_CONNECTION_ON);
            byte[] buffer = new byte[1024];
            int bytes ;
            String end = "]";
            StringBuilder curMsg = new StringBuilder();
            try{
                while( -1 != (bytes =mmInStream.read(buffer))) {
                    curMsg.append(new String(buffer, 0, bytes, Charset.forName("UTF-8")));
                    int endIdx = curMsg.indexOf(end);
                    if(endIdx != -1) {
                        String fullMessage = curMsg.substring(0, endIdx + end.length());
                        curMsg.delete(0, endIdx + end.length());
                        fullMessage = fullMessage.replace("[","");
                        fullMessage = fullMessage.replace("]","");
                        fullMessage = fullMessage + ",";
                        if(fullMessage.length()>10){
                            messageContentsSave(fullMessage);
                            runOnUiThreadToast(fullMessage);
                            Log.d(TAG, fullMessage);
                            writeToFile(fullMessage, mActivity, decideFileName());
                            uploadDataToServer(fullMessage, decideFileName());
                            Log.d(TAG, "Uploading File Fragments: " + fullMessage + "/ " + decideFileName());
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                connectionLost();
            }
        }
        public void write(byte[] buffer){
            try{
                mmOutStream.write(buffer);
            }catch (IOException e){
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch (IOException e){
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private synchronized  void setState(int state){
        if(state == 3){
            stopTimer();
            isConnectionLost = false;

        }
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }
    public synchronized void start(){
        Log.d(TAG, "start");
        if(mConnectThread == null){
        }else{
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread == null){
        } else{
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }
    public synchronized void connect(BluetoothDevice device){
        Log.d(TAG, "connect to: " + device);

        if(!isConnectionLost){
            dialog =ProgressDialog.show(mActivity, "", "Connecting...", true);
        }

        if(mState == STATE_CONNECTING){
            if(mConnectThread == null){
            }else{
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        if(mConnectedThread == null){
        }else{
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device){
        Log.d(TAG, "connected");
        if(mConnectThread == null){
        }else {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread == null){
        }else{
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        setState(STATE_CONNECTED);
    }

    public synchronized void stop(){
        Log.d(TAG, "stop");
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
        retryConnection = false;
        uploadCheck();
    }

    public void write(byte[] out){
        ConnectedThread r;
        synchronized (this){
            if(mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
            r.write(out);
        }
    }

    private void connectionFailed(){
        setState(STATE_LISTEN);
    }

    private void connectionLost(){
        isConnectionLost = true;
        messageTrigger(ACTION_STRING_CONNECTION_OFF);
        if(retryConnection){
            Log.d(TAG, "Reconnecting To.. \n" + "address: " + address);
            startTimer();
        }
        Log.d(TAG, "connection lost function called");
        Log.d(TAG, String.valueOf(retryConnection));
        setState(STATE_LISTEN);

    }

    private void messageTrigger(String action){
        Intent new_intent = new Intent();
        new_intent.setAction(action);
        mActivity.sendBroadcast(new_intent);
    }

    private void messageContentsSave(String messageContents){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString("lastMessage", messageContents);
        editor.apply();
    }

    public void uploadDataToServer(String Data, String fileName) {
        uploadTask = new UploadTask(Data, fileName);
        uploadTask.execute();
    }
    public class UploadTask extends AsyncTask<Void, Void, String> {
        private final String mData;
        private final String mFileName;

        Boolean checker = true;
        UploadTask(String Data, String FileName) {
            mData = Data;
            mFileName = FileName;
        }
        @Override
        protected void onCancelled() {
            uploadTask = null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress(true);
        }
        @Override
        protected String doInBackground(Void... params) {
            if (!checker) {
                Log.e("Checker", "Checker is Working");
                return null;
            }
            String URL = "";
            URL = "http://blossome.be/autoMonitoringTanzania.php";
            HashMap<String, String> SendSet = new HashMap<>();
            SendSet.put("fileData", mData);
            SendSet.put("fileName", mFileName);

            try {
                RequestHandler DeliverHandler = new RequestHandler();
                String result = DeliverHandler.sendPostRequest(URL, SendSet);
                if (!checker) {
                    Log.e("Checker", "Checker is Working");
                    return null;
                }
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String receivedLine) {
            super.onPostExecute(receivedLine);
            if (isCancelled()) {
                uploadTask = null;
            }
            if (receivedLine.equals("yes")) {
                runOnUiThreadToast("data fragment uploaded");

            } else if (receivedLine.equals("no")) {
                runOnUiThreadToast("data fragment strayed");
            }
            checker = false;
        }
    }

    public String bringTargetCountry(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        return sharedPreferences.getString("targetCountry", "Default");
    }

    private void writeToFile(String data,Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String decideFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date dateSaved = new Date();
        String dateStringSaved = formatter.format(dateSaved);
        return bringTargetCountry() + "_" + dateStringSaved +".txt";
    }

    public void uploadCheck(){
        String fileDir = mActivity.getFilesDir() + "/" + decideFileName();
        Uri selectedFileUri = Uri.parse(fileDir);
        selectedFilePath = selectedFileUri.getPath();
        Log.d(TAG, "Selected File Path:" + selectedFilePath);
        if(selectedFilePath != null && !selectedFilePath.equals("")){
            dialog = ProgressDialog.show(mActivity, "", "Uploading File...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadFile(selectedFilePath);
                }
            }).start();

        }else{
            runOnUiThreadToast("Could Not Upload..");
            messageTrigger(ACTION_STRING_CONNECTION_OFF);
        }
    }

    public int uploadFile(final String selectedFilePath){
        int serverResponseCode = 0;
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);
        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();
            runOnUiThreadToast("Source File Doesn't Exit!");
            Log.d(TAG, mActivity.getFilesDir().toString());
            messageTrigger(ACTION_STRING_CONNECTION_OFF);
            return 0;
        }else{
            try{
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file",selectedFilePath);
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];
                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);
                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                Log.d(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThreadToast("Uploaded Successfully!");
                    defineFileAsUploaded();
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThreadToast("File Not Found");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThreadToast("URL error!");
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThreadToast("Network is Unavailable/ turn it on");
            }
            dialog.dismiss();
            messageTrigger(ACTION_STRING_CONNECTION_OFF);
            return serverResponseCode;
        }
    }
    private String readFromFile(Context context, String fileName) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }



    private void defineFileAsUploaded(){

        String previousContents = readFromFile(mActivity, decideFileName());
        String newFileName = decideFileName();
        newFileName = newFileName.replace(".txt", "_OK.txt");

        File oldFile = new File(selectedFilePath);
        File newFile = new File(mActivity.getFilesDir() + "/" +newFileName);

        writeToFile(previousContents, mActivity, newFileName);
    }

    private void runOnUiThreadToast(final String message){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
