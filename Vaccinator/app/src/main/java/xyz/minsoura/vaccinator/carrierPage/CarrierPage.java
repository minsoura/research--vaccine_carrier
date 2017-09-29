package xyz.minsoura.vaccinator.carrierPage;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xyz.minsoura.vaccinator.R;

public class CarrierPage extends AppCompatActivity {

    private static final String TAG = "Main";
    String selectedFilePath="";

    private static final int REQUEST_CONNECT_DEVICE =1;
    private static final int REQUEST_ENABLE_BT =2;
    Boolean IsBeingUpload = false;
    ImageView btn_Upload, btn_Stop, btn_Connect;
    private TextView conStateView, fileNumberView;

    private BluetoothService btService = null;
    ArrayList<String> filesNotUploaded;
    ArrayList<String> filesUploaded;

    Dialog dialog;
    ProgressDialog progressDialog;
    EditText dialogTargetCountry;
    TextView dialogUpdate;
    TextView dialogCancel;

    static String targetCountry;

    private Activity mActivity;

    private static final String ACTION_STRING_CONNECTION_OFF= "ConnectionOff";
    private static final String ACTION_STRING_CONNECTION_ON="ConnectionOn";
    private String SERVER_URL = "http://blossome.be/FileTransferTanzania.php";
    private static final int LED_NOTIFICATION_ID = 0;


    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_STRING_CONNECTION_ON)){
                Toast.makeText(getApplicationContext(),"Connection On", Toast.LENGTH_SHORT).show();
                conStateView.setText("ON");
            }else if(intent.getAction().equals(ACTION_STRING_CONNECTION_OFF)){
                Toast.makeText(getApplicationContext(),"Connection Off", Toast.LENGTH_SHORT).show();
                conStateView.setText("OFF");
                checkFileList();
            }
        }
    };
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG, "OnCreate");
        setContentView(R.layout.carrier_main);
        componentInitialization();
        listenerRegistration();
        receiverRegistration();
        mActivity = this;

        targetCountry = bringTargetCountry();
        dialogTargetCountry.setText(targetCountry);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        checkFileList();
    }

    public void componentInitialization(){
        btn_Connect = (ImageView) findViewById(R.id.btn_connect);
        conStateView =(TextView) findViewById(R.id.stateView);
        fileNumberView = (TextView) findViewById(R.id.fileNumberView);
        btn_Stop = (ImageView) findViewById(R.id.stopButton);
        btn_Upload =(ImageView) findViewById(R.id.uploadButton);
        filesNotUploaded = new ArrayList<>();
        filesUploaded= new ArrayList<>();

        dialog = new Dialog(CarrierPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_set_country);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogTargetCountry = (EditText) dialog.findViewById(R.id.targetCountry);
        dialogUpdate = (TextView) dialog.findViewById(R.id.setCountry);
        dialogCancel = (TextView) dialog.findViewById(R.id.setCancel);

        if(btService == null){
            btService = new BluetoothService(this, mHandler);
        }
    }

    public void listenerRegistration(){
        btn_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadAllFilesNotUploaded();

            }
        });

        btn_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btService.getDeviceState()) {
                    btService.enableBluetooth();
                } else {
                    finish();
                }
            }
        });

        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btService.stop();


            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryValue = dialogTargetCountry.getText().toString();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final SharedPreferences.Editor editor = pref.edit();
                editor.putString("targetCountry", countryValue);
                editor.apply();
                Toast.makeText(getApplicationContext(), "Successfully changed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void receiverRegistration(){
        if(activityReceiver != null){
            IntentFilter connectionOn = new IntentFilter(ACTION_STRING_CONNECTION_ON);
            IntentFilter connectionOff = new IntentFilter(ACTION_STRING_CONNECTION_OFF);
            registerReceiver(activityReceiver, connectionOn);
            registerReceiver(activityReceiver, connectionOff);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(activityReceiver);
    }
    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onResume(){
        super.onResume();
        receiverRegistration();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK ){
                    btService.getDeviceInfo(data);
                }
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK){
                    btService.scanDevice();
                }else {
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }

    private String messageContentsDisplay(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString("lastMessage", "default");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_carrier, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_change_country:
                dialog.show();
                return true;
            case R.id.menu_view_fils:
                Intent fileIntent = new Intent(mActivity, FileListActivity.class);
                startActivity(fileIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String bringTargetCountry(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString("targetCountry", "Default");
    }

    private void checkFileList(){
        String path = getApplicationContext().getFilesDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        filesNotUploaded.clear();
        filesUploaded.clear();
        for (File file : files) {
            if (!file.getName().contains("OK")) {
                Log.d(TAG, "Not Uploaded:" + file.getName());
                filesNotUploaded.add(file.getName());
            }else {
                Log.d(TAG, "Uploaded:" + file.getName());
                filesUploaded.add(file.getName());
            }
        }
        Log.d(TAG, "Number of Files Not Uploaded: " + String.valueOf(filesNotUploaded.size()));
        Log.d(TAG, "Number of Files Uploaded: " + String.valueOf(filesUploaded.size()));
        String displayMessage = filesNotUploaded.size() + " FILE(S) TO UPLOAD";
        fileNumberView.setText(displayMessage);
        if(filesNotUploaded.size()==0){
            btn_Upload.setEnabled(false);
            Toast.makeText(this,"0 File(s) to Upload", Toast.LENGTH_LONG).show();
            btn_Upload.setImageResource(R.drawable.upload_b);
        }else{
            btn_Upload.setEnabled(true);
            btn_Upload.setImageResource(R.drawable.upload_pink);
        }
    }
    private void uploadAllFilesNotUploaded(){
        Boolean signal = false;
        progressDialog = ProgressDialog.show(CarrierPage.this,"", "Uploading..", false, true);
        for(int i =0;i<filesNotUploaded.size();i++){
            Log.d(TAG, "InsideLoop, Will Upload this File: " + filesNotUploaded.get(i));
            uploadCheck(filesNotUploaded.get(i));
            if(i==filesNotUploaded.size()-1){
                signal = true;
            }
        }
        if(signal){
            for(int i=0; i<filesNotUploaded.size(); i++){
                Log.d(TAG, "InsideLoop 2, Define File As Uploaded: " + filesNotUploaded.get(i));
                defineFileAsUploaded(filesNotUploaded.get(i));
                if(i==-filesNotUploaded.size()-1){
                    progressDialog.dismiss();
                }
            }
        }
        checkFileList();
    }
    private void writeToFile(String data,Context context, String fileName) {
        Log.d(TAG, "WriteToFile");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context, String fileName) {
        Log.d(TAG, "readFromFIle: " + fileName);
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
            Log.e(TAG, "File not found rff: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file rff: " + e.toString());
        }
        return ret;
    }
    private void defineFileAsUploaded(String notUploadedFiles){

        Log.d(TAG, "What will be defined as uploaded: " + notUploadedFiles);
        String previousContents = readFromFile(getApplicationContext(), notUploadedFiles);
        String newFileName = notUploadedFiles;
        newFileName = newFileName.replace(".txt", "_OK.txt");

        String fileDir = getFilesDir() + "/" + notUploadedFiles;
        Uri selectedFileUri = Uri.parse(fileDir);
        String selectedFilePath = selectedFileUri.getPath();

        File oldFile = new File(selectedFilePath);
        File newFile = new File(getFilesDir() + "/" +newFileName);
        boolean check = oldFile.renameTo(newFile);
        writeToFile(previousContents, getApplicationContext(), newFileName);
    }

    public void uploadCheck(String notUploadedFiles){
        String fileDir = getFilesDir() + "/" + notUploadedFiles;
        Uri selectedFileUri = Uri.parse(fileDir);
        final String selectedFilePath2 = selectedFileUri.getPath();
        Log.d(TAG, "Selected File Path:" + selectedFilePath2);
        if(selectedFilePath2 != null && !selectedFilePath2.equals("")){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadFile(selectedFilePath2);
                }
            }).start();

        }else{
            Toast.makeText(this,"Could Not Upload",Toast.LENGTH_SHORT).show();
        }
    }

    public int uploadFile(final String selectedFilePath){
        IsBeingUpload = true;
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
        Log.d(TAG, "Real Uploading This File: " + fileName);
        if (!selectedFile.isFile()){
            runOnUiThreadDismissDialog();
            runOnUiThreadToast("Source File Doesn't Exit.");
            IsBeingUpload = false;

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
                    runOnUiThreadDismissDialog();
                    runOnUiThreadToast("Uploaded Successfully..!");
                    Log.d(TAG, "what just uploaded was: " + fileName);

                    IsBeingUpload = false;

                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThreadToast("File Not Found..");
                runOnUiThreadDismissDialog();
                IsBeingUpload = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThreadToast("URL Error!");
                runOnUiThreadDismissDialog();
                IsBeingUpload = false;
            } catch (IOException e) {
                e.printStackTrace();
                IsBeingUpload = false;
                runOnUiThreadToast("Network is Unavailable/Turn it On");
            }


            return serverResponseCode;
        }
        //IsBeingUpload = false;
    }

    private void runOnUiThreadToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void runOnUiThreadDismissDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }


}



