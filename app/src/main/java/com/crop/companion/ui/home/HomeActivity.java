package com.crop.companion.ui.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.crop.companion.R;
import com.crop.companion.data.AppDatabase;
import com.crop.companion.data.Project;
import com.crop.companion.data.ProjectDao;
import com.crop.companion.ui.BaseActivity;
import com.crop.companion.ui.SimpleRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends BaseActivity implements HomeContract {

    private HomeContract.Presenter mPresenter;
    private ProjectDao projectDao;
    private List<Project> projects;
    private ArrayAdapter<ProjectListItem> adapter;

    private SimpleRecyclerAdapter simpleAdapter;

    public class ProjectListItem {
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

        RecyclerView rView = findViewById(R.id.project_list_view);
        simpleAdapter = new SimpleRecyclerAdapter() {
            @Override
            public void onBindViewHolder(SimpleRecyclerAdapter.SimpleViewHolder holder, int position) {
                ProjectListItem p1 = (ProjectListItem) simpleAdapter.list.get(position);
                holder.name.setText(p1.name);
                holder.mView.setOnClickListener(view -> {mPresenter.projectClicked(p1.projectId);});
            }
        };
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setItemAnimator(new DefaultItemAnimator());
        simpleAdapter.list.clear();
        simpleAdapter.list.addAll(projectList);
        rView.setAdapter(simpleAdapter);

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
