package com.example.cs446_group8.ui.project_details.add_crop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityAddCropLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddCropActivity extends BaseActivity implements AddCropContract {

    private AddCropContract.Presenter mPresenter;

    private ListView listView;

    ArrayList<String> cropList = new ArrayList<String>(
            Arrays.asList("Corn", "Cabbage", "Cauliflower", "Lettuce", "Tomatoes")
    );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
