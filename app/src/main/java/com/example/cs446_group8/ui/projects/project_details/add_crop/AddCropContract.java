package com.example.cs446_group8.ui.projects.project_details.add_crop;

import com.example.cs446_group8.ui.BaseContract;

public interface AddCropContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void cropSelected(String crop);
    }

}