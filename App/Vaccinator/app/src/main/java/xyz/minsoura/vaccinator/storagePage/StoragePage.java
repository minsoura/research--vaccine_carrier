package xyz.minsoura.vaccinator.storagePage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.minsoura.vaccinator.R;

/**
 * Created by min on 2016-03-03.
 */
public class StoragePage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_storage, container, false);

        return v;
    }



}