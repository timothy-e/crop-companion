package com.crop.companion.ui.home;

import com.crop.companion.ui.BaseContract;

public interface HomeContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        void addButtonClicked();
        void projectClicked(long projectId);
    }

}
