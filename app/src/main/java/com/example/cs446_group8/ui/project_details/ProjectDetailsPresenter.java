package com.example.cs446_group8.ui.project_details;

import android.content.Context;
import android.content.Intent;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.add_crop.AddCropActivity;

import com.example.cs446_group8.ui.project_details.add_crop.head_count.MonthlyHeadCountActivity;
import com.example.cs446_group8.ui.project_details.planting_schedule.PlantingScheduleActivity;
import com.example.cs446_group8.ui.project_settings.ProjectSettingsActivity;

import androidx.annotation.NonNull;

public class ProjectDetailsPresenter extends BasePresenter implements ProjectDetailsContract.Presenter {

    private ProjectDetailsContract mView;

    ProjectDetailsPresenter(@NonNull Context context, @NonNull ProjectDetailsContract view) {
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
    public void addButtonClicked() {
        Intent intent = new Intent(context, AddCropActivity.class);
        mView.launchActivity(intent);
    }

    @Override
    public void settingsButtonClicked() {
        // todo: need to check if headcounts exist, if headcounts dont exist, add dialog box saying
        //  user needs to enter headcounts first

        Intent intent = new Intent(context, ProjectSettingsActivity.class);
        mView.launchActivity(intent);
    }

    @Override
    public void headcountsButtonClicked() {
        Intent intent = new Intent(context, MonthlyHeadCountActivity.class);
        mView.launchActivity(intent);
    }

    public void scheduleButtonClicked() {
        Intent intent = new Intent(context, PlantingScheduleActivity.class);
        mView.launchActivity(intent);
    }

}
