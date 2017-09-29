package xyz.minsoura.vaccinator.recordPage;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.minsoura.vaccinator.storage.Contact;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor2;
import xyz.minsoura.vaccinator.R;

/**
 * Created by min on 2016-03-03.
 */
public class RecordPage extends Fragment {


    private View mProgressView;
    private View wrapper_View;

    EditText vacineeID;
    TextView searchButton;

    registrationTask theRegistrationTask;
    String str;
    Dialog registrationDialog;

    //TODO: id connection
    Spinner countrySpinner;
    Spinner districtSpinner;
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> districtAdapter1;
    ArrayAdapter<String> districtAdapter2;
    ArrayAdapter<String> districtAdapterDefalult;

    EditText dialogFirstName;
    EditText dialogMiddleName;
    EditText dialogLastName;
    EditText dialogAddress1;
    EditText dialogAddress2;
    EditText dialogPostalCode;
    EditText dialogWeight;
    EditText dialogHeight;
    EditText dialogLongitude;
    EditText dialogLatitude;
    TextView dialogRegister;
    TextView dialogGps;

    String idNumber = "";
    String countryIdentifier = "";
    String districtIdentifier = "";

    String firstName = "";
    String middleName = "";
    String lastName = "";
    String dialogCountry = "";
    String dialogDistrict = "";
    String address1 = "";
    String address2 = "";
    String postalCode = "";
    String weight = "";
    String height = "";

    String posLat ="";
    String posLong ="";

    LocationManager locationManager ;

    Double longitude;
    Double latitude;


    TextView registerText;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View v = inflater.inflate(R.layout.page_record, container, false);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mProgressView = v.findViewById(R.id.tab2_progress);
        wrapper_View = v.findViewById(R.id.tab2_wrapper);



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






        registrationDialog = new Dialog(getContext());

        registrationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        registrationDialog.setContentView(R.layout.record_person_info_register_dialog);
        registrationDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialogFirstName = (EditText) registrationDialog.findViewById(R.id.firstName);
        dialogMiddleName = (EditText) registrationDialog.findViewById(R.id.middleName);
        dialogLastName = (EditText) registrationDialog.findViewById(R.id.lastName);
        dialogAddress1 = (EditText) registrationDialog.findViewById(R.id.address1);
        dialogAddress2 = (EditText) registrationDialog.findViewById(R.id.address2);
        dialogPostalCode = (EditText) registrationDialog.findViewById(R.id.postalCode);
        dialogWeight = (EditText) registrationDialog.findViewById(R.id.weight);
        dialogHeight = (EditText) registrationDialog.findViewById(R.id.height);
        dialogLongitude = (EditText) registrationDialog.findViewById(R.id.longitude);
        dialogLatitude = (EditText) registrationDialog.findViewById(R.id.latitude);
        dialogRegister = (TextView) registrationDialog.findViewById(R.id.registerDialogText);
        dialogGps = (TextView) registrationDialog.findViewById(R.id.gpsChecker);




        dialogGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    dialogLatitude.setText(String.valueOf(latitude));
                    dialogLongitude.setText(String.valueOf(longitude));

                }else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    showSettingsAlert();

                }


            }
        });

        dialogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internalRegistration();

            }
        });


        countrySpinner = (Spinner) registrationDialog.findViewById(R.id.countrySpinner);
        countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (String[]) getResources().getStringArray(R.array.countryExample));
        countrySpinner.setAdapter(countryAdapter);


        districtSpinner = (Spinner) registrationDialog.findViewById(R.id.districtSpinner);
        districtAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (String[]) getResources().getStringArray(R.array.Nepal));
        districtAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (String[]) getResources().getStringArray(R.array.SouthKorea));
        districtAdapterDefalult = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, (String[]) getResources().getStringArray(R.array.districtDefault));


        districtSpinner.setAdapter(districtAdapterDefalult);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Nepal")) {
                    districtSpinner.setAdapter(districtAdapter1);
                    countryIdentifier = "NP";
                    // districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));


                } else if (parent.getSelectedItem().toString().equals("South Korea")) {
                    districtSpinner.setAdapter(districtAdapter2);
                    countryIdentifier = "KR";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Province 1")) {

                    districtIdentifier = "PR1";
                    // districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));


                } else if (parent.getSelectedItem().toString().equals("Province 2")) {

                    districtIdentifier = "PR2";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Province 3")) {

                    districtIdentifier = "PR3";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Province 4")) {

                    districtIdentifier = "PR4";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Province 5")) {

                    districtIdentifier = "PR5";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Province 6")) {

                    districtIdentifier = "PR6";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Province 7")) {

                    districtIdentifier = "PR7";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Seoul")) {

                    districtIdentifier = "SL";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Gyeonggi")) {

                    districtIdentifier = "GG";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Busan")) {

                    districtIdentifier = "BS";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Daegu")) {

                    districtIdentifier = "DG";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Incheon")) {

                    districtIdentifier = "IC";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Daejeon")) {

                    districtIdentifier = "DJ";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Gwangju")) {

                    districtIdentifier = "GJ";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Ulsan")) {

                    districtIdentifier = "US";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Kangwon")) {

                    districtIdentifier = "KW";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("ChoongCheong")) {

                    districtIdentifier = "CC";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Jeonla")) {

                    districtIdentifier = "JL";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Jeju")) {

                    districtIdentifier = "JJ";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                } else if (parent.getSelectedItem().toString().equals("Sejong")) {

                    districtIdentifier = "SJ";
                    //districtSpinner.setSelection(getIndex(districtSpinner, getUserRegion2));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerText = (TextView) v.findViewById(R.id.registerText);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationDialog.show();
            }
        });
        vacineeID = (EditText) v.findViewById(R.id.infoEditText);
        searchButton = (TextView) v.findViewById(R.id.infoSearchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoDelivery = vacineeID.getText().toString();

                DataBaseAccessor dataExistenceChecker = new DataBaseAccessor(getContext());


                if (infoDelivery.equals("")) {
                    Toast.makeText(getContext(), "ID Number를 입력해주세요", Toast.LENGTH_LONG).show();

                } else if (dataExistenceChecker.getContact(infoDelivery) == null) {
                    Toast.makeText(getContext(), "해당 ID Number에는 정보가 없습니다.", Toast.LENGTH_LONG).show();
                } else {
                    Intent theIntent = new Intent(getActivity(), InfoActivity.class);
                    theIntent.putExtra("child_ID", infoDelivery);
                    startActivity(theIntent);
                }

            }
        });


        vacineeID.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String infoDelivery = vacineeID.getText().toString();

                    DataBaseAccessor dataExistenceChecker = new DataBaseAccessor(getContext());


                    if (infoDelivery.equals("")) {
                        Toast.makeText(getContext(), "ID Number를 입력해주세요", Toast.LENGTH_LONG).show();

                    } else if (dataExistenceChecker.getContact(infoDelivery) == null) {
                        Toast.makeText(getContext(), "해당 ID Number에는 정보가 없습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        Intent theIntent = new Intent(getActivity(), InfoActivity.class);
                        theIntent.putExtra("child_ID", infoDelivery);
                        startActivity(theIntent);
                    }
                }

                return false;
            }
        });


        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {





                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void internalRegistration(){

        posLat = String.valueOf(latitude);
        posLong = String.valueOf(longitude);

        firstName = dialogFirstName.getText().toString();
        middleName =dialogMiddleName.getText().toString();
        lastName = dialogLastName.getText().toString();
        address1 = dialogAddress1.getText().toString();
        address2 = dialogAddress2.getText().toString();

        dialogCountry = countrySpinner.getSelectedItem().toString();
        dialogDistrict = districtSpinner.getSelectedItem().toString();

        DataBaseAccessor getIdNumberAccessor = new DataBaseAccessor(getActivity());
        ArrayList<String> idNumberSet = new ArrayList<String>();

        Integer integerIdentifier = getIdNumberAccessor.getContactsCount();

        String intToString="";
        if(integerIdentifier<10){

            intToString = "000" + Integer.toString(integerIdentifier);

        }else if(10 < integerIdentifier || integerIdentifier <100){
            intToString = "00" + Integer.toString(integerIdentifier);

        }else if(100< integerIdentifier || integerIdentifier< 1000){
            intToString = "0" + Integer.toString(integerIdentifier);

        }else if(1000< integerIdentifier || integerIdentifier< 10000){

            intToString = Integer.toString(integerIdentifier);
        }




        idNumber = countryIdentifier + districtIdentifier + intToString;
        postalCode = dialogPostalCode.getText().toString();

        weight = dialogWeight.getText().toString();
        height = dialogHeight.getText().toString();
        //TODO: GPS info will be followed up after(Longitude, and Latitude)

       if(checkRequirement()) {
           theRegistrationTask = new registrationTask(idNumber,posLat, posLong, firstName, middleName, lastName, dialogCountry, dialogDistrict, address1, address2, postalCode, weight, height);
           theRegistrationTask.execute();
       }

    }

    public boolean checkRequirement(){

        Boolean identifier = true;

        if(firstName.equals("")){
            identifier = false;
            dialogFirstName.setError("필수 입력란 입니다.");
            dialogFirstName.requestFocus();
        }

        if(dialogCountry.equals("Choose")){
            Toast.makeText(getContext(), "나라를 선택해주세요", Toast.LENGTH_SHORT).show();
            identifier = false;
        }
        if (dialogDistrict.equals("Choose")){
            Toast.makeText(getContext(), "지역을 선택해주세요", Toast.LENGTH_SHORT).show();
            identifier = false;
        }


        return identifier;
    }




    public class registrationTask extends AsyncTask<Void, Void, String> {

        String idNumberM;

        String firstNameM;
        String middleNameM;
        String lastNameM;
        String dialogCountryM="";
        String dialogDistrictM="";
        String address1M;
        String address2M;
        String postalCodeM;
        String weightM;
        String heightM;
        String longitudeM;
        String latitudeM;

        registrationTask(String idNumber,String posLat, String posLong, String firstName, String middleName, String lastName, String dialogCountry,String dialogDistrict, String address1, String address2, String postalCode, String weight, String height){

            idNumberM = idNumber;
            firstNameM = firstName;
            middleNameM = middleName;
            lastNameM = lastName;
            dialogCountryM = dialogCountry;
            dialogDistrictM= dialogDistrict;
            address1M  = address1;
            address2M = address2;
            postalCodeM = postalCode;
            weightM = weight;
            heightM= height;
            latitudeM = posLat;
            longitudeM = posLong;

        }

        @Override
        protected void onCancelled(){
            theRegistrationTask =null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... params) {



            try {
                DataBaseAccessor dataBaseAccessorTab2 = new DataBaseAccessor(getContext());

                dataBaseAccessorTab2.addContact(new Contact(idNumberM,latitudeM, longitudeM, firstNameM,middleNameM,lastNameM,dialogCountryM,dialogDistrictM,address1M,address2M,postalCodeM,weightM,heightM));

                DataBaseAccessor2 dataBaseAccessor2 = new DataBaseAccessor2(getContext());
                dataBaseAccessor2.addContact(new Contact(idNumberM, "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE", "NO", "DATE"));

            } catch (Exception e) {

                return null;
            }

            return "OK";
        }

        @Override
        protected void onPostExecute(final String receivedLine) {
            super.onPostExecute(receivedLine);
            if(isCancelled()){
                theRegistrationTask =null;
            }

            showProgress(false);
            if (receivedLine == null ||receivedLine.equals("OK")) {

               registrationDialog.dismiss();
                Toast.makeText(getActivity(), "아이디가 생성 되었습니다.", Toast.LENGTH_SHORT).show();
                //TODO: I could make a intent that delivers retrieved data from the DB such as the ones for the USER PROFILE;
                vacineeID.setText(idNumberM);
            } else{
                Toast.makeText(getActivity(),receivedLine,Toast.LENGTH_LONG).show();



            }


        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            wrapper_View.setVisibility(show ? View.GONE : View.VISIBLE);
            wrapper_View.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    wrapper_View.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            wrapper_View.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
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






}