package com.crop.companion.ui.add_project;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.crop.companion.R;
import com.crop.companion.data.AppDatabase;
import com.crop.companion.data.HeadCounts;
import com.crop.companion.data.Project;
import com.crop.companion.data.ProjectDao;
import com.crop.companion.databinding.ActivityAddProjectLayoutBinding;
import com.crop.companion.ui.BaseActivity;

import java.time.LocalDate;


public class AddProjectActivity extends BaseActivity implements AddProjectContract {

    private AddProjectContract.Presenter mPresenter;
    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddProjectLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project_layout);
        mPresenter = new AddProjectPresenter(this, this);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        binding.backButton.setOnClickListener(view -> onBackPressed());
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
                insertProjectToDb();
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

    protected void insertProjectToDb() {
        // grab and set name
        EditText nameField = findViewById(R.id.name_field);
        String newName = nameField.getText().toString();

        DatePicker datePickerField = findViewById(R.id.start_date_field);
        int month = datePickerField.getMonth();
        int day = datePickerField.getDayOfMonth();
        int year = datePickerField.getYear();
        LocalDate newStartDate = LocalDate.of(year, month, day);

        // grab and set calories per day per person
        EditText calPerDayPerPersonField = findViewById(R.id.calories_per_day_per_person_field);
        int newCalPerDayPerPerson = Integer.parseInt(calPerDayPerPersonField.getText().toString());

        // grab and set calories from leafy greens
        EditText calLeafyGreensField = findViewById(R.id.calories_leafy_greens_field);
        int newCalLeafyGreens = Integer.parseInt(calLeafyGreensField.getText().toString());

        // grab and set calories from colourful vegetables
        EditText calColourfulField = findViewById(R.id.calories_colourful_veg_field);
        int newCalColourful = Integer.parseInt(calColourfulField.getText().toString());

        // grab and set calories from starches
        EditText calStarchesField = findViewById(R.id.calories_starches_field);
        int newCalStarches = Integer.parseInt(calStarchesField.getText().toString());

        long newlyInsertedProjectId = projectDao.insert(Project.builder()
                .name(newName)
                .beginningOfSession(newStartDate)
                .caloriesPerDayPerPerson(newCalPerDayPerPerson)
                .caloriesFromGreen(newCalLeafyGreens)
                .caloriesFromColorful(newCalColourful)
                .caloriesFromStarch(newCalStarches)
                .headCounts(HeadCounts.empty())
                .build());

        // pass newly created ID of Project obj to the method call below (update signatures as well ofc)
        mPresenter.saveButtonClicked(newlyInsertedProjectId);
    }


}
