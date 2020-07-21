package com.vivekkaushik.datepicker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vivekkaushik.datepicker.OnDateSelectedListener;
import com.vivekkaushik.datepicker.R;
import com.vivekkaushik.datepicker.TimelineView;
import com.vivekkaushik.datepicker.helper.Utils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private static final String TAG = "TimelineAdapter";
    private static final String[] WEEK_DAYS = DateFormatSymbols.getInstance().getShortWeekdays();
    private static final String[] MONTH_NAME = DateFormatSymbols.getInstance().getShortMonths();

    private Calendar calendar = Calendar.getInstance();
    private TimelineView timelineView;
    private ArrayList<Date> deactivatedDates;
    private OnDateSelectedListener listener;
    private View selectedView;
    private int adapterSize;
    private int selectedPosition;
    private int oldPosition;

    public TimelineAdapter(TimelineView timelineView, int selectedPosition, int adapterSize) {
        this.timelineView = timelineView;
        this.selectedPosition = selectedPosition;
        this.adapterSize = adapterSize;
    }

    public void setAdapterSize(int size){
        this.adapterSize = size;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item_layout, parent, false);
        return new TimelineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        resetCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, position);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final boolean isDisabled = holder.bind(month, day, dayOfWeek, year, position);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDisabled) {
                    holder.rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_shape));
                    holder.monthView.setTextColor(Color.WHITE);
                    holder.dateView.setTextColor(Color.WHITE);
                    holder.dayView.setTextColor(Color.WHITE);
                    for (int i = 0; i < adapterSize; i++){
                        if (i != position){
                            holder.monthView.setTextColor(Color.BLACK);
                            holder.dateView.setTextColor(Color.BLACK);
                            holder.dayView.setTextColor(Color.BLACK);
                            holder.rootView.setBackground(null);
                            notifyItemChanged(i);
                        } else if(position == oldPosition){
                            holder.rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_rounded));
                            holder.monthView.setTextColor(Color.BLACK);
                            holder.dateView.setTextColor(Color.BLACK);
                            holder.dayView.setTextColor(Color.BLACK);
                        }else{
                            holder.rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_rounded));
                            holder.monthView.setTextColor(Color.BLACK);
                            holder.dateView.setTextColor(Color.BLACK);
                            holder.dayView.setTextColor(Color.BLACK);
                        }
                    }
                    notifyItemChanged(position);
                    selectedPosition = position;
                    selectedView = v;
                    if (listener != null) listener.onDateSelected(year, month, day, dayOfWeek);
                } else {
                    for (int i = 1; i < adapterSize; i++){
                        if (i != position) {
                            if (dayOfWeek == 7 || dayOfWeek == 1) {
                                holder.monthView.setTextColor(Color.RED);
                                holder.dateView.setTextColor(Color.RED);
                                holder.dayView.setTextColor(Color.RED);
                            }else {
                                holder.monthView.setTextColor(timelineView.getDisabledDateColor());
                                holder.dateView.setTextColor(timelineView.getDisabledDateColor());
                                holder.dayView.setTextColor(timelineView.getDisabledDateColor());
                            }
                            holder.rootView.setBackground(null);
                            notifyItemChanged(i);
                        }
                    }
                    if (listener != null) listener.onDisabledDateSelected(year, month, day, dayOfWeek, isDisabled);
                }
            }
        });
    }

    private void resetCalendar() {
        calendar.set(timelineView.getYear(), timelineView.getMonth(), timelineView.getDate(),
                1, 0, 0);
    }

    /**
     * Set the position of selected date
     * @param selectedPosition active date Position
     */
    public void setSelectedPosition(int selectedPosition) {
        this.oldPosition = selectedPosition;
    }

    @Override
    public int getItemCount() {
        return adapterSize;
    }

    public void disableDates(ArrayList<Date> dates) {
        this.deactivatedDates = dates;
        notifyDataSetChanged();
    }

    public void setDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView monthView, dateView, dayView;
        private View rootView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            monthView = itemView.findViewById(R.id.monthView);
            dateView = itemView.findViewById(R.id.dateView);
            dayView = itemView.findViewById(R.id.dayView);
            rootView = itemView.findViewById(R.id.rootView);
        }

        boolean bind(int month, int day, int dayOfWeek, int year, int position) {
            monthView.setTextColor(timelineView.getMonthTextColor());
            dateView.setTextColor(timelineView.getDateTextColor());
            dayView.setTextColor(timelineView.getDayTextColor());

            dayView.setText(Utils.Companion.getMapperDate(WEEK_DAYS[dayOfWeek].toUpperCase(Locale.US)));
            monthView.setText(MONTH_NAME[month].toUpperCase(Locale.US));
            dateView.setText(String.valueOf(day));

            if (deactivatedDates != null && dayOfWeek != 7 || deactivatedDates != null && dayOfWeek != 1){
                for (Date date : deactivatedDates) {
                    Calendar tempCalendar = Calendar.getInstance();
                    tempCalendar.setTime(date);
                    if (tempCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                            tempCalendar.get(Calendar.MONTH) == month &&
                            tempCalendar.get(Calendar.YEAR) == year) {
                        monthView.setTextColor(timelineView.getDisabledDateColor());
                        dateView.setTextColor(timelineView.getDisabledDateColor());
                        dayView.setTextColor(timelineView.getDisabledDateColor());

                        rootView.setBackground(null);
                        return true;
                    }
                }
            }

            if (dayOfWeek == 7 || dayOfWeek == 1){
                monthView.setTextColor(Color.RED);
                dateView.setTextColor(Color.RED);
                dayView.setTextColor(Color.RED);

                rootView.setBackground(null);
                return true;
            }

            if (oldPosition == position && selectedPosition == -1 || oldPosition == position && selectedPosition == oldPosition){
                rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_shape));
                monthView.setTextColor(Color.WHITE);
                dateView.setTextColor(Color.WHITE);
                dayView.setTextColor(Color.WHITE);
            }else if(oldPosition == position) {
                rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_rounded));
                monthView.setTextColor(Color.BLACK);
                dateView.setTextColor(Color.BLACK);
                dayView.setTextColor(Color.BLACK);
            }else if(selectedPosition == position) {
                rootView.setBackground(timelineView.getResources().getDrawable(R.drawable.bg_selected_shape));
                monthView.setTextColor(Color.WHITE);
                dateView.setTextColor(Color.WHITE);
                dayView.setTextColor(Color.WHITE);
            } else {
                rootView.setBackground(null);
                monthView.setTextColor(Color.BLACK);
                dateView.setTextColor(Color.BLACK);
                dayView.setTextColor(Color.BLACK);
            }
            return false;
        }
    }


}
