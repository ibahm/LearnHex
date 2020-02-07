package com.example.studenttimetable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditTimetable extends LinearLayout {

    ImageButton nextbutton, previousbutton;
    TextView currentDate;
    GridView gridview;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar timetable = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", Locale.ENGLISH);

    List<Date> dates = new ArrayList<>();
    List<TimetableEvents> eventsList = new ArrayList<>();

    public EditTimetable(Context context) {
        super(context);
    }

    public EditTimetable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Initialise();

        previousbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timetable.add(Calendar.MONTH, -1);
                TimetableSetUp();
            }
        });

        nextbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timetable.add(Calendar.MONTH, 1);
                TimetableSetUp();
            }
        });
    }

    public EditTimetable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void Initialise(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_of_timetable, this);
        nextbutton = view.findViewById(R.id.next);
        previousbutton = view.findViewById(R.id.previous);
        currentDate = view.findViewById(R.id.currentdate);
        gridview = view.findViewById(R.id.timetablegrid);

    }

    private void TimetableSetUp(){
        String TodaysDate = dateFormat.format(timetable.getTime());
        currentDate.setText(TodaysDate);
    }
}
