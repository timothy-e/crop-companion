package com.example.cs446_group8.ui.projects.project_details.add_crop.head_count;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.projects.project_details.ProjectDetailsActivity;
import com.example.cs446_group8.ui.projects.project_details.add_crop.crop_summary.CropSummaryActivity;

public class MonthlyHeadCountPresenter extends BasePresenter implements MonthlyHeadCountContract.Presenter {

    private MonthlyHeadCountContract mView;

    MonthlyHeadCountPresenter(@NonNull Context context, @NonNull MonthlyHeadCountContract view) {
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
    public void changedHeadCount(int month, int headCount) {
        mView.setFollowingHeadCountHints(month, headCount);

        // TODO: let the backend know about this

        mView.setBedCount(month, getRequiredBeds(month, headCount));
    }

    //todo : remove this probably
    @Override
    public void saveButtonClicked() {
        //Intent intent = new Intent(context, ProjectDetailsActivity.class);
        //Intent intent = new Intent(context, CropSummaryActivity.class);
        //intent.putExtra(GlobalConstants.CROP_KEY, crop);
        //mView.launchActivity(intent);
    }

    private int getRequiredBeds(int month, int headCount) {
        return (int) Math.pow(headCount, 2); // sophisticated backend calculation
    }
}
