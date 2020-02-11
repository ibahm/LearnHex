package com.example.studenttimetable;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EditTimetable extends LinearLayout {

    ImageButton nextbutton, previousbutton;
    TextView currentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar timetable = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", Locale.ENGLISH);
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<TimetableEvents> eventsList = new ArrayList<>();

    OpenDatabase openDatabase;
    Grid grid;

    public EditTimetable(Context context) {
        super(context);
    }

    public EditTimetable(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Initialise();
        TimetableSetUp();

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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_event, null);
                final EditText NameofEvent = addView.findViewById(R.id.eventname);
                final TextView TimeofEvent = addView.findViewById(R.id.eventtime);
                ImageButton TimeSet = addView.findViewById(R.id.settime);
                Button AddEvent = addView.findViewById(R.id.addevent);
                TimeSet.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar timetable = Calendar.getInstance();
                        int hours = timetable.get(Calendar.HOUR_OF_DAY);
                        int minutes = timetable.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        Calendar t = Calendar.getInstance();
                                        t.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        t.set(Calendar.MINUTE, minute);
                                        t.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                        String eventTime = hformate.format(t.getTime());
                                        TimeofEvent.setText(eventTime);
                                    }
                                }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                final String date = dateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvents(NameofEvent.getText().toString(), TimeofEvent.getText().toString(),date,month,year);
                        TimetableSetUp();
                        alertDialog.dismiss();


                    }
                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();


            }

        });

        }


    public EditTimetable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveEvents (String event, String time, String date, String month, String year){
        openDatabase = new OpenDatabase(context);
        SQLiteDatabase database = openDatabase.getWritableDatabase();
        openDatabase.SaveEvent(event, time, date, month, year, database);
        openDatabase.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void Initialise(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_of_timetable, this);
        nextbutton = view.findViewById(R.id.next);
        previousbutton = view.findViewById(R.id.previous);
        currentDate = view.findViewById(R.id.currentdate);
        gridView = view.findViewById(R.id.timetablegrid);

    }

    private void TimetableSetUp(){
        String TodaysDate = dateFormat.format(timetable.getTime());
        currentDate.setText(TodaysDate);
        dates.clear();
        Calendar monthTimetable = (Calendar)timetable.clone();
        monthTimetable.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDay = monthTimetable.get(Calendar.DAY_OF_WEEK)-1;
        monthTimetable.add(Calendar.DAY_OF_MONTH, -FirstDay);

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthTimetable.getTime());
            monthTimetable.add(Calendar.DAY_OF_MONTH, 1);
        }

        grid = new Grid(context, dates, timetable,eventsList);
        gridView.setAdapter(grid);
    }
}
