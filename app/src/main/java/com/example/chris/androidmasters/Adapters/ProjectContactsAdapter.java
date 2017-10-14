package com.example.chris.androidmasters.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.R;

import java.util.List;

/**
 * Created by chris on 08/10/2017.
 */

public class ProjectContactsAdapter extends RecyclerView.Adapter<ProjectContactsAdapter.ViewHolder> {

    private final List<Contacts> contactsList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView card;
        private final TextView contact;
        private final TextView position;
        private final TextView name;

        public ViewHolder(View view) {
            super(view);

            name  = (TextView)view.findViewById(R.id.tv_name);
            position = (TextView)view.findViewById(R.id.tv_position);
            contact = (TextView)view.findViewById(R.id.tv_contact);
            card = (CardView)view.findViewById(R.id.cv_card);

        }
    }

    public ProjectContactsAdapter(Context context, List<Contacts> contactsList){
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_contacts, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contacts contacts = contactsList.get(position);

        Log.d("Adapter","OK");
        holder.contact.setText(contacts.getContact());
        holder.name.setText(contacts.getName());
        holder.position.setText(contacts.getPosition());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void clear() {contactsList.clear();}


}
