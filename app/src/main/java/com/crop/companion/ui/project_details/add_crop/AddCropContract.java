package com.crop.companion.ui.project_details.add_crop;

import com.crop.companion.ui.BaseContract;

public interface AddCropContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void backPressed(long projectId);
        void floatingNextPressed(long projectId);
    }

}
