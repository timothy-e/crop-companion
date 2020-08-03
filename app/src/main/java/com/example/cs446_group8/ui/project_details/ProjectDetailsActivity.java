package com.example.cs446_group8.ui.project_details;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowWithCrop;
import com.example.cs446_group8.databinding.ActivityProjectDetailsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import org.jetbrains.annotations.NotNull;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDetailsActivity extends BaseActivity implements ProjectDetailsContract {

    private ProjectDetailsContract.Presenter mPresenter;
    private String projectName;
    private long projectId;
    private ProjectDao projectDao;
    private ArrayAdapter<CropListItem> adapter;
    private ListView listView;

    class CropListItem {
        long cropId;
        String name;

        @NotNull
        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntent = getIntent();
        projectId = mIntent.getLongExtra("projectId", GlobalConstants.ID_DOES_NOT_EXIST);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        ProjectWithSows projectWithSows = projectDao.loadOneByIdWithSows(projectId);
        projectName = projectWithSows.getProject().getName();

        // get crops on the project
        List<Crop> crops = projectWithSows.getSows().stream().map(SowWithCrop::getCrop).collect(Collectors.toList());
        List<CropListItem> cropList = new ArrayList<>();

        // nicely formatted object for the list
        for (int i = 0; i < crops.size(); i++) {
            CropListItem cli = new CropListItem();
            cli.cropId = crops.get(i).getId();
            cli.name = crops.get(i).getName();
            cropList.add(cli);
        }

        ActivityProjectDetailsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_details_layout);
        mPresenter = new ProjectDetailsPresenter(this, this);

        listView = findViewById(R.id.crops_list);
        listView.setAdapter(adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cropList
        ));

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            CropListItem c1 = (CropListItem) listView.getItemAtPosition(i);
            mPresenter.cropClicked(c1.name, projectName);
        });

        binding.setProjectName(projectName);
        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> mPresenter.backButtonClicked());

        binding.settingsButton.setOnClickListener(view -> mPresenter.settingsButtonClicked(projectId));

        binding.headcountsButton.setOnClickListener(view -> mPresenter.headcountsButtonClicked(projectId));

        binding.addButton.setOnClickListener(view -> mPresenter.addButtonClicked(projectId));

        binding.plantingScheduleButton.setOnClickListener(view -> mPresenter.scheduleButtonClicked(projectId));

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

    @Override
    public void onBackPressed() {
        mPresenter.backButtonClicked();
    }
}
