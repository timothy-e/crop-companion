package com.example.cs446_group8.ui.settings;

import android.content.Context;
import com.example.cs446_group8.ui.BasePresenter;
import androidx.annotation.NonNull;

public class SettingsPresenter extends BasePresenter implements SettingsContract.Presenter {

    private SettingsContract mView;

    SettingsPresenter(@NonNull Context context, @NonNull SettingsContract view) {
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
