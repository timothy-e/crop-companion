package com.example.cs446_group8.ui.projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.projects.project_details.ProjectDetailsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProjectsFragment extends Fragment implements ProjectsContract {
    private ProjectsContract.Presenter presenter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        presenter = new ProjectsPresenter(context, this);

        Button button = view.findViewById(R.id.go_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToProject();
            }
        });
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToProject() {
        Intent intent = new Intent(context, ProjectDetailsActivity.class);
        startActivity(intent);
    }

}
