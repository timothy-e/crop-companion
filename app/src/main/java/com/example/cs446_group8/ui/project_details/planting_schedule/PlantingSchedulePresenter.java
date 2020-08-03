package com.example.cs446_group8.ui.project_details.planting_schedule;

import android.content.Context;
import android.util.Log;

import com.example.cs446_group8.calculations.Plantings;
import com.example.cs446_group8.data.AppDatabase;
import com.example.cs446_group8.data.Crop;
import com.example.cs446_group8.data.CropSchedule;
import com.example.cs446_group8.ui.BasePresenter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class PlantingSchedulePresenter extends BasePresenter implements PlantingScheduleContract.Presenter {

    private PlantingScheduleContract mView;
    final private AppDatabase db;
    private Plantings plantings;

    public class SingleWeekCrop {
        public Crop crop;
        public Integer plantingAmount;

        SingleWeekCrop (Crop cr, Integer amount) {
            crop = cr;
            plantingAmount = amount;
        }
    }

    PlantingSchedulePresenter(@NonNull Context context, @NonNull PlantingScheduleContract view) {
        super.subscribe(context);
        this.mView = view;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public HashMap<LocalDate, List<SingleWeekCrop>> getSchedule(long id) {
        plantings = new Plantings(db, id);
        List<CropSchedule> schedule = plantings.getSchedule();

        HashMap<LocalDate, List<SingleWeekCrop>> scheduleMap = new HashMap<>(); //return this to activity, calced once on activity start
        for (CropSchedule cropSchedule : schedule) {
            LocalDate firstDate = cropSchedule.firstPlanting;
            for (int i=0; i<cropSchedule.weeklyPlantingAmounts.size(); i++) {
                addCropAmountToDate(firstDate.plusWeeks(i), cropSchedule.crop, cropSchedule.weeklyPlantingAmounts.get(i),
                        scheduleMap);
            }
        }

        return scheduleMap;
    }

    @Override
    public LocalDate getFirstDateOfWeek(LocalDate date) {
        int day = date.getDayOfWeek().getValue();
        return date.minusDays(day);
    }

    @Override
    public YearMonth getFirstMonthOfProject(HashMap<LocalDate, List<SingleWeekCrop>> scheduleMap) {
        LocalDate date = null;
        for (Map.Entry<LocalDate, List<SingleWeekCrop>> set : scheduleMap.entrySet()) {
            if (date != null) {
                if (set.getKey().isBefore(date)) {
                    date = set.getKey();
                }
            } else {
                date = set.getKey();
            }
        }

        return YearMonth.of(date.getYear(), date.getMonth());
    }

    @Override
    public YearMonth getLastMonthOfProject(HashMap<LocalDate, List<SingleWeekCrop>> scheduleMap) {
        LocalDate date = null;
        for (Map.Entry<LocalDate, List<SingleWeekCrop>> set : scheduleMap.entrySet()) {
            if (date != null) {
                if (set.getKey().isAfter(date)) {
                    date = set.getKey();
                }
            } else {
                date = set.getKey();
            }
        }

        return YearMonth.of(date.getYear(), date.getMonth());
    }

    void addCropAmountToDate(LocalDate date, Crop crop, Integer amount, HashMap<LocalDate, List<SingleWeekCrop>> scheduleMap) {
        List<SingleWeekCrop> list = scheduleMap.get(date);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new SingleWeekCrop(crop, amount));
        scheduleMap.put(date,list);
    }

}
