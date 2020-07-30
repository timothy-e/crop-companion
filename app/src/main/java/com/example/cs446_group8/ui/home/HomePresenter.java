package com.example.cs446_group8.ui.home;

import android.content.Context;

import com.example.cs446_group8.ui.BasePresenter;

import androidx.annotation.NonNull;

public class HomePresenter extends BasePresenter implements HomeContract.Presenter {

    private HomeContract mView;

    HomePresenter(@NonNull Context context, @NonNull HomeContract view) {
        super.subscribe(context);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
