package com.example.cs446_group8.ui.projects.project_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.databinding.ActivityProjectSettingsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class ProjectSettingsActivity extends BaseActivity implements ProjectSettingsContract {

    private ProjectSettingsContract.Presenter mPresenter;
    private String projectName = "Project Name";
    private int projectId = 0;
    private ProjectDao projectDao;
    private Project curProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        final AppDatabase db = AppDatabase.getInstance(context);
        projectDao = db.projectDao();

        Intent mIntent = getIntent();
        //projectId = mIntent.getIntExtra("projectId", -1);
        //projectName = mIntent.getStringExtra("projectName", "New Project");

        ActivityProjectSettingsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_settings_layout);
        mPresenter = new ProjectSettingsPresenter(this, this);

        binding.setProjectName(projectName);
        curProject = new Project();

        // existing project
        if (projectId != -1 && projectName != "New Project") {
            curProject = projectDao.loadOneById(projectId);
            binding.setProjectName(curProject.getName());
            //binding.setCalPerDayPerPerson(curProject.getCaloriesPerDayPerPerson());
            //binding.setCalLeafyGreens(curProject.getCaloriesFromGreen());
            //binding.setCalColourful(curProject.getCaloriesFromColorful());
            //binding.setCalStarches(curProject.getCaloriesFromStarch());
        }

        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProject();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    protected void saveProject() {
        // grab and set name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String newName = nameField.getText().toString();
        curProject.setName(newName);

        DatePicker datePickerField = (DatePicker) findViewById(R.id.start_date_field);
        int month = datePickerField.getMonth();
        int day = datePickerField.getDayOfMonth();
        int year = datePickerField.getYear();
        Date newStartDate = new Date(year, month, day);
        LocalDate ld = newStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        curProject.setBeginningOfSession(ld);

        // grab and set calories per day per person
        EditText calPerDayPerPersonField = (EditText) findViewById(R.id.calories_per_day_per_person_field);
        int newCalPerDayPerPerson = Integer.parseInt(calPerDayPerPersonField.getText().toString());
        curProject.setCaloriesPerDayPerPerson(newCalPerDayPerPerson);

        // grab and set calories from leafy greens
        EditText calLeafyGreensField = (EditText) findViewById(R.id.calories_leafy_greens_field);
        int newCalLeafyGreens = Integer.parseInt(calLeafyGreensField.getText().toString());
        curProject.setCaloriesFromGreen(newCalLeafyGreens);

        // grab and set calories from colourful vegetables
        EditText calColourfulField = (EditText) findViewById(R.id.calories_colourful_veg_field);
        int newCalColourful = Integer.parseInt(calColourfulField.getText().toString());
        curProject.setCaloriesFromColorful(newCalColourful);

        // grab and set calories from starches
        EditText calStarchesField = (EditText) findViewById(R.id.calories_starches_field);
        int newCalStarches = Integer.parseInt(calStarchesField.getText().toString());
        curProject.setCaloriesFromStarch(newCalStarches);

        // save operation
        projectDao.update(curProject);
    }
}
