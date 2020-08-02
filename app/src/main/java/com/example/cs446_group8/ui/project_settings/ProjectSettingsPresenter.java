package com.example.cs446_group8.ui.project_settings;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

public class ProjectSettingsPresenter extends BasePresenter implements ProjectSettingsContract.Presenter {

    private ProjectSettingsContract mView;

    ProjectSettingsPresenter(@NonNull Context context, @NonNull ProjectSettingsContract view) {
        super.subscribe(context);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void goBackToDetails(long projectId) {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        intent.putExtra("projectId", projectId);
        mView.launchActivity(intent);

    }
}
