package com.example.chris.androidmasters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.androidmasters.Functions.ElapsedTime;
import com.example.chris.androidmasters.Objects.Project;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import static com.example.chris.androidmasters.Objects.Constants.getCurrencySymbol;

/**
 * Created by chris on 08/10/2017.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private Context context;
    private List<Project> projectlist;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView description;
        private final ImageView display;
        private final TextView organization;
        private final ImageView image;
        private final TextView date;
        private final TextView date_measure;
        private final ImageView logo;
        private final CardView card;
        private final TextView id;
        private final TextView goal;
        private final TextView current;
        private final TextView current_label;
        private final ProgressBar status;
        private final TextView donated;

        public ViewHolder(View view) {
            super(view);

            id = (TextView)view.findViewById(R.id.tv_id);
            name = (TextView)view.findViewById(R.id.tv_name);
            description = (TextView)view.findViewById(R.id.tv_desc);
            display = (ImageView)view.findViewById(R.id.iv_image);
            organization = (TextView)view.findViewById(R.id.tv_organization);
            logo = (ImageView)view.findViewById(R.id.iv_logo);
            image = (ImageView)view.findViewById(R.id.iv_image);
            date = (TextView)view.findViewById(R.id.tv_date);
            date_measure = (TextView)view.findViewById(R.id.tv_date_measure);
            card = (CardView)view.findViewById(R.id.cv_card);
            goal = (TextView) view.findViewById(R.id.tv_goal);
            donated = (TextView) view.findViewById(R.id.tv_donated_value);
            current = (TextView)view.findViewById(R.id.tv_current);
            current_label = (TextView)view.findViewById(R.id.tv_current_label);
            status = (ProgressBar)view.findViewById(R.id.pb_status);
        }
    }

    public ProjectListAdapter( Context context, List<Project> projectlist){
        this.context = context;
        this.projectlist = projectlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_projectlist, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Project project = projectlist.get(position);

//      -------------- SET THE TEXT GATHERED FROM THE PROJECT OBJECT ---------

        holder.id.setText(project.getId());
        holder.name.setText(project.getName());
        holder.description.setText(project.getDescription());


        holder.date.setText(project.getDate());
        holder.organization.setText(project.getOrganization());
        Double goal = Double.valueOf(project.getGoal());
        Double current = Double.valueOf(project.getCurrent());

        Double progress = Double.valueOf(project.getCurrent()) / Double.valueOf(project.getGoal()) * 100;
        String currency = "₱";

        holder.current.setText(progress.intValue() + "%");
        holder.donated.setText(currency+ NumberFormat.getIntegerInstance().format(Integer.valueOf(project.getCurrent())));
        holder.goal.setText(currency+ NumberFormat.getIntegerInstance().format(Integer.valueOf(project.getGoal())));



        Log.d("PROGRESS", String.valueOf(progress));

        if(current > goal){
            holder.status.getProgressDrawable().setColorFilter(Color.parseColor("#FF00C853"), PorterDuff.Mode.SRC_IN);
            holder.current.setTextColor(Color.parseColor("#FF00C853"));
            holder.current_label.setTextColor(Color.parseColor("#FF00C853"));
        }else if(current.equals(goal)){
            holder.status.getProgressDrawable().setColorFilter(Color.parseColor("#FF00C853"), PorterDuff.Mode.SRC_IN);
            holder.current.setTextColor(Color.parseColor("#FF00C853"));
            holder.current_label.setTextColor(Color.parseColor("#FF00C853"));
        }else if(current.equals(0)){
            holder.status.getProgressDrawable().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN);
            holder.current.setTextColor(Color.parseColor("#F44336"));
            holder.current_label.setTextColor(Color.parseColor("#F44336"));
        }else{
            holder.status.getProgressDrawable().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
            holder.current.setTextColor(Color.parseColor("#FF9800"));
            holder.current_label.setTextColor(Color.parseColor("#FF9800"));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.status.setProgress(progress.intValue(),true);
        }else{
            holder.status.setProgress(progress.intValue());
        }

        Log.d("IMAGE",project.getImage());

        Picasso.with(context)
                .load(project.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.image);

        Picasso.with(context)
                .load(project.getLogo())
                .resize(30,30)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.logo);

//        ------------- ON CLICK LISTENER ----------------------

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = holder.id.getText().toString();
                String organization = holder.organization.getText().toString();
//                Toast.makeText(context, id, Toast.LENGTH_SHORT).show();

                Intent intent =  new Intent(context, ProjectView.class);
//                Intent intent = new Intent(context, SampleCheckout.class);
                intent.putExtra("id",id);
                intent.putExtra("organization",organization);
                context.startActivity(intent);
            }
        });


        //Get Date Elapsed
        Date now = new Date();
        Date completion = project.getCompletion_date();

        ElapsedTime elapsed = new ElapsedTime(now,completion);

        if(elapsed.getDay() > 0 ){
            holder.date.setText(elapsed.getDay() + "");
            holder.date_measure.setText("days to go");
        }else if(elapsed.getHour() > 0 ){
            holder.date.setText(elapsed.getHour() + "");
            holder.date_measure.setText("hours remaining");
        }else if(elapsed.getMinute() > 0 ){
            holder.date.setText(elapsed.getMinute() + "");
            holder.date_measure.setText("minutes left");
        }else{
            holder.date.setText("0");
            holder.date_measure.setText("has ended");
        }


    }

    @Override
    public int getItemCount() {
        return projectlist.size();
    }

    public void clear() {
        projectlist.clear();

    }

}
