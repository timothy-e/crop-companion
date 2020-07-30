package com.example.cs446_group8.ui.add_project;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityAddProjectLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class AddProjectActivity extends BaseActivity implements AddProjectContract {

    private AddProjectContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAddProjectLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_project_layout);
        mPresenter = new AddProjectPresenter(this, this);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        // todo: grab all fields and construct a Project out of it
        // grab and set name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String newName = nameField.getText().toString();

        DatePicker datePickerField = (DatePicker) findViewById(R.id.start_date_field);
        int month = datePickerField.getMonth();
        int day = datePickerField.getDayOfMonth();
        int year = datePickerField.getYear();
        Date newStartDate = new Date(year, month, day);
        LocalDate ld = newStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // grab and set calories per day per person
        EditText calPerDayPerPersonField = (EditText) findViewById(R.id.calories_per_day_per_person_field);
        int newCalPerDayPerPerson = Integer.parseInt(calPerDayPerPersonField.getText().toString());

        // grab and set calories from leafy greens
        EditText calLeafyGreensField = (EditText) findViewById(R.id.calories_leafy_greens_field);
        int newCalLeafyGreens = Integer.parseInt(calLeafyGreensField.getText().toString());

        // grab and set calories from colourful vegetables
        EditText calColourfulField = (EditText) findViewById(R.id.calories_colourful_veg_field);
        int newCalColourful = Integer.parseInt(calColourfulField.getText().toString());

        // grab and set calories from starches
        EditText calStarchesField = (EditText) findViewById(R.id.calories_starches_field);
        int newCalStarches = Integer.parseInt(calStarchesField.getText().toString());

        // todo: insert to db, need to get id of inserted record back so we can pass this
        //  to the next activity (headcounts)

        mPresenter.saveButtonClicked();
    }


}
