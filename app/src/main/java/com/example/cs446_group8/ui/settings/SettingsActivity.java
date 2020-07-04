package com.example.cs446_group8.ui.settings;

import android.os.Bundle;
import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

public class SettingsActivity extends BaseActivity implements SettingsContract {

    SettingsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_layout);
        mPresenter = new SettingsPresenter(this, this);

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
