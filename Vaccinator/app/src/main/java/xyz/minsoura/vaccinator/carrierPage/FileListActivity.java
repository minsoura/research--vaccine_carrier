package xyz.minsoura.vaccinator.carrierPage;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import xyz.minsoura.vaccinator.R;

/**
 * Created by 사용자 on 2017-08-08.
 */

public class FileListActivity extends Activity {
    private static final int LED_NOTIFICATION_ID = 0;
    private static final String TAG = "FileListActivity";
    ArrayAdapter<String> storedFilesArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_files_list);
        storedFilesArrayAdapter = new ArrayAdapter<String>(this, R.layout.file_name );
        //ArrayAdapter<String> storedFilesArrayAdapter = new ArrayAdapter<String>(this, R.layout.file_name );
        ListView fileListView = (ListView) findViewById(R.id.stored_files);
        fileListView.setAdapter(storedFilesArrayAdapter);
        displayFileLIst();
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),((TextView)adapterView.getChildAt(i)).getText(), Toast.LENGTH_SHORT).show();
                String fileName = ((TextView)adapterView.getChildAt(i)).getText().toString();

                if(fileName.contains("txt")){
                    Intent fileViewIntent = new Intent(getApplicationContext(), FileViewActivity.class);
                    fileViewIntent.putExtra("fileName", fileName);
                    startActivity(fileViewIntent);
                }


            }
        });

    }

    public void displayFileLIst(){
        File[] fileList = getFilesDir().listFiles();
        findViewById(R.id.title_stored_files).setVisibility(View.VISIBLE);
        if(fileList.length>0){
            for(File oneFile: fileList){
                storedFilesArrayAdapter.add(oneFile.getName());
            }
        }else{
            String noFiles = getResources().getText(R.string.no_files).toString();
            storedFilesArrayAdapter.add(noFiles);

        }

    }



}
