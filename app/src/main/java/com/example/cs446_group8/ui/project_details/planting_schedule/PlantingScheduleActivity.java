package com.example.cs446_group8.ui.project_details.planting_schedule;

import android.os.Bundle;

import com.example.cs446_group8.GlobalConstants;
import com.example.cs446_group8.R;
import com.example.cs446_group8.databinding.ActivityPlantingScheduleLayoutBinding;
import com.example.cs446_group8.ui.BaseActivity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
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
    private Long projectId;

    private LocalDate selectedDate = null;
    private String selectedWeek = "";
    private DateTimeFormatter monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM");

    private List<WeekSelectionListener> weekSelectionListeners = new ArrayList<>();

    private HashMap<LocalDate, List<PlantingSchedulePresenter.SingleWeekCrop>> scheduleMap;

    class DayViewContainer extends ViewContainer implements WeekSelectionListener {
        CalendarDay day = null;
        View mView;
        public DayViewContainer(@NonNull View view) {
            super(view);
            mView = view;
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

                for (WeekSelectionListener listener : weekSelectionListeners) {
                    listener.weekSelected(day.getDay());
                }
            });
        }

        //todo fix visual week selection lol
        @Override
        public void weekSelected(int date) {
            if (day.getOwner() == DayOwner.THIS_MONTH) {
                LocalDate datee = day.getDate();
                //Log.d("Huhuhuh", "ghdsfhhs");
                binding.calendar.notifyDateChanged(datee);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_planting_schedule_layout);
        binding.setLifecycleOwner(this);

        mPresenter = new PlantingSchedulePresenter(this, this);
        projectId = getIntent().getLongExtra(GlobalConstants.PROJECT_ID_KEY, GlobalConstants.ID_DOES_NOT_EXIST);

        scheduleMap = mPresenter.getSchedule(projectId);
        binding.setSelectedWeek(selectedWeek);

        scheduleAdapter = new ScheduleAdapter();
        binding.eventsRv.setLayoutManager(new LinearLayoutManager(this));
        binding.eventsRv.setItemAnimator(new DefaultItemAnimator());
        binding.eventsRv.setAdapter(scheduleAdapter);

        YearMonth startingMonth = mPresenter.getFirstMonthOfProject(scheduleMap);
        YearMonth endingMonth = mPresenter.getLastMonthOfProject(scheduleMap);
        YearMonth activeMonth;
        if (YearMonth.now().isAfter(startingMonth) && YearMonth.now().isBefore(endingMonth)) { //if the currentMonth is within the project timeline, set active month to it, otherwise set to starting month
            activeMonth = YearMonth.now();
        } else {
            activeMonth = startingMonth;
        }
        binding.calendar.setup(startingMonth, endingMonth, DayOfWeek.SUNDAY);
        binding.calendar.scrollToMonth(activeMonth);

        binding.calendar.setDayBinder(new DayBinder<DayViewContainer>() {
            //todo display total number of individual crops to be planted,total beds planted so far including this week, for each week row
            //todo highlight entire week when a day cell is selected
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                DayViewContainer cont = new DayViewContainer(view);
                weekSelectionListeners.add(cont);
                return cont;
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

            if (selectedDate != null) {
                selectedDate = null;
                // binding.calendar.notifyDateChanged(selectedDate); //todo this gives null error cause stupid kotlin to java decompilation error
                updateAdapterForDate(null);
            }
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
        if (date != null ) {
            LocalDate firstDateOfWeek = mPresenter.getFirstDateOfWeek(date);
            selectedWeek = firstDateOfWeek.toString() + " - " + firstDateOfWeek.plusDays(6).toString();
            List<PlantingSchedulePresenter.SingleWeekCrop> weekCrops = scheduleMap.get(firstDateOfWeek);
            scheduleAdapter.cropList.clear();
            if (weekCrops != null) {
                scheduleAdapter.cropList.addAll(weekCrops);
            }
        } else {
            selectedWeek = "";
            scheduleAdapter.cropList.clear();
        }
        binding.setSelectedWeek(selectedWeek);
        scheduleAdapter.notifyDataSetChanged();
    }

}
