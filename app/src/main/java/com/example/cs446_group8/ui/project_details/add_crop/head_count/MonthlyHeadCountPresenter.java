package com.example.cs446_group8.ui.project_details.add_crop.head_count;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

import java.time.Month;

public class MonthlyHeadCountPresenter extends BasePresenter implements MonthlyHeadCountContract.Presenter {

    private MonthlyHeadCountContract mView;

    MonthlyHeadCountPresenter(@NonNull Context context, @NonNull MonthlyHeadCountContract view) {
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
    public void changedHeadCount(Project project, Month month, int headCount) {
        mView.setFollowingHeadCountHints(project, month, headCount);
        mView.setBedCount(month, getRequiredBeds(month, headCount));
    }

    private int getRequiredBeds(Month month, int headCount) {
        return (int) Math.pow(headCount, 2); // sophisticated backend calculation
    }

    @Override
    public void saveButtonClicked(long projectId, String fromActivity) {
        if (fromActivity.equals("AddProject")) {
            Intent intent = new Intent(context, ProjectDetailsActivity.class);
            intent.putExtra("projectId", projectId);
            mView.launchActivity(intent);
        }
    }
}
