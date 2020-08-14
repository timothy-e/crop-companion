package com.crop.companion.ui.project_details.add_crop.crop_summary;

import com.crop.companion.ui.BaseContract;

public interface CropSummaryContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void finishSummary();
    }

}
