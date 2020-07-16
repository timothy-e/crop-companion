package com.example.cs446_group8.ui;

import android.content.Context;

import androidx.annotation.NonNull;

public class BasePresenter {
    protected Context context;

    protected void subscribe(@NonNull Context context) {
        this.context = context;
    }

    public boolean isSubscribed() {
        return context != null;
    }
}
