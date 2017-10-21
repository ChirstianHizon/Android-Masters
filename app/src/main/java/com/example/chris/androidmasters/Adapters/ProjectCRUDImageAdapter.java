package com.example.chris.androidmasters.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chris.androidmasters.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 08/10/2017.
 */

public class ProjectCRUDImageAdapter extends RecyclerView.Adapter<ProjectCRUDImageAdapter.ViewHolder> {

    private final List<String> imageList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView card;
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);

            card = (CardView)view.findViewById(R.id.cv_card);
            image = (ImageView)view.findViewById(R.id.iv_image);

        }
    }

    public ProjectCRUDImageAdapter(Context context, ArrayList<String> contactsList){
        this.context = context;
        this.imageList = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Picasso.with(context)
                .load(Uri.parse(imageList.get(position)))
                .resize(30,30)
                .placeholder(R.color.white)
                .error(R.color.colorAccent)
                .centerCrop()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void clear() {imageList.clear();}


}
