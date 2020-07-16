package com.example.cs446_group8.ui.projects;

import android.content.Context;

import com.example.cs446_group8.ui.BasePresenter;

import androidx.annotation.NonNull;

class ProjectsPresenter extends BasePresenter implements ProjectsContract.Presenter {
    private ProjectsContract view;

    ProjectsPresenter(@NonNull Context context, @NonNull ProjectsContract view) {
        super.subscribe(context);
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
