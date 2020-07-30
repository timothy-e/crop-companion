package com.example.cs446_group8.ui.project_details;

import com.example.cs446_group8.ui.BaseContract;

public interface ProjectDetailsContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void addButtonClicked(int projectId);
        void settingsButtonClicked(int projectId);
        void headcountsButtonClicked(int projectId);
        void scheduleButtonClicked();
    }

}
