package com.example.cs446_group8.ui.home;

import android.content.Context;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BasePresenter;
import com.example.cs446_group8.ui.projects.ProjectsFragment;

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

    @Override
    public boolean navigationTabClicked(int itemId) {
        switch(itemId) {
            case R.id.page_1 :
                mView.openFragment(new ProjectsFragment());
                return true;
            case R.id.page_2 :
                //todo open climate fragment
                return true;
            case R.id.page_3 :
                //todo open trends fragment
                return true;
            case R.id.page_4 :
                //todo open analytics fragment
                return true;
            case R.id.page_5 :
                //todo open settings fragment
                return true;
            default:
                return false;
        }
    }
}
