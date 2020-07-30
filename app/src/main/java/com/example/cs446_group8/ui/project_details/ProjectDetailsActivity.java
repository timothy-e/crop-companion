package com.example.cs446_group8.ui.project_details;

import android.content.Intent;
import android.os.Bundle;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityProjectDetailsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

public class ProjectDetailsActivity extends BaseActivity implements ProjectDetailsContract {

    private ProjectDetailsContract.Presenter mPresenter;
    private String projectName = "Project 1"; //todo replace with actual project name passed from previous activity
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        projectId = mIntent.getIntExtra("projectId", -1);

        // todo (PR): get database instance

        // todo (PR): query for Project WHERE id = projectId (you'll also need to grab the crops associated with the project)

        // todo (PR): once the Project obj is received, get the project name and assign it to the member projectName

        ActivityProjectDetailsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details_layout);
        mPresenter = new ProjectDetailsPresenter(this, this);

        binding.setProjectName(projectName);
        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> onBackPressed());

        binding.settingsButton.setOnClickListener(view -> mPresenter.settingsButtonClicked(projectId));

        binding.headcountsButton.setOnClickListener(view -> mPresenter.headcountsButtonClicked(projectId));

        binding.addButton.setOnClickListener(view -> mPresenter.addButtonClicked(projectId));

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
