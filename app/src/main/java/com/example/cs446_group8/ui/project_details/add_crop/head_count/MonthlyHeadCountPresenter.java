package com.example.cs446_group8.ui.project_details.add_crop.head_count;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

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

    private int getRequiredBeds(int month, int headCount) {
        return (int) Math.pow(headCount, 2); // sophisticated backend calculation
    }

    @Override
    public void saveButtonClicked() {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        mView.launchActivity(intent);
    }
}
