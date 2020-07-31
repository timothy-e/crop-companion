package com.example.cs446_group8.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;
import com.example.cs446_group8.ui.project_details.ProjectDetailsActivity;

public class HomeActivity extends BaseActivity implements HomeContract {

    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
        mPresenter = new HomePresenter(this, this);

        Button button = findViewById(R.id.go_btn);
        button.setOnClickListener(view -> jumpToProject());

        ImageView btn = findViewById(R.id.add_button);
        btn.setOnClickListener(v -> mPresenter.addButtonClicked());

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
        Intent intent = new Intent(this, ProjectDetailsActivity.class);
        startActivity(intent);
    }
}
