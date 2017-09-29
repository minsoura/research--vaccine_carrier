package xyz.minsoura.vaccinator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import xyz.minsoura.vaccinator.carrierPage.CarrierPage;
import xyz.minsoura.vaccinator.storage.Contact;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor2;
import xyz.minsoura.vaccinator.utils.SlidingTabLayout;
import xyz.minsoura.vaccinator.adapter.ViewPagerAdapter;
import xyz.minsoura.vaccinator.utils.requestHandler;


public class MainActivity extends AppCompatActivity implements LocationListener {
    ViewPager pager;
    ViewPagerAdapter adapter;


     View mProgressView;
    View wrapper_View;
    View wrapper_View2;

   sendDataTask DataSendingTask;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"INFO", "CARRIER","CENTER"};
    int Numboftabs =3;
    Dialog networkChecker;

    TextView networkCancel;

    TextView networkYes;

    ArrayList<String> idNumberSet = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressView = findViewById(R.id.main_progress);
        wrapper_View = findViewById(R.id.pager);
        wrapper_View2 = findViewById(R.id.tabs);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        adapter = new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        pager =(ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs =(SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
            }
        });

        tabs.setViewPager(pager);
        networkChecker = new Dialog(this);
        networkChecker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        networkChecker.setContentView(R.layout.record_network_checker_dialog);
        networkChecker.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);





        networkCancel = (TextView) networkChecker.findViewById(R.id.networkCancel);
        networkYes = (TextView) networkChecker.findViewById(R.id.networkYes);
        networkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkChecker.dismiss();
            }
        });
        networkYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataFunction();
                networkChecker.dismiss();
            }
        });

        tabs.TheMagicMethod(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NetworkServiceFunction();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void NetworkServiceFunction(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to wifi
                networkChecker.show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Network Is Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void SendDataFunction(){

        DataBaseAccessor getIdNumberAccessor = new DataBaseAccessor(this);
        DataBaseAccessor2 getIdNumberAccssor2 = new DataBaseAccessor2(this);

        Integer theNumberOfLists= getIdNumberAccessor.getContactsCount();
        Toast.makeText(getApplicationContext(),String.valueOf(theNumberOfLists),Toast.LENGTH_LONG).show();

       for(int i=0; i<getIdNumberAccessor.getContactsCount(); i++){
           idNumberSet.add(i, getIdNumberAccessor.getAllContacts().get(i).getIdNumber());
            Contact dataDeliveryContact = getIdNumberAccessor.getContact(idNumberSet.get(i));
            Contact dataDeliveryContactSecond = getIdNumberAccssor2.getContact(idNumberSet.get(i));
           DataSendingTask = new sendDataTask(dataDeliveryContact.getIdNumber(),dataDeliveryContact.getPosLat(),dataDeliveryContact.getPosLong(),dataDeliveryContact.getFirstNameM(),dataDeliveryContact.getMiddleNameM(),dataDeliveryContact.getLastNameM(),dataDeliveryContact.getDialogCountryM(),dataDeliveryContact.getDialogDistrictM(),dataDeliveryContact.getAddress1M(),dataDeliveryContact.getAddress2M(),dataDeliveryContact.getPostalCodeM(),dataDeliveryContact.getWeightM(),dataDeliveryContact.getHeightM(),dataDeliveryContactSecond.getBcgCheck(),dataDeliveryContactSecond.getBcgDate(),dataDeliveryContactSecond.getPolioFirstCheck(),dataDeliveryContactSecond.getPolioFirstDate(),dataDeliveryContactSecond.getPolioSecondCheck(),dataDeliveryContactSecond.getPolioSecondDate(),dataDeliveryContactSecond.getPolioThirdCheck(),dataDeliveryContactSecond.getPolioThirdDate(),dataDeliveryContactSecond.getDptFirstCheck(),dataDeliveryContactSecond.getDptFirstDate(),
                   dataDeliveryContactSecond.getDptSecondCheck(),dataDeliveryContactSecond.getDptSecondDate(),dataDeliveryContactSecond.getDptThirdCheck(),dataDeliveryContactSecond.getDptThirdDate(),dataDeliveryContactSecond.getMeasleCheck(),dataDeliveryContactSecond.getMeasleDate(),dataDeliveryContactSecond.getJeCheck(),dataDeliveryContactSecond.getJeDate());

           // Contact combinedDelivery = new Contact(dataDeliveryContact.getIdNumber(), dataDeliveryContact.getFirstNameM(),dataDeliveryContact.getMiddleNameM(),dataDeliveryContact.getLastNameM(), dataDeliveryContact.getDialogCountryM(), dataDeliveryContact.getDialogDistrictM(),dataDeliveryContact.getAddress1M(),dataDeliveryContact.getAddress2M(),dataDeliveryContact.getPostalCodeM(),dataDeliveryContact.getWeightM(),dataDeliveryContact.getHeightM(), dataDeliveryContactSecond.getBcgCheck(),dataDeliveryContactSecond.getPolioFirstCheck(),dataDeliveryContactSecond.getPolioSecondCheck(),dataDeliveryContactSecond.getPolioThirdCheck(),dataDeliveryContactSecond.getDptFirstCheck(),dataDeliveryContactSecond.getDptSecondCheck(),dataDeliveryContactSecond.getDptThirdCheck(),dataDeliveryContactSecond.getMeasleCheck(),dataDeliveryContactSecond.getJeCheck(),dataDeliveryContactSecond.getBcgDate(),dataDeliveryContactSecond.getPolioFirstDate(),dataDeliveryContactSecond.getPolioSecondDate(),dataDeliveryContactSecond.getPolioThirdDate(),dataDeliveryContactSecond.getDptFirstDate(),dataDeliveryContactSecond.getDptSecondDate(),dataDeliveryContactSecond.getDptThirdDate(),dataDeliveryContactSecond.getMeasleDate(),dataDeliveryContactSecond.getJeDate());
           //Log.e("test", idNumberSet.get(i));
           DataSendingTask.execute();
            Log.e("idNumber", dataDeliveryContact.getIdNumber());
         if(i+1 ==theNumberOfLists){
             Toast.makeText(getApplicationContext(),"전송이 완료 되었습니다",Toast.LENGTH_LONG).show();
         }

       }




        

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class sendDataTask extends AsyncTask<Void, Void, String> {


        String idNumber;
        String posLat;
        String posLong;

        String firstNameM;
        String  middleNameM;
        String lastNameM ;
        String dialogCountryM;
        String dialogDistrictM;
        String address1M ;
        String address2M ;
        String postalCodeM;
        String weightM;
        String heightM;
        String bcgCheck ;
        String bcgDate ;
        String polioFirstCheck ;
        String polioFirstDate ;
        String  polioSecondCheck;
        String  polioSecondDate;
        String  polioThirdCheck;
        String  polioThirdDate;

        String dptFirstCheck;
        String  dptFirstDate;
        String  dptSecondCheck;
        String  dptSecondDate;
        String dptThirdCheck;
        String  dptThirdDate;

        String measleCheck;
        String  measleDate;

        String jeCheck;
        String jeDate;


        sendDataTask(String idNumber0, String posLat, String posLong, String firstName, String middleName, String lastName, String dialogCountry, String dialogDistrict, String address1, String address2, String postalCode, String weight, String height, String bcgCheck, String bcgDate,String polioFirstCheck,String polioFirstDate, String polioSecondCheck, String polioSecondDate, String polioThirdCheck, String polioThirdDate, String dptFirstCheck, String dptFirstDate, String dptSecondCheck, String dptSecondDate, String dptThirdCheck, String dptThirdDate, String measleCheck
                     ,String measleDate, String jeCheck, String jeDate) {

            this.idNumber = idNumber0;
            this.posLat = posLat;
            this.posLong = posLong;
            this.firstNameM = firstName;
            this.middleNameM = middleName;
            this.lastNameM = lastName;
            this.dialogCountryM = dialogCountry;
            this.dialogDistrictM= dialogDistrict;
            this.address1M  = address1;
            this.address2M = address2;
            this.postalCodeM = postalCode;
            this.weightM = weight;
            this.heightM= height;
            this.bcgCheck = bcgCheck;
            this.bcgDate = bcgDate;
            this.polioFirstCheck = polioFirstCheck;
            this.polioFirstDate = polioFirstDate;
            this.polioSecondCheck = polioSecondCheck;
            this.polioSecondDate = polioSecondDate;
            this.polioThirdCheck = polioThirdCheck;
            this.polioThirdDate = polioThirdDate;

            this.dptFirstCheck = dptFirstCheck;
            this.dptFirstDate = dptFirstDate;
            this.dptSecondCheck = dptSecondCheck;
            this.dptSecondDate = dptSecondDate;
            this.dptThirdCheck = dptThirdCheck;
            this.dptThirdDate = dptThirdDate;

            this.measleCheck = measleCheck;
            this.measleDate = measleDate;

            this.jeCheck = jeCheck;
            this.jeDate = jeDate;




        }

        @Override
        protected void onCancelled() {
           DataSendingTask = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override

        protected String doInBackground(Void... params) {

            String URL = "";
                URL = "http://blossome.be/uploadAll.php";

            HashMap<String, String> SendSet = new HashMap<>();
            SendSet.put("idNumber", idNumber);
            SendSet.put("posLat", posLat);
            SendSet.put("posLong", posLong);
            SendSet.put("firstName", firstNameM);
            SendSet.put("middleName", middleNameM);
            SendSet.put("lastName", lastNameM);
            SendSet.put("country", dialogCountryM);
            SendSet.put("district", dialogDistrictM);
            SendSet.put("address1", address1M);
            SendSet.put("address2", address2M);
            SendSet.put("postalCode", postalCodeM);
            SendSet.put("weight", weightM);
            SendSet.put("height", heightM);
            SendSet.put("bcgCheck",bcgCheck);
            SendSet.put("bcgDate",bcgDate);
            SendSet.put("polioFirstCheck",polioFirstCheck);
            SendSet.put("polioFirstDate", polioFirstDate);
            SendSet.put("polioSecondCheck", polioSecondCheck);
            SendSet.put("polioSecondDate", polioSecondDate);
            SendSet.put("polioThirdCheck", polioThirdCheck);
            SendSet.put("polioThirdDate", polioThirdDate);
            SendSet.put("dptFirstCheck", dptFirstCheck);
            SendSet.put("dptFirstDate", dptFirstDate);
            SendSet.put("dptSecondCheck", dptSecondCheck);
            SendSet.put("dptSecondDate", dptSecondDate);
            SendSet.put("dptThirdCheck",dptThirdCheck);
            SendSet.put("dptThirdDate", dptThirdDate);
            SendSet.put("measleCheck", measleCheck);
            SendSet.put("measleDate", measleDate);
            SendSet.put("jeCheck",jeCheck);
            SendSet.put("jeDate", jeDate);

            try {
                requestHandler LikeDeliverHandler = new requestHandler();
                String result = LikeDeliverHandler.sendPostRequest(URL, SendSet);

                return result;

            } catch (Exception e) {

                return null;
            }


        }

        @Override
        protected void onPostExecute(final String receivedLine) {
            super.onPostExecute(receivedLine);
            if (isCancelled()) {
                DataSendingTask = null;
            }

            showProgress(false);
            if (receivedLine == null || receivedLine.equals("Error")) {

                Toast.makeText(getApplicationContext(),receivedLine, Toast.LENGTH_LONG).show();
                //TODO: I could make a intent that delivers retrieved data from the DB such as the ones for the USER PROFILE;

            } else if (receivedLine.equals("no")) {

                Toast.makeText(getApplicationContext(),idNumber +"의 정보 업로드 실패하였습니다", Toast.LENGTH_LONG).show();

            }
        }
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
            wrapper_View2.setVisibility(show ? View.GONE : View.VISIBLE);
            wrapper_View2.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    wrapper_View2.setVisibility(show ? View.GONE : View.VISIBLE);
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
            wrapper_View2.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
