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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xyz.minsoura.vaccinator.storage.Contact;
import xyz.minsoura.vaccinator.storage.DataBaseAccessor2;
import xyz.minsoura.vaccinator.R;

/**
 * Created by min on 2016-03-28.
 */
public class VaccineTab extends Fragment {



    Dialog vaccineDialog;


    private View mProgressView;
    private View wrapper_View;

    Spinner bcgSpinner;
    Spinner polioSpinner1;
    Spinner polioSpinner2;
    Spinner polioSpinner3;
    Spinner dptSpinner1;
    Spinner dptSpinner2;
    Spinner dptSpinner3;
    Spinner measleSpinner;
    Spinner jeSpinner;

    TextView bdgDate;
    TextView polioDate1;
    TextView polioDate2;
    TextView polioDate3;
    TextView dptDate1;
    TextView dptDate2;
    TextView dptDate3;
    TextView measleDate;
    TextView jeDate;

    TextView bcgCheck;
    TextView polioCheck1;
    TextView polioCheck2;
    TextView polioCheck3;
    TextView dptCheck1;
    TextView dptCheck2;
    TextView dptCheck3;
    TextView measleCheck;
    TextView jeCheck;

    TextView bcgDateHome;
    TextView polioDate1Home;
    TextView polioDate2Home;
    TextView polioDate3Home;
    TextView dptDate1Home;
    TextView dptDate2Home;
    TextView dptDate3Home;
    TextView measleDateHome;
    TextView jeDateHome;

    TextView vaccineUpdate;


    TextView vaccineDialogUpdate;

