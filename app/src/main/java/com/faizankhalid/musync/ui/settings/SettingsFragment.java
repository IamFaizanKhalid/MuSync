package com.faizankhalid.musync.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.faizankhalid.musync.R;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {

    private ArrayList<Setting> settingList;
    private ListView settingView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        settingView = (ListView) root.findViewById(R.id.list_settings);
        settingList = new ArrayList<Setting>();
        settingList.add(new Setting("Setting 1", "Description for setting 1."));
        settingList.add(new Setting("Setting 2", "Description for setting 2."));
        settingList.add(new Setting("Setting 3", "Description for setting 3."));
        settingList.add(new Setting("Setting 4", "Description for setting 4."));

        SettingAdapter settingAdt = new SettingAdapter(this.getContext(), settingList);
        settingView.setAdapter(settingAdt);

        return root;
    }
}

