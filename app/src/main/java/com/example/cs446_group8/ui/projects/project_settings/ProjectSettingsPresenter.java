package com.example.cs446_group8.ui.projects.project_settings;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.cs446_group8.ui.BasePresenter;

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

    @Override
    public void saveButtonClicked() {
        //todo: save operation
    }
}
