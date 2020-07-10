package com.example.cs446_group8.ui.head_count;

import android.os.Bundle;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

public class MonthlyHeadCountActivity extends BaseActivity implements MonthlyHeadCountContract {

    private MonthlyHeadCountContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_head_count_layout);
        mPresenter = new MonthlyHeadCountPresenter(this, this);

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
