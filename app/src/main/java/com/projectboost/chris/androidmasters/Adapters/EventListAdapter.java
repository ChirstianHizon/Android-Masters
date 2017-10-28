package com.projectboost.chris.androidmasters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectboost.chris.androidmasters.Events.EventViewActivity;
import com.projectboost.chris.androidmasters.Functions.ElapsedTime;
import com.projectboost.chris.androidmasters.Objects.Events;
import com.projectboost.chris.androidmasters.R;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by chris on 08/10/2017.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private Context context;
    private List<Events> eventlist;
    private ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#9ACCCD")), new ColorDrawable(Color.parseColor("#8FD8A0")),
                    new ColorDrawable(Color.parseColor("#CBD890")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D9A790")), new ColorDrawable(Color.parseColor("#D18FD9")),
                    new ColorDrawable(Color.parseColor("#FF6772")), new ColorDrawable(Color.parseColor("#DDFB5C"))
            };

    public EventListAdapter(Context context, List<Events> eventlist) {
        this.context = context;
        this.eventlist = eventlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Events events = eventlist.get(position);

        Date now = new Date();
        Date completion = events.getDate_event();

        ElapsedTime elapsed = new ElapsedTime(now, completion);

        if (elapsed.getDay() > 0) {
            holder.remain.setText(elapsed.getDay() + "");
            holder.measure.setText("days to go");
        } else if (elapsed.getHour() > 0) {
            holder.remain.setText(elapsed.getHour() + "");
            holder.measure.setText("hours remaining");
        } else if (elapsed.getMinute() > 0) {
            holder.remain.setText(elapsed.getMinute() + "");
            holder.measure.setText("minutes left");
        } else {
            holder.remain.setText("");
            holder.measure.setText("Finished");
        }

        holder.event.setText(events.getName());
        String day = (String) DateFormat.format("dd", completion);
        String monthString = (String) DateFormat.format("MMM", completion);
        String year = (String) DateFormat.format("yyyy", completion);
        String hour = (String) DateFormat.format("hh", completion);
        String min = (String) DateFormat.format("mm", completion);

        holder.date.setText(monthString + " " + day + ", " + year);
        holder.time.setText(hour + " : " + min);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = events.getId();
                Intent intent = new Intent(context, EventViewActivity.class);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventlist.size();
    }

    public void clear() {
        eventlist.clear();
    }

    public ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView event;
        private final TextView date;
        private final TextView time;
        private final TextView remain;
        private final CardView card;
        private final TextView measure;

        public ViewHolder(View view) {
            super(view);

            event = (TextView) view.findViewById(R.id.tv_event);
            date = (TextView) view.findViewById(R.id.tv_date);
            time = (TextView) view.findViewById(R.id.tv_time);
            remain = (TextView) view.findViewById(R.id.tv_remain);
            measure = (TextView) view.findViewById(R.id.tv_date_measure);
            card = (CardView) view.findViewById(R.id.cv_card);

        }
    }

}
