package com.example.cs446_group8.ui.project_details.add_crop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropDao;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectDao_Impl;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowWithCrop;
import com.example.cs446_group8.databinding.ActivityAddCropLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddCropActivity extends BaseActivity implements AddCropContract {

    private AddCropContract.Presenter mPresenter;

    private ListView listView;

    ArrayList<String> cropList;
    private ProjectDao projectDao;
    private CropDao cropDao;
    private long projectId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GetDb + DAO for operations
        Context context = getApplicationContext();
        final AppDatabase db = AppDatabase.getInstance(context);
        projectDao = db.projectDao();
        cropDao = db.cropDao();
        Intent mIntent = getIntent();
        projectId  = mIntent.getLongExtra("projectId", -1);
        //Receives set difference of crops the user has not added.
        ProjectWithSows projectSows = projectDao.loadOneByIdWithSows((int)projectId);

        cropList = new ArrayList<String>( cropDao.loadAll().stream().map(crop -> crop.getName()).collect(Collectors.toList()));
        if(projectSows != null) {
            ArrayList<String> cropsInProject = new ArrayList<String>(projectDao.loadOneByIdWithSows((int) projectId).getSows().
                    stream().map(projectWithSow -> projectWithSow.getCrop().getName()).collect(Collectors.toList())); // cropList already existing in project
            cropList.removeAll(cropsInProject);
        }

        ActivityAddCropLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_crop_layout);
        mPresenter = new AddCropPresenter(this, this);

        binding.setPresenter(mPresenter);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listView = findViewById(R.id.list_view);

        CropListAdapter adapter1 = new CropListAdapter(cropList, this);
        listView.setAdapter(adapter1);
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
