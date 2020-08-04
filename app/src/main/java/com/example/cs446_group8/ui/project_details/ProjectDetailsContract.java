package com.example.cs446_group8.ui.project_details;

import com.example.cs446_group8.ui.BaseContract;

public interface ProjectDetailsContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void addButtonClicked(long projectId);
        void settingsButtonClicked(long projectId);
        void headcountsButtonClicked(long projectId);
        void scheduleButtonClicked(long projectId);
        void backButtonClicked();
        void cropClicked(String cropName, String projectName);
        void showNoCropsToast();
    }

}
