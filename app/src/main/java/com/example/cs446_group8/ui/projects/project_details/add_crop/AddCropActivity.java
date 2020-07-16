package com.example.cs446_group8.ui.projects.project_details.add_crop;

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

public class AddCropActivity extends BaseActivity implements AddCropContract, SearchView.OnQueryTextListener {

    private AddCropContract.Presenter mPresenter;

    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter <String> adapter;
    private Filter filter;

    private final String[] crops = new String[]{"Corn", "Cabbage", "Cauliflower", "Lettuce", "Tomatoes"};

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

        searchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                crops
        ));
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPresenter.cropSelected((String) listView.getItemAtPosition(position));
            }
        });
        filter = adapter.getFilter();
        setupSearchView();

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

    private void setupSearchView () {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint(getString(R.string.crop_search_hint));
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)) {
            filter.filter(null);
        } else {
            filter.filter(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
}
