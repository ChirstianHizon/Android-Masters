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

import com.example.chris.androidmasters.Objects.Project;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        private final ImageView logo;
        private final CardView card;
        private final TextView id;
        private final TextView goal;
        private final TextView current;
        private final ProgressBar status;

        public ViewHolder(View view) {
            super(view);

            id = (TextView)view.findViewById(R.id.tv_id);
            name = (TextView)view.findViewById(R.id.tv_name);
            description = (TextView)view.findViewById(R.id.tv_desc);
            display = (ImageView)view.findViewById(R.id.iv_image);
            organization = (TextView)view.findViewById(R.id.tv_amount);
            logo = (ImageView)view.findViewById(R.id.iv_logo);
            image = (ImageView)view.findViewById(R.id.iv_image);
            date = (TextView)view.findViewById(R.id.tv_date);
            card = (CardView)view.findViewById(R.id.cv_card);
            goal = (TextView) view.findViewById(R.id.tv_goal);
            current = (TextView)view.findViewById(R.id.tv_current);
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
        holder.current.setText("P "+project.getCurrent());
        holder.goal.setText("P "+project.getGoal());

        Double goal = Double.valueOf(project.getGoal());
        Double current = Double.valueOf(project.getCurrent());

        Double progress = Double.valueOf(project.getCurrent()) / Double.valueOf(project.getGoal()) * 100;

        Log.d("PROGRESS", String.valueOf(progress));

        if(current > goal){
            holder.status.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        }else if(current.equals(goal)){
            holder.status.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        }else{
            holder.status.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.status.setProgress(progress.intValue(),true);
        }else{
            holder.status.setProgress(progress.intValue());
        }

        Log.d("IMAGE",project.getImage());

        Picasso.with(context)
                .load(project.getImage())
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.image);

        Picasso.with(context)
                .load(project.getLogo())
                .resize(30,30)
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


    }

    @Override
    public int getItemCount() {
        return projectlist.size();
    }

    public void clear() {
        projectlist.clear();

    }


}
