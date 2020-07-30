package com.example.cs446_group8.ui.project_details.add_crop.head_count;

import com.example.cs446_group8.ui.BaseContract;

public interface MonthlyHeadCountContract extends BaseContract {
    /**
     * Assigns all (not currently set) months below the current one to have this value as a hint
     * i.e. if we set Apr to 40, then all of May-Dec have 40 as their hint
     * i.e. if we set Nov to 20, then Dec has 20 as it's hint. Then if we set Sept to 30, then
     *      only Oct has 30 as its hint, and Dec stays at 20.
     * @param currentMonth a number describing the month, 0-11
     * @param headCount the newly set value
     */
    void setFollowingHeadCountHints(int currentMonth, int headCount);

    /**
     * Updates the required bed count associated with the month
     * @param month a number describing the month, 0-11
     * @param bedCount the newly set value
     */
    void setBedCount(int month, int bedCount);

    interface Presenter extends BaseContract.Presenter {
        /**
         * Notifies the presenter that the given month has had its population count for the month
         * changed to headCount
         * @param month a number describing the month, 0-11
         * @param headCount the newly set value
         */
        void changedHeadCount(int month, int headCount);

    }

}
