package com.example.cs446_group8.ui.home;

import com.example.cs446_group8.ui.BaseContract;

import androidx.fragment.app.Fragment;

public interface HomeContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void addButtonClicked();
        void projectClicked(long projectId);
    }

}
