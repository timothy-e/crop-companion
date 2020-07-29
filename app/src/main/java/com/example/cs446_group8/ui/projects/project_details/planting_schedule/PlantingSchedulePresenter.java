package com.example.cs446_group8.ui.projects.project_details.planting_schedule;

import android.content.Context;

import com.example.cs446_group8.ui.BasePresenter;

import androidx.annotation.NonNull;

public class PlantingSchedulePresenter extends BasePresenter implements PlantingScheduleContract.Presenter {

    private PlantingScheduleContract mView;

    PlantingSchedulePresenter(@NonNull Context context, @NonNull PlantingScheduleContract view) {
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
