package com.example.cs446_group8.ui.projects.project_details;

import com.example.cs446_group8.ui.BaseContract;

public interface ProjectDetailsContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void addButtonClicked();
        void settingsButtonClicked();
    }

}
