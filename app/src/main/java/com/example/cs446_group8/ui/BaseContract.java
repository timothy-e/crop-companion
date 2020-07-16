package com.example.cs446_group8.ui;

import android.content.Intent;

public interface BaseContract {
    interface Presenter {
        void resume();
        void pause();
    }

    void launchActivity(Intent intent);
}
