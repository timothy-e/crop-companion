package com.example.cs446_group8.ui.project_details.add_crop.crop_summary;

import android.content.Context;
import android.content.Intent;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

import androidx.annotation.NonNull;

public class CropSummaryPresenter extends BasePresenter implements CropSummaryContract.Presenter {

    private CropSummaryContract mView;

    CropSummaryPresenter(@NonNull Context context, @NonNull CropSummaryContract view) {
        super.subscribe(context);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void finishSummary() {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mView.launchActivity(intent);
    }
}
