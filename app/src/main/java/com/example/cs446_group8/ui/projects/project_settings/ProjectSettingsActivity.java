package com.example.cs446_group8.ui.projects.project_settings;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityProjectSettingsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;


public class ProjectSettingsActivity extends BaseActivity implements ProjectSettingsContract {

    private ProjectSettingsContract.Presenter mPresenter;
    private String projectName = "Project 1"; //todo replace with actual project name passed from previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProjectSettingsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_settings_layout);
        mPresenter = new ProjectSettingsPresenter(this, this);

        binding.setProjectName(projectName);
        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.saveButtonClicked();
            }
        });
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
