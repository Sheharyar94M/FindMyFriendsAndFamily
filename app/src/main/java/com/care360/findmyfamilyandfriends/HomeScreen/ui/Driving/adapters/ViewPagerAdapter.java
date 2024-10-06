package com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.dayData.DayDataFragment;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.monthData.MonthDataFragment;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.weekData.WeekDataFragment;
import com.care360.findmyfamilyandfriends.HomeScreen.ui.Driving.movement_record.yearData.YearDataFragment;
import com.care360.findmyfamilyandfriends.R;

public class ViewPagerAdapter extends FragmentStateAdapter implements DayDataFragment.ClickedOnMap, MonthDataFragment.ClickedOnMapMonth, WeekDataFragment.ClickedOnMapWeek, YearDataFragment.ClickedOnMapYear, DayDataFragment.DayLocations, MonthDataFragment.MonthLocations, WeekDataFragment.WeekLocations, YearDataFragment.YearLocations {

    private final DayMapClicked mapClicked;
    private final MonthMapClicked monthMapClicked;
    private final YearMapClicked yearMapClicked;
    private final WeekMapClicked weekMapClicked;
    //locations
    private final DayLocations dayLocations;
    private final MonthLocations monthLocations;
    private final WeekLocations weekLocations;
    private final YearLocations yearLocations;
    private Fragment fragment = null;

    public ViewPagerAdapter(@NonNull Fragment fragment,DayMapClicked mapClicked,MonthMapClicked monthMapClicked,WeekMapClicked weekMapClicked,YearMapClicked yearMapClicked,DayLocations dayLocations,MonthLocations monthLocations,WeekLocations weekLocations,YearLocations yearLocations) {
        super(fragment);
        this.mapClicked = mapClicked;
        this.monthMapClicked = monthMapClicked;
        this.weekMapClicked = weekMapClicked;
        this.yearMapClicked = yearMapClicked;
        this.dayLocations = dayLocations;
        this.monthLocations = monthLocations;
        this.weekLocations = weekLocations;
        this.yearLocations = yearLocations;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            fragment = new DayDataFragment(this,this);

        } else if (position == 1) {
            fragment = new WeekDataFragment(this,this);

        } else if (position == 2) {
            fragment = new MonthDataFragment(this,this);

        } else if (position == 3) {
            fragment = new YearDataFragment(this,this);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    //day button map
    @Override
    public void mapClickButton() {
        mapClicked.DayMapButtonClicked();
    }

    //month button map
    @Override
    public void mapClickButtonMonth() {
        monthMapClicked.MonthMapButtonClicked();
    }

    @Override
    public void mapClickButtonWeek() {
        weekMapClicked.WeekMapButtonClicked();
    }

    @Override
    public void mapClickButtonYear() {
        yearMapClicked.YearMapButtonClicked();
    }

    @Override
    public void dayLocations() {
        dayLocations.dayLocations();
    }

    @Override
    public void monthLocations() {
        monthLocations.monthLocations();
    }

    @Override
    public void weekLocations() {
        weekLocations.weekLocations();
    }

    @Override
    public void yearLocations() {
        yearLocations.yearLocations();
    }

    public interface YearLocations{
        void yearLocations();
    }

    public interface WeekLocations{
        void weekLocations();
    }

    public interface MonthLocations{
        void monthLocations();
    }

    public interface DayLocations{
        void dayLocations();
    }
    //interfaces for data transfer and click listening
    public interface DayMapClicked{
        void DayMapButtonClicked();
    }

    public interface MonthMapClicked{
        void MonthMapButtonClicked();
    }

    public interface WeekMapClicked{
        void WeekMapButtonClicked();
    }

    public interface YearMapClicked{
        void YearMapButtonClicked();
    }
}
