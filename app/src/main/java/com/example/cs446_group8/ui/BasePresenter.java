package com.example.cs446_group8.ui;

import android.content.Context;

import androidx.annotation.NonNull;

public class BasePresenter {
    protected Context mContext;

    protected void subscribe(@NonNull Context context) {
        this.mContext = context;
    }

    public boolean isSubscribed() {
        return mContext != null;
    }
}
