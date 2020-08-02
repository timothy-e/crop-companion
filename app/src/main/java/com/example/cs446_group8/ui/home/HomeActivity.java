package com.example.cs446_group8.ui.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements HomeContract {

    private HomeContract.Presenter mPresenter;
    private ProjectDao projectDao;
    private List<Project> projects;
    private ArrayAdapter<ProjectListItem> adapter;
    private ListView listView;

    class ProjectListItem {
        long projectId;
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
        mPresenter = new HomePresenter(this, this);

        ImageView btn = findViewById(R.id.add_button);
        btn.setOnClickListener(v -> mPresenter.addButtonClicked());

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        projects = projectDao.loadAll();

        List<ProjectListItem> projectList = new ArrayList<>();

        // nicely formatted object for the list
        for (int i = 0; i < projects.size(); i++) {
            ProjectListItem pli = new ProjectListItem();
            pli.projectId = projects.get(i).getId();
            pli.name = projects.get(i).getName();
            projectList.add(pli);
        }

        listView = findViewById(R.id.project_list_view);
        listView.setAdapter(adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                projectList
        ));

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            ProjectListItem p1 = (ProjectListItem) listView.getItemAtPosition(i);
            mPresenter.projectClicked(p1.projectId);
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
