package xyz.minsoura.vaccinator.recordPage;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import xyz.minsoura.vaccinator.R;
import xyz.minsoura.vaccinator.utils.SlidingTabLayout;
import xyz.minsoura.vaccinator.adapter.ViewPagerAdapter2;

/**
 * Created by min on 2016-03-04.
 */
public class InfoActivity extends AppCompatActivity {
    static String child_ID;


    ViewPager pager;
    ViewPagerAdapter2 adapter;

    SlidingTabLayout tabs;
    CharSequence Titles[]={"Person", "Vaccine"};
    int Numboftabs =2;

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

    //TextView longitude;
    //TextView latitude;


    static String  UPLOAD_KEY_CHILD_ID="child_ID";

    //TODO: WebServerLocation
   static public String UPLOAD_URL ="http://minsoura.xyz/uploadRegistrationBoys.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_tab_container);
        adapter = new ViewPagerAdapter2(getSupportFragmentManager(),Titles,Numboftabs);
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

    }





}
