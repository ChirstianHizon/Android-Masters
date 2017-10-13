package com.example.chris.androidmasters.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

        private final TextView amount;
        private final TextView datestamp;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);

            amount  = (TextView)view.findViewById(R.id.tv_name);
            datestamp = (TextView)view.findViewById(R.id.tv_date);
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
                .inflate(R.layout.card_transactions, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contacts contacts = contactsList.get(position);

//        String currency = getCurrencySymbol("PHP");
//        holder.amount.setText("A user has donated  "+currency+" "+ transactions.getAmount());
//        holder.datestamp.setText(transactions.getDate());
//
//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, contacts.getId(), Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void clear() {
        contactsList.clear();

    }


}
