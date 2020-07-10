package com.example.cs446_group8.ui.head_count;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.cs446_group8.ui.BasePresenter;

public class MonthlyHeadCountPresenter extends BasePresenter implements MonthlyHeadCountContract.Presenter {

    private MonthlyHeadCountContract mView;

    MonthlyHeadCountPresenter(@NonNull Context context, @NonNull MonthlyHeadCountContract view) {
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
