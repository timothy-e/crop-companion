package com.example.cs446_group8.ui.home;

import android.os.Bundle;
import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

public class HomeActivity extends BaseActivity implements HomeContract {

    HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
        mPresenter = new HomePresenter(this, this);

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
