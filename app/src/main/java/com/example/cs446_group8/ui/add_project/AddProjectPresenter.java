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
    public void saveButtonClicked() {
        Intent intent = new Intent(context, MonthlyHeadCountActivity.class);
        mView.launchActivity(intent);
    }
}
