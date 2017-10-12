package com.example.chris.androidmasters.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.androidmasters.Objects.Transactions;
import com.example.chris.androidmasters.R;

import java.util.List;

/**
 * Created by chris on 08/10/2017.
 */

public class ProjectTransactionAdapter extends RecyclerView.Adapter<ProjectTransactionAdapter.ViewHolder> {

    private final List<Transactions> transactionsList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView amount;
        private final TextView datestamp;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);

            amount  = (TextView)view.findViewById(R.id.tv_amount);
            datestamp = (TextView)view.findViewById(R.id.tv_date);
            card = (CardView)view.findViewById(R.id.cv_card);

        }
    }

    public ProjectTransactionAdapter(Context context, List<Transactions> transactionsList){
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_projectlist, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Transactions transactions = transactionsList.get(position);

        holder.amount.setText("P " + transactions.getAmount());
        holder.datestamp.setText(transactions.getDate());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, transactions.getId(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void clear() {
        transactionsList.clear();

    }


}
