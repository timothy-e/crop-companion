package com.example.cs446_group8.ui.project_details.add_crop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropDao;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.data.ProjectWithSows;
import com.example.cs446_group8.data.SowDao;
import com.example.cs446_group8.data.SowWithCrop;
import com.example.cs446_group8.databinding.ActivityAddCropLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

public class AddCropActivity extends BaseActivity implements AddCropContract {

    private AddCropContract.Presenter mPresenter;

    private ListView listView;

    private String fromActivity;

    ArrayList<String> cropList;
    private List<Crop> crops;
    private ProjectDao projectDao;
    private CropDao cropDao;
    private SowDao sowDao;
    private long projectId = -1;
    private ArrayAdapter<CropListItem> adapter;
    class CropListItem{
        long cropId;
        String cropName;
        @Override
        public String toString() {
            return cropName;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GetDb + DAO for operations
        Context context = getApplicationContext();
        final AppDatabase db = AppDatabase.getInstance(context);
        projectDao = db.projectDao();
        cropDao = db.cropDao();
        sowDao = db.sowDao();
        Intent mIntent = getIntent();

        projectId  = mIntent.getLongExtra("projectId", -1);
        fromActivity = mIntent.getStringExtra("FROM_ACTIVITY");

        // Receives set difference of crops the user has not added.
        ProjectWithSows projectSows = projectDao.loadOneByIdWithSows(projectId);
           crops = cropDao.loadAll();
        ArrayList<CropListItem> cropItemList = new ArrayList<CropListItem>();
        ArrayList<CropListItem> currentCrops = new ArrayList<CropListItem>();
        if (projectSows != null) {
            List<SowWithCrop> cropWithSows = projectSows.getSows();
            for (int i = 0; i < cropWithSows.size(); i++) {
                CropListItem cli = new CropListItem();
                cli.cropId = crops.get(i).getId();
                cli.cropName = crops.get(i).getName();
                currentCrops.add(cli);
            }
        }
        for (int i =0; i < crops.size(); i++) {
            CropListItem cli = new CropListItem();
            cli.cropId = crops.get(i).getId();
            cli.cropName = crops.get(i).getName();
            if (!find(currentCrops, cli.cropId)) {
                cropItemList.add(cli);
            }
        }

        ActivityAddCropLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_crop_layout);
        mPresenter = new AddCropPresenter(this, this);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(view -> mPresenter.floatingNextPressed(projectId));

        if (!fromActivity.equals("AddProject")) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.floatingContainer);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
            rl.removeView(fab);
        }

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> mPresenter.backPressed(projectId));

        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1
                ,cropItemList));

        CropListAdapter adapter1 = new CropListAdapter(cropItemList, projectId, sowDao, this);
        listView.setAdapter(adapter1);
        binding.setPresenter(mPresenter);
    }
    protected boolean find(ArrayList<CropListItem> currentCrops, long cropId){
        for(CropListItem cli : currentCrops){
            if(cli.cropId == cropId) return true;
        }
        return false;
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
