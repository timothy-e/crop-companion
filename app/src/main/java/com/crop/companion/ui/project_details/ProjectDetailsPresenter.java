package com.crop.companion.ui.project_details;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.crop.companion.GlobalConstants;
import com.crop.companion.ui.BasePresenter;
import com.crop.companion.ui.home.HomeActivity;
import com.crop.companion.ui.project_details.add_crop.AddCropActivity;

import com.crop.companion.ui.project_details.add_crop.head_count.MonthlyHeadCountActivity;
import com.crop.companion.ui.project_details.crop_timelapse.timelapseActivity;
import com.crop.companion.ui.project_details.planting_schedule.PlantingScheduleActivity;
import com.crop.companion.ui.project_settings.ProjectSettingsActivity;

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
    public void addButtonClicked(long projectId) {
        Intent intent = new Intent(context, AddCropActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("FROM_ACTIVITY", "ProjectDetails");
        mView.launchActivity(intent);
    }

    @Override
    public void settingsButtonClicked(long projectId) {
        Intent intent = new Intent(context, ProjectSettingsActivity.class);
        intent.putExtra("projectId", projectId);
        mView.launchActivity(intent);
    }

    @Override
    public void headcountsButtonClicked(long projectId) {
        Intent intent = new Intent(context, MonthlyHeadCountActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("FROM_ACTIVITY", "ProjectDetails");
        mView.launchActivity(intent);
    }

    @Override
    public void scheduleButtonClicked(long projectId) {
        Intent intent = new Intent(context, PlantingScheduleActivity.class);
        intent.putExtra(GlobalConstants.PROJECT_ID_KEY, projectId);
        mView.launchActivity(intent);
    }

    // if we're in project details, going back should only send the user to the "home" screen
    //    with the list of projects to select
    @Override
    public void backButtonClicked() {
        Intent intent = new Intent(context, HomeActivity.class);
        mView.launchActivity(intent);
    }

    @Override
    public void cropClicked(String cropName, String projectName) {
        Intent intent = new Intent(context, timelapseActivity.class);
        intent.putExtra("cropName", cropName);
        intent.putExtra("projectName", projectName);
        mView.launchActivity(intent);
    }

    public void showNoCropsToast() {
        Toast.makeText(context, "Please add crops before viewing the planting schedule", Toast.LENGTH_LONG).show();
    }

}
