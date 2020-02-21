package com.example.studenttimetable;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.EventLog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.util.EventLog.*;

public class EditTimetable extends LinearLayout {

    ImageButton nextbutton, previousbutton;
    TextView currentDate;
    GridView gridView;
    public static final int MAX_CALENDAR_DAYS = 42;
    Calendar timetable = Calendar.getInstance(Locale.ENGLISH);
    OpenDatabase openDatabase;
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", Locale.ENGLISH);
    SimpleDateFormat eventDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Grid grid;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<TimetableEvents> eventsList = new ArrayList<>();





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

                final String date = eventDate.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvents(NameofEvent.getText().toString(), TimeofEvent.getText().toString(), date, month, year);
                        TimetableSetUp();
                        alertDialog.dismiss();
                    }

                });

                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }

        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String date = eventDate.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.events, null);

                RecyclerView recyclerView = showView.findViewById(R.id.Events);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                StudentEvents studentEvents = new StudentEvents(showView.getContext()
                ,DailyEvents(date));
                recyclerView.setAdapter(studentEvents);
                studentEvents.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        TimetableSetUp();
                    }
                });



                return true;
            }
        });

    }

    private ArrayList<TimetableEvents> DailyEvents(String date){
        ArrayList<TimetableEvents> arrayList = new ArrayList<>();
        openDatabase = new OpenDatabase(context);
        SQLiteDatabase database = openDatabase.getReadableDatabase();
        Cursor cursor = openDatabase.ReadEvents(date, database);
        while(cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(Database.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(Database.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(Database.DATE));
            String month = cursor.getString(cursor.getColumnIndex(Database.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(Database.YEAR));
            TimetableEvents events = new TimetableEvents(event, time, Date, month, Year);
            arrayList.add(events);
        }
        cursor.close();
        openDatabase.close();
        return arrayList;
    }


    public EditTimetable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void SaveEvents (String event, String time, String date, String month, String year){
        openDatabase = new OpenDatabase(context);
        SQLiteDatabase database = openDatabase.getWritableDatabase();
        openDatabase.SaveEvent(event,time,date,month,year,database);
        openDatabase.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    public void Initialise(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_of_timetable, this);
        nextbutton = view.findViewById(R.id.next);
        previousbutton = view.findViewById(R.id.previous);
        currentDate = view.findViewById(R.id.currentdate);
        gridView = view.findViewById(R.id.timetablegrid);

    }

    public void TimetableSetUp(){
        String TodaysDate = dateFormat.format(timetable.getTime());
        currentDate.setText(TodaysDate);
        dates.clear();
        Calendar monthTimetable = (Calendar)timetable.clone();
        monthTimetable.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDay = monthTimetable.get(Calendar.DAY_OF_WEEK)-1;
        monthTimetable.add(Calendar.DAY_OF_MONTH, -FirstDay);
        MonthlyEvents(monthFormat.format(timetable.getTime()),yearFormat.format(timetable.getTime()));


        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthTimetable.getTime());
            monthTimetable.add(Calendar.DAY_OF_MONTH, 1);
        }

        grid = new Grid(context, dates, timetable,eventsList);
        gridView.setAdapter(grid);
    }

    public void MonthlyEvents(String Month, String year){
        eventsList.clear();
        openDatabase = new OpenDatabase(context);
        SQLiteDatabase database = openDatabase.getReadableDatabase();
        Cursor cursor = openDatabase.ReadEventsperMonth(Month, year, database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(Database.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(Database.TIME));
            String date = cursor.getString(cursor.getColumnIndex(Database.DATE));
            String month = cursor.getString(cursor.getColumnIndex(Database.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(Database.YEAR));
            TimetableEvents events = new TimetableEvents(event,time,date,month,Year);
            eventsList.add(events);

        }

        cursor.close();
        openDatabase.close();
    }
}