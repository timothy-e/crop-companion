package com.example.cs446_group8.ui.projects.project_details.add_crop.crop_summary;

import com.example.cs446_group8.ui.BaseContract;

public interface CropSummaryContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void finishSummary();
    }

}