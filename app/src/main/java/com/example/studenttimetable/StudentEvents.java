package com.example.studenttimetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentEvents extends RecyclerView.Adapter<StudentEvents.MyViewHolder> {

    Context context;
    ArrayList<TimetableEvents> arrayList;
    OpenDatabase openDatabase;

    public StudentEvents(Context context, ArrayList<TimetableEvents> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_of_events, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final TimetableEvents events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.DateTxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.deleteevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteStudentEvents(events.getEVENT(), events.getDATE(), events.getTIME());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DateTxt, Time, Event;
        Button deleteevent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventtime);
            deleteevent = itemView.findViewById(R.id.deleteevent);

        }
    }

    private void DeleteStudentEvents(String event, String date, String time){
        openDatabase = new OpenDatabase(context);
        SQLiteDatabase database = openDatabase.getWritableDatabase();
        openDatabase.DeleteEvents(event, date, time,database);
        openDatabase.close();

    }
}
