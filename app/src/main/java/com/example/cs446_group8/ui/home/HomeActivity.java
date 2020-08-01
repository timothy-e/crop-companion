package com.example.cs446_group8.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.ui.BaseActivity;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

import java.util.List;

public class HomeActivity extends BaseActivity implements HomeContract {

    private HomeContract.Presenter mPresenter;
    private ProjectDao projectDao;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
        mPresenter = new HomePresenter(this, this);

        Button button = findViewById(R.id.go_btn);
        button.setOnClickListener(view -> jumpToProject());

        ImageView btn = findViewById(R.id.add_button);
        btn.setOnClickListener(v -> mPresenter.addButtonClicked());

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        projects = projectDao.loadAll();

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

    public void jumpToProject() {
        //long projectId;
        Intent intent = new Intent(this, ProjectDetailsActivity.class);
        //intent.putExtra("projectId", projectId);
        startActivity(intent);
    }
}
