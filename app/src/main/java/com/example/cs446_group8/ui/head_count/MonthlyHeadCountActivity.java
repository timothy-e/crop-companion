package com.example.cs446_group8.ui.head_count;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cs446_group8.R;
import com.example.cs446_group8.ui.BaseActivity;

import org.w3c.dom.Text;

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
            headCountEditTexts[i] = createHeadcountInput(this);
            bedsRequiredTextViews[i] = createBedCountTextView(this);

            LinearLayout monthContainer = new LinearLayout(this);
            monthContainer.setOrientation(LinearLayout.HORIZONTAL);
            monthContainer.addView(monthTextViews[i]);
            monthContainer.addView(headCountEditTexts[i]);
            monthContainer.addView(bedsRequiredTextViews[i]);

            monthsContainer.addView(monthContainer);
        }
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

    private EditText createHeadcountInput(Context context) {
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

        return editText;
    }

    private TextView createBedCountTextView(Context context) {
        TextView textView = new TextView(context);
        textView.setText("-");
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


}
