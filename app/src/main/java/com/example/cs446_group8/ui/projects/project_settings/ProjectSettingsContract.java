package com.example.cs446_group8.ui.projects.project_settings;

import android.view.View;

import com.example.cs446_group8.ui.BaseContract;

public interface ProjectSettingsContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void saveButtonClicked();
        void onRadioButtonClicked(View v);
    }

}
