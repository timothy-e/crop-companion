package com.example.cs446_group8.ui.add_project;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.add_crop.head_count.MonthlyHeadCountActivity;

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
    // todo: update signature here and in the Contract class to accept an int param being the id
    //   of the newly created project
    public void saveButtonClicked(long projectId) {
        Intent intent = new Intent(context, MonthlyHeadCountActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("FROM_ACTIVITY", "AddProject");
        mView.launchActivity(intent);
    }
}
