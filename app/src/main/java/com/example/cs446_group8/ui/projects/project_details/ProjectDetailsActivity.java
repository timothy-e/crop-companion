package com.example.cs446_group8.ui.projects.project_details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityProjectDetailsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

public class ProjectDetailsActivity extends BaseActivity implements ProjectDetailsContract {

    private ProjectDetailsContract.Presenter mPresenter;
    private String projectName = "Project 1"; //todo replace with actual project name passed from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProjectDetailsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details_layout);
        mPresenter = new ProjectDetailsPresenter(this, this);

        binding.setProjectName(projectName);
        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> onBackPressed());

        binding.plantingScheduleButton.setOnClickListener(view -> mPresenter.scheduleButtonClicked());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // todo update crops list
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }
}
