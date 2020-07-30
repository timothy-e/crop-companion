package com.example.cs446_group8.ui.project_details.add_crop.crop_summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityCropSummaryLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

public class CropSummaryActivity extends BaseActivity implements CropSummaryContract {

    private CropSummaryContract.Presenter mPresenter;

    private String crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCropSummaryLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_crop_summary_layout);
        mPresenter = new CropSummaryPresenter(this, this);

        Intent intent = getIntent();
        crop = intent.getStringExtra(GlobalConstants.CROP_KEY);

        binding.setCropName(crop);
        binding.setPeopleFed(2000);
        binding.setBedsNeeded(150);
        binding.setSeedsNeeded(800);
        binding.setStartDate("August 16, 2020");
        binding.setGrowthTime("30 days");
        binding.setHarvestDate("September 17, 2020");

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.finishSummary();
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

    @Override
    public void onBackPressed() {
        mPresenter.finishSummary();
    }
}
