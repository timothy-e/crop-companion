package com.crop.companion.ui.project_details.planting_schedule;

import com.crop.companion.ui.BaseContract;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;

public interface PlantingScheduleContract extends BaseContract {

    interface Presenter extends BaseContract.Presenter {
        HashMap<LocalDate, List<PlantingSchedulePresenter.SingleWeekCrop>> getSchedule(long id);
        LocalDate getFirstDateOfWeek(LocalDate date);
        YearMonth getFirstMonthOfProject(HashMap<LocalDate, List<PlantingSchedulePresenter.SingleWeekCrop>> scheduleMap);
        YearMonth getLastMonthOfProject(HashMap<LocalDate, List<PlantingSchedulePresenter.SingleWeekCrop>> scheduleMap);
    }

}
