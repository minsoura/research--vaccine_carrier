package xyz.minsoura.vaccinator.recordPage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.minsoura.vaccinator.storage.Contact;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor;
import xyz.minsoura.vaccinator.R;

/**
 * Created by min on 2016-03-28.
 */
public class PersonTab extends Fragment {
    static String child_ID;


    private View mProgressView;
    private View wrapper_View;



    //TODO: variable used in the page
    TextView firstName;
    TextView middleName;
    TextView lastName;
    TextView country;
    TextView district;
    TextView address1;
    TextView address2;
    TextView postalCode;
    TextView weight;
    TextView height;
    TextView personalUpdate;
    TextView updateConfirm;

    TextView posLat;
    TextView posLong;

    EditText dialogFirstName;
    EditText dialogMiddleName;
    EditText dialogLastName;
    EditText dialogAddress1;
    EditText dialogAddress2;
    EditText dialogPostalCode;
    EditText dialogWeight;
    EditText dialogHeight;

    TextView dialogCountry;
    TextView dialogDistrict;
    TextView dialogPosLat;
    TextView dialogPosLong;

    Dialog updateDialog;

    String longitude="";
    String latitude="";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.record_personal_info_tab, container, false);
        child_ID = getActivity().getIntent().getStringExtra("child_ID");




        mProgressView = v.findViewById(R.id.personTab_progress);
        wrapper_View = v.findViewById(R.id.personTab_wrapper);
        firstName =(TextView) v.findViewById(R.id.firstName);
        middleName =(TextView) v.findViewById(R.id.middleName);
        lastName =(TextView) v.findViewById(R.id.lastName);
        country =(TextView) v.findViewById(R.id.country);
        district =(TextView) v.findViewById(R.id.district);

        address1 =(TextView) v.findViewById(R.id.address1);
        address2 =(TextView) v.findViewById(R.id.address2);
        postalCode = (TextView) v.findViewById(R.id.postalCode);
        weight =(TextView) v.findViewById(R.id.weight);
        height =(TextView) v.findViewById(R.id.height);
        personalUpdate =(TextView) v.findViewById(R.id.PERSONAL_UPDATE);
        posLat =(TextView) v.findViewById(R.id.latitude);
        posLong =(TextView) v.findViewById(R.id.longitude);




        updateDialog = new Dialog(getContext());

        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setContentView(R.layout.record_person_info_update_dialog);
        updateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialogFirstName =(EditText) updateDialog.findViewById(R.id.firstName);
        dialogMiddleName =(EditText) updateDialog.findViewById(R.id.middleName);
        dialogLastName =(EditText) updateDialog.findViewById(R.id.lastName);

        dialogAddress1 =(EditText) updateDialog.findViewById(R.id.address1);
        dialogAddress2 =(EditText) updateDialog.findViewById(R.id.address2);
        dialogPostalCode =(EditText) updateDialog.findViewById(R.id.postalCode);
        dialogWeight =(EditText) updateDialog.findViewById(R.id.weight);
        dialogHeight =(EditText) updateDialog.findViewById(R.id.height);
        dialogPosLat =(TextView) updateDialog.findViewById(R.id.longitude);
        dialogPosLong =(TextView) updateDialog.findViewById(R.id.latitude);
        dialogCountry =(TextView) updateDialog.findViewById(R.id.countryUpdate);
        dialogDistrict =(TextView) updateDialog.findViewById(R.id.districtUpdate);


        updateConfirm =(TextView) updateDialog.findViewById(R.id.updateConfirm);



        personalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogFirstName.setText(firstName.getText().toString());
                dialogMiddleName.setText(middleName.getText().toString());
                dialogLastName.setText(lastName.getText().toString());

                dialogCountry.setText(country.getText().toString());
                dialogDistrict.setText(district.getText().toString());

                dialogAddress1.setText(address1.getText().toString());
                dialogAddress2.setText(address2.getText().toString());
                dialogPostalCode.setText(postalCode.getText().toString());
                dialogHeight.setText(height.getText().toString());
                dialogWeight.setText(weight.getText().toString());
                dialogPosLong.setText(posLong.getText().toString());
                dialogPosLat.setText(posLat.getText().toString());
                updateDialog.show();


            }
        });

        updateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
               String updateFirstName = dialogFirstName.getText().toString();
               String updateMiddleName= dialogMiddleName.getText().toString();
               String updateLastName = dialogLastName.getText().toString();

                String updateCountry = dialogCountry.getText().toString();
                String updateDistrict = dialogDistrict.getText().toString();
               String updateAddress1 = dialogAddress1.getText().toString();
               String updateAddress2 = dialogAddress2.getText().toString();
                String updatePostalCode = dialogPostalCode.getText().toString();
                String updateHeight = dialogHeight.getText().toString();
                String updateWeight = dialogWeight.getText().toString();
                String updateLongitude = dialogPosLong.getText().toString();
                String updateLatitude = dialogPosLat.getText().toString();

                DataBaseAccessor dataBaseAccessorTab2 = new DataBaseAccessor(getContext());

                dataBaseAccessorTab2.updateContact(new Contact(child_ID,updateLatitude,updateLongitude, updateFirstName, updateMiddleName, updateLastName, updateCountry, updateDistrict, updateAddress1, updateAddress2, updatePostalCode, updateWeight, updateHeight));
                showProgress(false);
                updateDialog.dismiss();

                Toast.makeText(getActivity(),"정보가 업데이트 되었습니다", Toast.LENGTH_LONG).show();
                bringTheInfo(child_ID);
            }
        });


        bringTheInfo(child_ID);


        return v;
    }

    public void bringTheInfo(String theID) {

        DataBaseAccessor dataBaseAccessor2 = new DataBaseAccessor(getActivity());
        Contact contact = dataBaseAccessor2.getContact(theID);

        firstName.setText(contact.getFirstNameM());

        middleName.setText(contact.getMiddleNameM());
        lastName.setText(contact.getLastNameM());
        country.setText(contact.getDialogCountryM());
        district.setText(contact.getDialogDistrictM());
        address1.setText(contact.getAddress1M());
        address2.setText(contact.getAddress2M());
        postalCode.setText(contact.getPostalCodeM());
        weight.setText(contact.getWeightM());
        height.setText(contact.getHeightM());
        posLong.setText(contact.getPosLong());
        posLat.setText(contact.getPosLat());


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




}