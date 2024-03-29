package com.crop.companion.ui.add_project;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.crop.companion.ui.BasePresenter;
import com.crop.companion.ui.project_details.add_crop.AddCropActivity;

public class AddProjectPresenter extends BasePresenter implements AddProjectContract.Presenter {

    private AddProjectContract mView;

    AddProjectPresenter(@NonNull Context context, @NonNull AddProjectContract view) {
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
    public void saveButtonClicked(long projectId) {
        Intent intent = new Intent(context, AddCropActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("FROM_ACTIVITY", "AddProject");
        mView.launchActivity(intent);
    }
}
