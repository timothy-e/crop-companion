package com.example.cs446_group8.ui.head_count;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

public class MonthlyHeadCountActivity extends BaseActivity implements MonthlyHeadCountContract {

    private MonthlyHeadCountContract.Presenter mPresenter;
    private TextView[] monthTextViews = new TextView[12];
    private EditText[] headCountEditTexts = new EditText[12];
    private TextView[] bedsRequiredTextViews = new TextView[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_head_count_layout);
        mPresenter = new MonthlyHeadCountPresenter(this, this);

        LinearLayout monthsContainer = findViewById(R.id.months_container);

        String[] months = new String[]{ "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

        for (int i = 0; i < 12; i++) {
            String monthName = months[i];

            monthTextViews[i] = createMonthTextView(this, monthName);
            headCountEditTexts[i] = createHeadcountInput(this, i);
            bedsRequiredTextViews[i] = createBedCountTextView(this);

            LinearLayout monthContainer = new LinearLayout(this);
            monthContainer.setOrientation(LinearLayout.HORIZONTAL);
            monthContainer.addView(monthTextViews[i]);
            monthContainer.addView(headCountEditTexts[i]);
            monthContainer.addView(bedsRequiredTextViews[i]);

            monthsContainer.addView(monthContainer);
        }

        // calculate beds required
        mPresenter.changedHeadCount(0, getHeadCount(0));
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

    private EditText createHeadcountInput(Context context, final int monthNum) {
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
        editText.setHint("10");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.changedHeadCount(monthNum, getHeadCount(monthNum));
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
    public void setFollowingHeadCountHints(int currentMonth, int headCount) {
        for (int i = currentMonth + 1; i < 12; i++) {
            if (TextUtils.isEmpty(headCountEditTexts[i].getText())) {
                headCountEditTexts[i].setHint(String.valueOf(headCount));

                // now that the hint is changed, we need to recalculate the number of beds required
                mPresenter.changedHeadCount(i, headCount);
            } else {
                // don't set anything that's already had it's hint changed
                break;
            }
        }
    }

    @Override
    public void setBedCount(int month, int bedCount) {
        bedsRequiredTextViews[month].setText(String.valueOf(bedCount));
    }

    private int getHeadCount(int month) {
        if (TextUtils.isEmpty(headCountEditTexts[month].getText())) {
            // if unset, use the hint (default val)
            return Integer.parseInt(headCountEditTexts[month].getHint().toString());
        } else {
            // if set, use that value
            return Integer.parseInt(headCountEditTexts[month].getText().toString());
        }
    }
}
