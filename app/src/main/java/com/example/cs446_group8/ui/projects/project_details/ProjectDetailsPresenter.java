package com.example.cs446_group8.ui.projects.project_details;

import android.content.Context;
import android.content.Intent;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.projects.project_details.add_crop.AddCropActivity;
import com.example.cs446_group8.ui.projects.project_settings.ProjectSettingsActivity;

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
        Intent intent = new Intent(context, ProjectSettingsActivity.class);
        mView.launchActivity(intent);
    }
}
