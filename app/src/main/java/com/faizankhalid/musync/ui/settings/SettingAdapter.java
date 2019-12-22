package com.faizankhalid.musync.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faizankhalid.musync.R;

import java.util.ArrayList;
import java.util.Set;

public class SettingAdapter extends BaseAdapter {
    private ArrayList<Setting> settings;
    private LayoutInflater settingInf;


    public SettingAdapter(Context c, ArrayList<Setting> settings){
        this.settings=settings;
        this.settingInf= LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return settings.size();
    }

    @Override
    public Object getItem(int position) {
        return settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout settingLay = (LinearLayout)settingInf.inflate
                (R.layout.setting, parent, false);

        TextView titleView = (TextView)settingLay.findViewById(R.id.setting_title);
        TextView descView = (TextView)settingLay.findViewById(R.id.setting_desc);

        Setting currSetting = settings.get(position);

        titleView.setText(currSetting.getTitle());
        descView.setText(currSetting.getDescription());

        settingLay.setTag(position);
        return settingLay;
    }
}
