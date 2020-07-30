package com.example.cs446_group8.ui.project_details.add_crop;

import android.content.Context;
import android.content.Intent;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.add_crop.head_count.MonthlyHeadCountActivity;

import androidx.annotation.NonNull;

public class AddCropPresenter extends BasePresenter implements AddCropContract.Presenter {

    private AddCropContract mView;

    AddCropPresenter(@NonNull Context context, @NonNull AddCropContract view) {
        super.subscribe(context);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
