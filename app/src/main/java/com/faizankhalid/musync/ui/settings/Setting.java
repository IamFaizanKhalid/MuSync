package com.faizankhalid.musync.ui.settings;

public class Setting {

    String title;
    String description;

    public  Setting(String setting_title, String setting_description)
    {
        title=setting_title;
        description=setting_description;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }
}
