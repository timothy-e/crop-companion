package com.example.cs446_group8.ui.project_details.planting_schedule;

import android.os.Bundle;

import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityPlantingScheduleLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PlantingScheduleActivity extends BaseActivity implements PlantingScheduleContract {

    private ActivityPlantingScheduleLayoutBinding binding;
    private PlantingScheduleContract.Presenter mPresenter;
    private ScheduleAdapter scheduleAdapter;

    private LocalDate selectedDate = null;
    private String selectedWeek = "";
    private DateTimeFormatter monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM");

    class DayViewContainer extends ViewContainer {
        CalendarDay day = null;

        public DayViewContainer(@NonNull View view) {
            super(view);
            view.setOnClickListener(view1 -> {
                if (day.getOwner() == DayOwner.THIS_MONTH) {
                    LocalDate date = day.getDate();
                    if (selectedDate != date) {
                        LocalDate oldDate = selectedDate;
                        selectedDate = date;
                        binding.calendar.notifyDateChanged(date);
                        if (oldDate != null) {
                            binding.calendar.notifyDateChanged(oldDate);
                        }
                        updateAdapterForDate(date);
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_planting_schedule_layout);
        mPresenter = new PlantingSchedulePresenter(this, this);

        binding.setSelectedWeek(selectedWeek);

        scheduleAdapter = new ScheduleAdapter();
        binding.eventsRv.setLayoutManager(new LinearLayoutManager(this));
        binding.eventsRv.setItemAnimator(new DefaultItemAnimator());
        binding.eventsRv.setAdapter(scheduleAdapter);

        //todo replace with YearMonth objects of actual starting and ending months for project
        YearMonth startingMonth = YearMonth.now();
        YearMonth endingMonth = YearMonth.now().plusMonths(10);
        YearMonth activeMonth;
        if (false) { //todo if the currentMonth is within the project timeline, set active month to it, otherwise set to startingmonth
            activeMonth = YearMonth.now();
        } else {
            activeMonth = startingMonth;
        }
        binding.calendar.setup(startingMonth, endingMonth, DayOfWeek.MONDAY);
        binding.calendar.scrollToMonth(activeMonth);

        binding.calendar.setDayBinder(new DayBinder<DayViewContainer>() {
            //todo display total number of individual crops to be planted,total beds planted so far including this week, for each week row
            //todo highlight entire week when a day cell is selected
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                container.day = day;
                TextView textView = container.getView().findViewById(R.id.day_text);
                View layout = container.getView().findViewById(R.id.day_layout);
                textView.setText(Integer.toString(day.getDate().getDayOfMonth()));

                if (day.getOwner() == DayOwner.THIS_MONTH) {
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    layout.setBackgroundResource((selectedDate == day.getDate()) ? R.drawable.selected_bg : 0);
                } else {
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                    layout.setBackground(null);
                }

            }
        });

        binding.calendar.setMonthHeaderBinder(new MonthHeaderFooterBinder<ViewContainer>() {
            @NonNull
            @Override
            public ViewContainer create(@NonNull View view) {
                return new ViewContainer(view);
            }

            @Override
            public void bind(@NonNull ViewContainer container, @NonNull CalendarMonth month) {
                View layout = container.getView().findViewById(R.id.legendLayout);
                if (layout.getTag() == null) {
                    layout.setTag(month.getYearMonth());
                }
            }
        });

        binding.calendar.setMonthScrollListener(month -> {
            String title = monthTitleFormatter.format(month.getYearMonth()) + " " + month.getYearMonth().getYear();
            binding.monthYearText.setText(title);

            /*if (selectedDate != null) {
                selectedDate = null;
                binding.calendar.notifyDateChanged(selectedDate);
                updateAdapterForDate(null);
            }*/
            return null;
        });

        binding.nextMonthButton.setOnClickListener(view -> {
            if (binding.calendar.findFirstVisibleMonth() != null) {
                binding.calendar.smoothScrollToMonth(binding.calendar.findFirstVisibleMonth().getYearMonth().plusMonths(1));
            }
        });

        binding.previousMonthButton.setOnClickListener(view -> {
            if (binding.calendar.findFirstVisibleMonth() != null) {
                binding.calendar.smoothScrollToMonth(binding.calendar.findFirstVisibleMonth().getYearMonth().minusMonths(1));
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

    private void updateAdapterForDate(@Nullable LocalDate date) {
        //scheduleAdapter.croplist =
        //todo set the list in the adapter to a list of the crops coupled with their amount, to be planted on the week of the selected date
        // get this from whatever structure backend gives as the schedule of crops
    }

}
