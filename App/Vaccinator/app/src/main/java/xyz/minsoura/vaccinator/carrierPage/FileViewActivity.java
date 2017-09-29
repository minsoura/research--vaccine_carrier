package xyz.minsoura.vaccinator.carrierPage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import xyz.minsoura.vaccinator.R;

/**
 * Created by 사용자 on 2017-08-08.
 */

public class FileViewActivity extends Activity {
    String fileContents;
    String fileName;
    final String TAG = "FILE_VIEW_ACTIVITY";
    TextView contentsTextView;
    Button uploadButton, closeButton;
    ProgressDialog progressDialog;
    Boolean IsBeingUpload = false;
    private String SERVER_URL = "http://blossome.be/FileTransferTanzania.php";
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_files_viewer);
        contentsTextView = (TextView) findViewById(R.id.fileContents);
        uploadButton = (Button) findViewById(R.id.uploadViewerButton);
        closeButton =(Button) findViewById(R.id.closeButton);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(FileViewActivity.this,"", "Uploading..", false, true);
                uploadCheck(fileName);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fileName = getIntent().getExtras().getString("fileName", "none");
        fileContents = readFromFile(getApplicationContext(), fileName);
        contentsTextView.setText(fileContents);
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
