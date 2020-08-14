package com.crop.companion.ui.project_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.crop.companion.GlobalConstants;
import com.crop.companion.R;
import com.crop.companion.data.AppDatabase;
import com.crop.companion.data.Project;
import com.crop.companion.data.ProjectDao;
import com.crop.companion.databinding.ActivityProjectSettingsLayoutBinding;
import com.crop.companion.ui.BaseActivity;

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
            dp.updateDate(
                    ld.getYear(),
                    ld.getMonthValue() - 1, // localdate stores 1-12, dp stores needs 0-11
                    ld.getDayOfMonth());

            binding.setCalPerDayPerPerson(String.valueOf(curProject.getCaloriesPerDayPerPerson()));
            binding.setCalLeafyGreens(String.valueOf(curProject.getCaloriesFromGreen()));
            binding.setCalColourful(String.valueOf(curProject.getCaloriesFromColorful()));
            binding.setCalStarches(String.valueOf(curProject.getCaloriesFromStarch()));
        }

        binding.setPresenter(mPresenter);

        binding.backButton.setOnClickListener(view -> mPresenter.goBackToDetails(projectId));
        binding.saveButton.setOnClickListener(view -> { //disgusting lazy error checking lol
            EditText nameField = findViewById(R.id.name_field);
            EditText calPerDayPerPersonField = findViewById(R.id.calories_per_day_per_person_field);
            EditText calLeafyGreensField = findViewById(R.id.calories_leafy_greens_field);
            EditText calColourfulField = findViewById(R.id.calories_colourful_veg_field);
            EditText calStarchesField = findViewById(R.id.calories_starches_field);
            boolean noError = true;
            if (TextUtils.isEmpty(nameField.getText())) {
                noError = false;
                nameField.setError(getString(R.string.empty_field_error));
            }

            if (TextUtils.isEmpty(calPerDayPerPersonField.getText())) {
                noError = false;
                calPerDayPerPersonField.setError(getString(R.string.empty_field_error));
            }

            if (TextUtils.isEmpty(calLeafyGreensField.getText())) {
                noError = false;
                calLeafyGreensField.setError(getString(R.string.empty_field_error));
            } else if (Integer.parseInt(calLeafyGreensField.getText().toString()) < 0 || Integer.parseInt(calLeafyGreensField.getText().toString()) > 100) {
                noError = false;
                calLeafyGreensField.setError(getString(R.string.field_percent_error));
            }

            if (TextUtils.isEmpty(calColourfulField.getText())) {
                noError = false;
                calColourfulField.setError(getString(R.string.empty_field_error));
            } else if (Integer.parseInt(calColourfulField.getText().toString()) < 0 || Integer.parseInt(calColourfulField.getText().toString()) > 100) {
                noError = false;
                calColourfulField.setError(getString(R.string.field_percent_error));
            }

            if (TextUtils.isEmpty(calStarchesField.getText())) {
                noError = false;
                calStarchesField.setError(getString(R.string.empty_field_error));
            } else if (Integer.parseInt(calStarchesField.getText().toString()) < 0 || Integer.parseInt(calStarchesField.getText().toString()) > 100) {
                noError = false;
                calStarchesField.setError(getString(R.string.field_percent_error));
            }

            if (noError) {
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
        int month = datePickerField.getMonth() + 1; // datePicker returns 0-11, we need 1-12
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
