package xyz.minsoura.vaccinator.carrierPage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import xyz.minsoura.vaccinator.MainActivity;
import xyz.minsoura.vaccinator.R;
import xyz.minsoura.vaccinator.utils.FilePath;

/**
 * Created by min on 2016-03-03.
 */
public class CarrierPageTab extends Fragment {
    private static final int PICK_FILE_REQUEST = 1;

    private String selectedFilePath;
    private String SERVER_URL = "http://blossome.be/UploadToServer.php";
    ImageView ivAttachment, bluetoothActivityOn;
    TextView tvFileName;
    ProgressDialog dialog;
    Double longitude;
    Double latitude;

    LocationManager locationManager ;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_carrier, container, false);
        ivAttachment = (ImageView) v.findViewById(R.id.ivAttachment);
        bluetoothActivityOn =(ImageView) v.findViewById(R.id.bluetoothOn);
        tvFileName = (TextView) v.findViewById(R.id.tv_file_name);
        tvFileName.setText("1.Take out the Mini Card from the Vaccine Carrier\n" +
                "2.Put the Mini SD Card into the Egg \n" +"" +
                "3.Connect the Egg to the Phone \n" +
                "4.Touch the Icon to Upload the File to the Server");


        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        bluetoothActivityOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yoIntent = new Intent(getActivity(), CarrierPage.class);
                startActivity(yoIntent);
            }
        });
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return v;
    }

    private String getCountryName(){

        String countryName="default";
        try{
            Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude,1);
            if(addresses.size()>0){
                countryName = addresses.get(0).getCountryName();
                return countryName;
            }

        }catch(Exception e){
            e.printStackTrace();

        }
        return countryName;

    }

    LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {



            longitude = location.getLongitude();

            latitude = location.getLatitude();



        }



        public void onStatusChanged(String provider, int status, Bundle extras) {

        }



        public void onProviderEnabled(String provider) {

        }



        public void onProviderDisabled(String provider) {

        }

    };


    private void showFileChooser() {



        Uri selectedFileUri = Uri.parse("/mnt/sdcard/UsbStorage/UsbDriveA/location.txt");

        selectedFilePath = selectedFileUri.getPath();
        Log.i("ALLO", "Selected File Path:" + selectedFilePath);

        if(selectedFilePath != null && !selectedFilePath.equals("")){
            tvFileName.setText(selectedFilePath);
            dialog = ProgressDialog.show(getContext(),"","Uploading File...",true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations

                    uploadFile(selectedFilePath);
                }
            }).start();
        }else{
            Toast.makeText(getContext(),"Cannot upload file to server",Toast.LENGTH_SHORT).show();
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

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                    Toast.makeText(getContext(), "Source File Doesn't Exist!", Toast.LENGTH_LONG).show();
                }
            });
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

                Log.i("ALLO", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFileName.setText("GeoData Uploading completed.\n\n" + fileName);

                            Toast.makeText(getContext(),"Uploading was successful..!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();



            } catch (FileNotFoundException e) {
                e.printStackTrace();
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   Toast.makeText(getContext(), "File Not Found", Toast.LENGTH_LONG).show();
               }
           });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "URL error!", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Cannot Read/Write File!", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

}