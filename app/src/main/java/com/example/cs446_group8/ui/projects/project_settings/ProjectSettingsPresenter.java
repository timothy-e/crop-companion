package com.example.cs446_group8.ui.projects.project_settings;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BasePresenter;

public class ProjectSettingsPresenter extends BasePresenter implements ProjectSettingsContract.Presenter {

    private ProjectSettingsContract mView;

    ProjectSettingsPresenter(@NonNull Context context, @NonNull ProjectSettingsContract view) {
        super.subscribe(context);
        this.mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void saveButtonClicked() {
        //todo: save operation
    }

    @Override
    public void onRadioButtonClicked(View v){
        boolean isChecked = ((RadioButton) v).isChecked();

        switch(v.getId()){
            case R.id.radio_eq_sqft:
                if (isChecked) {
                    //todo
                }
                break;
            case R.id.radio_eq_cal:
                if (isChecked) {
                    //todo
                }
                break;
            case R.id.radio_prop_fav:
                if (isChecked) {
                    //todo
                }
                break;
        }

    }
}
