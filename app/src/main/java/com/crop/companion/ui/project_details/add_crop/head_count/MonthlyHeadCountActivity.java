package com.crop.companion.ui.project_details.add_crop.head_count;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crop.companion.GlobalConstants;
import com.crop.companion.R;
import com.crop.companion.data.AppDatabase;
import com.crop.companion.data.HeadCounts;
import com.crop.companion.data.Project;
import com.crop.companion.data.ProjectDao;
import com.crop.companion.ui.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MonthlyHeadCountActivity extends BaseActivity implements MonthlyHeadCountContract {

    private MonthlyHeadCountContract.Presenter mPresenter;
    private TextView[] monthTextViews = new TextView[12];
    private EditText[] headCountEditTexts = new EditText[12];
    private TextView[] bedsRequiredTextViews = new TextView[12];
    private long projectId;
    private String fromActivity;

    private String crop;

    private ProjectDao projectDao;
    private Project project;

    private static final List<String> MONTH_NAMES = Collections.unmodifiableList(
            Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_head_count_layout);
        mPresenter = new MonthlyHeadCountPresenter(this, this);

        Intent intent = getIntent();
        crop = intent.getStringExtra(GlobalConstants.CROP_KEY);
        projectId = intent.getLongExtra("projectId", GlobalConstants.ID_DOES_NOT_EXIST);
        fromActivity = intent.getStringExtra("FROM_ACTIVITY");

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        projectDao = db.projectDao();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> onBackPressed());

        FloatingActionButton saveButton = findViewById(R.id.floatingActionButton);
        saveButton.setOnClickListener(view -> saveHeadcounts(true));

        LinearLayout monthsContainer = findViewById(R.id.months_container);

        project = projectDao.loadOneById(projectId);

        int firstMonth = project.getBeginningOfSession().getMonth().ordinal();
        for (int monthIndex = 0; monthIndex < MONTH_NAMES.size(); monthIndex++) {
            // we want to label a month based on the start date of the project
            String monthName = MONTH_NAMES.get((monthIndex + firstMonth) % 12);

            Month month = Month.of(monthIndex + 1);
            monthTextViews[monthIndex] = createMonthTextView(this, monthName);
            int headCount = project.getHeadCounts().get(month);
            headCountEditTexts[monthIndex] = createHeadcountInput(this, project, month, headCount);
            bedsRequiredTextViews[monthIndex] = createBedCountTextView(this);

            LinearLayout monthContainer = new LinearLayout(this);
            monthContainer.setOrientation(LinearLayout.HORIZONTAL);
            monthContainer.addView(monthTextViews[monthIndex]);
            monthContainer.addView(headCountEditTexts[monthIndex]);
            monthContainer.addView(bedsRequiredTextViews[monthIndex]);

            monthsContainer.addView(monthContainer);
        }

        mPresenter.changedHeadCount(project, project.getHeadCounts());
    }

    private TextView createMonthTextView(Context context, String monthName) {
        TextView textView = new TextView(context);
        textView.setText(monthName);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.2f
        ));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return textView;
    }

    private void saveHeadcounts(boolean changeScreen) {
        HeadCounts headCounts = project.getHeadCounts();
        for (Month month : Month.values()) {
            headCounts.set(month, parseHeadCount(month));
        }
        projectDao.update(project);
        if (changeScreen) {
            mPresenter.saveButtonClicked(projectId, fromActivity);
        }
    }

    private EditText createHeadcountInput(Context context, Project project, final Month month, int initialValue) {
        EditText editText = new EditText(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        params.setMargins(60, 0, 20, 0);
        editText.setLayoutParams(params);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setText(Integer.toString(initialValue));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setFollowingHeadCountHints(project, month, parseHeadCount(month));
                HeadCounts newHeadCounts = HeadCounts.empty();
                for (Month month : Month.values()) {
                    newHeadCounts.set(month, parseHeadCount(month));
                }
                mPresenter.changedHeadCount(project, newHeadCounts);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return editText;
    }

    private TextView createBedCountTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        return textView;
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

    @Override
    public void setFollowingHeadCountHints(Project project, Month currentMonth, int headCount) {
        final int nextMonthIndex = currentMonth.getValue();
        final int lastMonthIndex = Month.DECEMBER.getValue() - 1;
        for (int i = nextMonthIndex; i <= lastMonthIndex; i++) {
            if (TextUtils.isEmpty(headCountEditTexts[i].getText())) {
                headCountEditTexts[i].setHint(String.valueOf(headCount));
            } else {
                // don't set anything that's already had it's hint changed
                break;
            }
        }
    }

    @Override
    public void setBedCount(Month month, int bedCount) {
        int monthIndex = month.getValue() - 1;
        bedsRequiredTextViews[monthIndex].setText(String.valueOf(bedCount));
    }

    private int parseHeadCount(Month month) {
        int monthIndex = month.getValue() - 1;
        // TODO: do we need error handling?
        String text = headCountEditTexts[monthIndex].getText().toString();
        if (TextUtils.isEmpty(text)) {
            // if unset, use the default value of 0
            return 0;
        } else {
            // if set, use that value
            return Integer.parseInt(text);
        }
    }
}
