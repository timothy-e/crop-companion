package com.crop.companion.ui.project_details.add_crop.head_count;

import com.crop.companion.data.HeadCounts;
import com.crop.companion.data.Project;
import com.crop.companion.ui.BaseContract;

import java.time.Month;

public interface MonthlyHeadCountContract extends BaseContract {
    /**
     * Assigns all (not currently set) months below the current one to have this value as a hint
     * i.e. if we set Apr to 40, then all of May-Dec have 40 as their hint
     * i.e. if we set Nov to 20, then Dec has 20 as it's hint. Then if we set Sept to 30, then
     *      only Oct has 30 as its hint, and Dec stays at 20.
     * @param project the project instance being modified
     * @param currentMonth the current month
     * @param headCount the newly set value
     */
    void setFollowingHeadCountHints(Project project, Month currentMonth, int headCount);

    /**
     * Updates the required bed count associated with the month
     * @param month a month of the year
     * @param bedCount the newly set value
     */
    void setBedCount(Month month, int bedCount);

    interface Presenter extends BaseContract.Presenter {
        /**
         * Notifies the presenter that the given month has had its population count for the month
         * changed to headCount
         * @param project the project instance being modified
         * @param headCounts the new headcounts object we want to use for calculations
         */
        void changedHeadCount(Project project, HeadCounts headCounts);

        void saveButtonClicked(long projectId, String fromActivity);
    }

}
