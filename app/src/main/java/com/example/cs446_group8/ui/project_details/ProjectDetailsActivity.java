package com.example.cs446_group8.ui.project_details;

import android.content.Intent;
import android.os.Bundle;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.databinding.ActivityProjectDetailsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

public class ProjectDetailsActivity extends BaseActivity implements ProjectDetailsContract {

    private ProjectDetailsContract.Presenter mPresenter;
    private String projectName;
    private long projectId;
    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        projectId = mIntent.getLongExtra("projectId", GlobalConstants.ID_DOES_NOT_EXIST);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);
        projectName = projectWithSows.getProject().getName();

        ActivityProjectDetailsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details_layout);
        mPresenter = new ProjectDetailsPresenter(this, this);

        binding.setProjectName(projectName);
        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> mPresenter.backButtonClicked());

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
