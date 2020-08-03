package com.example.cs446_group8.ui.project_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Project;
import com.example.cs446_group8.data.ProjectDao;
import com.example.cs446_group8.databinding.ActivityProjectSettingsLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import java.time.LocalDate;


public class ProjectSettingsActivity extends BaseActivity implements ProjectSettingsContract {

    private ProjectSettingsContract.Presenter mPresenter;
    private String projectName = "Project Name";
    private long projectId = -1;
    private ProjectDao projectDao;
    private Project curProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        final AppDatabase db = AppDatabase.getInstance(context);
        projectDao = db.projectDao();

        Intent mIntent = getIntent();
        projectId = mIntent.getLongExtra("projectId", GlobalConstants.ID_DOES_NOT_EXIST);

        ActivityProjectSettingsLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_project_settings_layout);
        mPresenter = new ProjectSettingsPresenter(this, this);

        curProject = new Project();

        // existing project
        if (projectId != GlobalConstants.ID_DOES_NOT_EXIST) {
            curProject = projectDao.loadOneById(projectId);
            binding.setProjectName(projectName);
            binding.setProjectName(curProject.getName());

            LocalDate ld = curProject.getBeginningOfSession();
            DatePicker dp = (DatePicker) findViewById(R.id.start_date_field);
            dp.updateDate(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());

            binding.setCalPerDayPerPerson(String.valueOf(curProject.getCaloriesPerDayPerPerson()));
            binding.setCalLeafyGreens(String.valueOf(curProject.getCaloriesFromGreen()));
            binding.setCalColourful(String.valueOf(curProject.getCaloriesFromColorful()));
            binding.setCalStarches(String.valueOf(curProject.getCaloriesFromStarch()));
        }

        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> mPresenter.goBackToDetails(projectId));
        binding.saveButton.setOnClickListener(view -> saveProject());
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
        LocalDate newStartDate = LocalDate.of(year, month, day);
        curProject.setBeginningOfSession(newStartDate);

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

        projectDao.update(curProject);
        mPresenter.showSuccessToast();
    }
}
