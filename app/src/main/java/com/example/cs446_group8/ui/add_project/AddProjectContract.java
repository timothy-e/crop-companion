package com.example.cs446_group8.ui.add_project;


import com.example.cs446_group8.ui.BaseContract;

public interface AddProjectContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void saveButtonClicked();
    }

}
