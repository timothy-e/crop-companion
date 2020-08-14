package com.crop.companion.ui.add_project;


import com.crop.companion.ui.BaseContract;

public interface AddProjectContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void saveButtonClicked(long projectId);
    }

}
