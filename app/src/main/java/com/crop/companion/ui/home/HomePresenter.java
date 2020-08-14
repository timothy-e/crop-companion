package com.crop.companion.ui.home;

import android.content.Context;
import android.content.Intent;

import com.crop.companion.ui.BasePresenter;
import com.crop.companion.ui.add_project.AddProjectActivity;
import com.crop.companion.ui.project_details.ProjectDetailsActivity;

import androidx.annotation.NonNull;

public class HomePresenter extends BasePresenter implements HomeContract.Presenter {

    private HomeContract mView;

    HomePresenter(@NonNull Context context, @NonNull HomeContract view) {
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
    public void addButtonClicked(){
        Intent intent = new Intent(context, AddProjectActivity.class);
        mView.launchActivity(intent);
    }

    public void projectClicked(long projectId) {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        intent.putExtra("projectId", projectId);
        mView.launchActivity(intent);
    }
}