    ArrayAdapter<String> choiceAdapter;
  static  String child_id;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.record_vaccine_info_tab, container, false);


        child_id = getActivity().getIntent().getStringExtra("child_ID");

        bcgCheck = (TextView) v.findViewById(R.id.BCG_CHECK);
        polioCheck1=(TextView) v.findViewById(R.id.POLIO_CHECK_FIRST);
        polioCheck2=(TextView) v.findViewById(R.id.POLIO_CHECK_SECOND);
        polioCheck3 =(TextView) v.findViewById(R.id.POLIO_CHECK_THIRD);
        dptCheck1= (TextView) v.findViewById(R.id.DPT_CHECK_FIRST);
        dptCheck2=(TextView) v.findViewById(R.id.DPT_CHECK_SECOND);
        dptCheck3=(TextView) v.findViewById(R.id.DPT_CHECK_THRID);
        measleCheck=(TextView) v.findViewById(R.id.MEASLE_CHECK);
        jeCheck =(TextView) v.findViewById(R.id.JE_CHECK);

        bcgDateHome =(TextView) v.findViewById(R.id.BCG_DATE);
        polioDate1Home=(TextView) v.findViewById(R.id.POLIO_DATE_FIRST);
        polioDate2Home=(TextView) v.findViewById(R.id.POLIO_DATE_SECOND);

        polioDate3Home=(TextView) v.findViewById(R.id.POLIO_DATE_THRID);
        dptDate1Home=(TextView) v.findViewById(R.id.DPT_DATE_FIRST);
        dptDate2Home=(TextView) v.findViewById(R.id.DPT_DATE_SECOND);
        dptDate3Home=(TextView) v.findViewById(R.id.DPT_DATE_THIRD);
        measleDateHome=(TextView) v.findViewById(R.id.MEASLE_DATE);
        jeDateHome =(TextView) v.findViewById(R.id.JE_DATE);



        choiceAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,new String[]{"NO","YES","UNKNOWN"});

        mProgressView = v.findViewById(R.id.vaccineTab_progress);
        wrapper_View = v.findViewById(R.id.vaccine_wrapper);
        vaccineDialog = new Dialog(getContext());
        vaccineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vaccineDialog.setContentView(R.layout.record_vaccine_info_update_dialog);
        vaccineDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);



         bcgSpinner = (Spinner) vaccineDialog.findViewById(R.id.bcgSpinner);
         polioSpinner1 = (Spinner) vaccineDialog.findViewById(R.id.polioFirstSpinner);
         polioSpinner2= (Spinner) vaccineDialog.findViewById(R.id.polioSecondSpinner);
         polioSpinner3= (Spinner) vaccineDialog.findViewById(R.id.polioThirdSpinner);
         dptSpinner1= (Spinner) vaccineDialog.findViewById(R.id.dptFirstSpinner);
         dptSpinner2= (Spinner) vaccineDialog.findViewById(R.id.dptSecondSpinner);
         dptSpinner3= (Spinner) vaccineDialog.findViewById(R.id.dptThirdSpinner);
         measleSpinner= (Spinner) vaccineDialog.findViewById(R.id.measleSpinner);
         jeSpinner= (Spinner) vaccineDialog.findViewById(R.id.jeSpinner);

         bdgDate =(TextView) vaccineDialog.findViewById(R.id.BCG_DATE);
        bdgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                bdgDate.setText(formatter.format(dateNow));




            }
        });
        bdgDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bdgDate.setText("DATE");
                return true;
            }
        });
         polioDate1=(TextView) vaccineDialog.findViewById(R.id.POLIO_DATE_FIRST);
        polioDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                polioDate1.setText(formatter.format(dateNow));

            }
        });
        polioDate1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                polioDate1.setText("DATE");
                return true;
            }
        });
         polioDate2=(TextView) vaccineDialog.findViewById(R.id.POLIO_DATE_SECOND);
        polioDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                polioDate2.setText(formatter.format(dateNow));

            }
        });
        polioDate2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                polioDate2.setText("DATE");
                return true;
            }
        });
         polioDate3=(TextView) vaccineDialog.findViewById(R.id.POLIO_DATE_THRID);
        polioDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                polioDate3.setText(formatter.format(dateNow));

            }
        });
        polioDate3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                polioDate3.setText("DATE");
                return true;
            }
        });
        dptDate1=(TextView) vaccineDialog.findViewById(R.id.DPT_DATE_FIRST);
        dptDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                dptDate1.setText(formatter.format(dateNow));
            }
        });
        dptDate1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dptDate1.setText("DATE");
                return true;
            }
        });
         dptDate2=(TextView) vaccineDialog.findViewById(R.id.DPT_DATE_SECOND);
        dptDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                dptDate2.setText(formatter.format(dateNow));

            }
        });
        dptDate2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dptDate2.setText("DATE");
                return true;
            }
        });
         dptDate3=(TextView) vaccineDialog.findViewById(R.id.DPT_DATE_THIRD);
        dptDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                dptDate3.setText(formatter.format(dateNow));

            }
        });
        dptDate3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dptDate3.setText("DATE");
                return true;
            }
        });
         measleDate=(TextView) vaccineDialog.findViewById(R.id.MEASLE_DATE);
        measleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                measleDate.setText(formatter.format(dateNow));

            }
        });
        measleDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               measleDate.setText("DATE");
                return true;
            }
        });
         jeDate=(TextView) vaccineDialog.findViewById(R.id.JE_DATE);
        jeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date dateNow = new Date();
                jeDate.setText(formatter.format(dateNow));

            }
        });
        jeDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                jeDate.setText("DATE");
                return true;
            }
        });

         vaccineDialogUpdate=(TextView) vaccineDialog.findViewById(R.id.VACCINE_dialogUpdate);
        vaccineDialogUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVaccineInfo();
            }
        });

        bcgSpinner.setAdapter(choiceAdapter);
        polioSpinner1.setAdapter(choiceAdapter);
        polioSpinner2.setAdapter(choiceAdapter);
        polioSpinner3.setAdapter(choiceAdapter);
        dptSpinner1.setAdapter(choiceAdapter);
        dptSpinner2.setAdapter(choiceAdapter);
        dptSpinner3.setAdapter(choiceAdapter);
        measleSpinner.setAdapter(choiceAdapter);
        jeSpinner.setAdapter(choiceAdapter);

        vaccineUpdate = (TextView) v.findViewById(R.id.VACCINE_UPDATE);
        vaccineUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseAccessor2 bringVaccineRecord = new DataBaseAccessor2(getActivity());
                Contact contact = bringVaccineRecord.getContact(child_id);

                //TODO:


                    bcgSpinner.setSelection(getIndex(bcgSpinner, contact.getBcgCheck()));



                polioSpinner1.setSelection(getIndex(polioSpinner1, contact.getPolioFirstCheck()));

                polioSpinner2.setSelection(getIndex(polioSpinner2, contact.getPolioSecondCheck()));

                polioSpinner3.setSelection(getIndex(polioSpinner3,contact.getPolioThirdCheck()));

                dptSpinner1.setSelection(getIndex(dptSpinner1,contact.getDptFirstCheck()));

                dptSpinner2.setSelection(getIndex(dptSpinner2,contact.getDptSecondCheck()));

                dptSpinner3.setSelection(getIndex(dptSpinner3,contact.getDptThirdCheck()));

                measleSpinner.setSelection(getIndex(measleSpinner,contact.getMeasleCheck()));

                jeSpinner.setSelection(getIndex(jeSpinner,contact.getJeCheck()));

                bdgDate.setText(contact.getBcgDate());
                polioDate1.setText(contact.getPolioFirstDate());
                polioDate2.setText(contact.getPolioSecondDate());
                polioDate3.setText(contact.getPolioThirdDate());
                dptDate1.setText(contact.getDptFirstDate());
                dptDate2.setText(contact.getDptSecondDate());
                dptDate3.setText(contact.getDptThirdDate());
                measleDate.setText(contact.getMeasleDate());
                jeDate.setText(contact.getJeDate());












                vaccineDialog.show();
            }
        });
        if (!child_id.equals("")) {
            startProcess();
        }else{
            Toast.makeText(getContext(),child_id,Toast.LENGTH_LONG).show();
        }

        return v;
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

    public void startProcess(){

        DataBaseAccessor2 dataBaseAccessor2 = new DataBaseAccessor2(getActivity());

        if(dataBaseAccessor2.getContact(child_id)==null){

        }else{
            bringTheInfo(child_id);

        }

    }

    public void updateVaccineInfo(){

        String bcgString= bcgSpinner.getSelectedItem().toString();
        String polioString1 = polioSpinner1.getSelectedItem().toString();
        String  polioString2 = polioSpinner2.getSelectedItem().toString();
        String polioString3 = polioSpinner3.getSelectedItem().toString();
        String dptString1 = dptSpinner1.getSelectedItem().toString();
        String dptString2 = dptSpinner2.getSelectedItem().toString();
        String dptString3 = dptSpinner3.getSelectedItem().toString();
        String measleString = measleSpinner.getSelectedItem().toString();
        String jeString = jeSpinner.getSelectedItem().toString();


         String bcgDateString=bdgDate.getText().toString();
         String polioDateString1=polioDate1.getText().toString();
         String polioDateString2=polioDate2.getText().toString();
         String polioDateString3=polioDate3.getText().toString();
         String dptDateString1=dptDate1.getText().toString();
         String dptDateString2=dptDate2.getText().toString();
         String dptDateString3=dptDate3.getText().toString();
         String measleDateString=measleDate.getText().toString();
         String jeDateString=jeDate.getText().toString();




        DataBaseAccessor2 updateVaccineRecord= new DataBaseAccessor2(getActivity());

        if(updateVaccineRecord.getContact(child_id)==null){
            showProgress(true);
            updateVaccineRecord.addContact(new Contact(child_id, bcgString, bcgDateString, polioString1, polioDateString1, polioString2, polioDateString2, polioString3, polioDateString3, dptString1, dptDateString1, dptString2, dptDateString2, dptString3, dptDateString3, measleString, measleDateString, jeString, jeDateString));
            Toast.makeText(getContext(),"백신접종이 새로 기록 되었습니다.", Toast.LENGTH_LONG).show();
            showProgress(false);
            bringTheInfo(child_id);
            vaccineDialog.dismiss();
        }else{
            showProgress(true);
            updateVaccineRecord.updateContact(new Contact(child_id, bcgString, bcgDateString, polioString1, polioDateString1, polioString2, polioDateString2, polioString3, polioDateString3, dptString1, dptDateString1, dptString2, dptDateString2, dptString3, dptDateString3, measleString, measleDateString, jeString, jeDateString));
            Toast.makeText(getContext(),"백신접종기록이 업데이트 되었습니다",Toast.LENGTH_LONG).show();
            vaccineDialog.dismiss();
            bringTheInfo(child_id);
            showProgress(false);
        }

    }

    public void bringTheInfo(String child_ID){
        DataBaseAccessor2 bringVaccineRecord = new DataBaseAccessor2(getActivity());
        Contact contact = bringVaccineRecord.getContact(child_ID);

        //TODO:

            bcgCheck.setText(contact.getBcgCheck());
            polioCheck1.setText(contact.getPolioFirstCheck());
            polioCheck2.setText(contact.getPolioSecondCheck());
            polioCheck3.setText(contact.getPolioThirdCheck());
            dptCheck1.setText(contact.getDptFirstCheck());
            dptCheck2.setText(contact.getDptSecondCheck());
            dptCheck3.setText(contact.getDptThirdCheck());
            measleCheck.setText(contact.getMeasleCheck());
            jeCheck.setText(contact.getJeCheck());

            bcgDateHome.setText(contact.getBcgDate());
            polioDate1Home.setText(contact.getPolioFirstDate());
            polioDate2Home.setText(contact.getPolioSecondDate());
            dptDate1Home.setText(contact.getDptFirstDate());
            dptDate2Home.setText(contact.getDptSecondDate());
            dptDate3Home.setText(contact.getDptThirdDate());
            measleDateHome.setText(contact.getMeasleDate());
            jeDateHome.setText(contact.getJeDate());




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


