package com.vivekkaushik.datepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.vivekkaushik.datepicker.helper.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import org.joda.time.DateTime;

public class DatePickerTimeline extends LinearLayout {
    private TimelineView timelineView;

    public DatePickerTimeline(Context context) {
        super(context);
    }

    public DatePickerTimeline(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DatePickerTimeline(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePickerTimeline(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    void init(AttributeSet attrs, int defStyleAttr) {
        final View view = inflate(getContext(), R.layout.date_picker_timeline, this);
        timelineView = view.findViewById(R.id.timelineView);

        // load Default values
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DatePickerTimeline, defStyleAttr, 0);
        timelineView.setDayTextColor(a.getColor(R.styleable.DatePickerTimeline_dayTextColor, getResources().getColor(R.color.black)));
        timelineView.setDateTextColor(a.getColor(R.styleable.DatePickerTimeline_dateTextColor, getResources().getColor(R.color.black)));
        timelineView.setMonthTextColor(a.getColor(R.styleable.DatePickerTimeline_monthTextColor, getResources().getColor(R.color.black)));
        timelineView.setDisabledDateColor(a.getColor(R.styleable.DatePickerTimeline_disabledColor, getResources().getColor(R.color.grey)));

//        timelineView.setMonthTextSize(a.getDimension(R.styleable.DatePickerTimeline_monthTextSize, getResources().getDimension(R.dimen.monthTextSize)));
//        timelineView.setDateTextSize(a.getDimension(R.styleable.DatePickerTimeline_dateTextSize, getResources().getDimension(R.dimen.dateTextSize)));
//        timelineView.setDayTextSize(a.getDimension(R.styleable.DatePickerTimeline_dayTextSize, getResources().getDimension(R.dimen.dayTextSize)));

        a.recycle();
        timelineView.invalidate();
    }

    /**
     * Sets the color for date text
     * @param color the color of the date text
     */
    public void setDateTextColor(int color) {
        timelineView.setDateTextColor(color);
    }

    /**
     * Sets the color for day text
     * @param color the color of the day text
     */
    public void setDayTextColor(int color) {
        timelineView.setDayTextColor(color);
    }

    /**
     * Sets the color for month
     * @param color the color of the month text
     */
    public void setMonthTextColor(int color) {
        timelineView.setMonthTextColor(color);
    }

    /**
     * Sets the color for disabled dates
     * @param color the color of the date
     */
    public void setDisabledDateColor(int color) {
        timelineView.setDisabledDateColor(color);
    }

    /**
     * Register a callback to be invoked when a date is selected.
     * @param listener the callback that will run
     */
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        timelineView.setOnDateSelectedListener(listener);
    }

    /**
     * Set a Start date for the calendar (Default, 1 Jan 1970)
     * @param date start date
     */
    public void setInitialDate(String date, int maxRange) {
        Date currentDate = parseDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        timelineView.setInitialDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), maxRange);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Set selected background to active date
     * @param date Active Date
     */
    public void setActiveDate(Calendar date) {
        timelineView.setActiveDate(date);
    }


    public void setDateRangeDeactive(Date curDate, int range){
        DateTime dtOrg = new DateTime(curDate);
        DateTime dtRange = dtOrg.plusDays(range);

        deactivateDates(Objects.requireNonNull(Utils.Companion.getDateRange(dtOrg.toString(), dtRange.toString())));
    }
    /**
     * Deactivate dates from the calendar. User won't be able to select
     * the deactivated date.
     * @param dates Array of Dates
     */
    public void deactivateDates(ArrayList<Date> dates) {
        ArrayList<Date> newDates = new ArrayList<Date>();
        for(Date date: dates){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) == 1){
                newDates.add(plusDate(date, 1).toDate());
            }else if(cal.get(Calendar.DAY_OF_WEEK) ==7){
                newDates.add(plusDate(date, 2).toDate());
            }else{
                newDates.add(date);
            }
        }
        timelineView.deactivateDates(newDates);
    }

    public DateTime plusDate(Date date, int plus){
        DateTime dtOrg = new DateTime(date);
        return dtOrg.plusDays(plus);
    }
}
