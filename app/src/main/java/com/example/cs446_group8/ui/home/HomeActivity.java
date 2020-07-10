package com.example.cs446_group8.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cs446_group8.ui.head_count.MonthlyHeadCountActivity;
import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

public class HomeActivity extends BaseActivity implements HomeContract {

    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
        mPresenter = new HomePresenter(this, this);

    }

    public void jumpToHeadCount(View vew) {
        Intent intent = new Intent(this, MonthlyHeadCountActivity.class);
        startActivity(intent);
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
