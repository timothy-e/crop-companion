package com.example.cs446_group8.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract {

    public void launchActivity (Intent intent) {
        startActivity(intent);
    }

}
