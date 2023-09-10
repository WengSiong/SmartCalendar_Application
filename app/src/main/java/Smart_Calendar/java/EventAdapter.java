package Smart_Calendar.java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Smart_Calendar.java.Event>
{

    public EventAdapter(@NonNull Context context, List<Smart_Calendar.java.Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Smart_Calendar.java.Event event = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

//        Set TextView
        String eventTitle = Smart_Calendar.java.CalendarUtils.formattedTime(event.getTime()) + "\t\t\t\t\t" + event.getName();
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}

