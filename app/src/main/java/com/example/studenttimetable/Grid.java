package com.example.studenttimetable;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Grid extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<TimetableEvents> events;
    LayoutInflater inflater;


    public Grid(@NonNull Context context, List<Date> dates, Calendar currentDate, List<TimetableEvents> events) {
        super(context, R.layout.grid_cell);

        this.dates=dates;
        this.currentDate=currentDate;
        this.events=events;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateTimetable = Calendar.getInstance();
        dateTimetable.setTime(monthDate);
        int NumberofDay = dateTimetable.get(Calendar.DAY_OF_MONTH);
        int ShowMonth = dateTimetable.get(Calendar.MONTH) + 1;
        int ShowYear = dateTimetable.get(Calendar.YEAR);
        int CurrentMonth = currentDate.get(Calendar.MONTH) + 1;
        int CurrentYear = currentDate.get(Calendar.YEAR);


        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.grid_cell, parent, false);

        }

        if (ShowMonth == CurrentMonth && ShowYear == CurrentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        TextView NumberedDay = view.findViewById(R.id.timetableday);
        TextView NumberedEvent = view.findViewById(R.id.timetablevent);
        NumberedDay.setText(String.valueOf(NumberofDay));

        Calendar eventTimetable = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i = 0; i < events.size();i++){
            eventTimetable.setTime(StringtoDate(events.get(i).getDATE()));
            if(NumberofDay == eventTimetable.get(Calendar.DAY_OF_MONTH) && CurrentMonth == eventTimetable.get(Calendar.MONTH)+1
                    && CurrentYear == eventTimetable.get(Calendar.YEAR)){
                arrayList.add(events.get(i).getEVENT());
                NumberedEvent.setText(arrayList.size()+" Events");

            }

        }


                return view;
    }

    public Date StringtoDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
