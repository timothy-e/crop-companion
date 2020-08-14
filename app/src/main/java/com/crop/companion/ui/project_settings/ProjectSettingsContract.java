package com.crop.companion.ui.project_settings;

import com.crop.companion.ui.BaseContract;

public interface ProjectSettingsContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void goBackToDetails(long projectId);
        void showSuccessToast();
    }

}
