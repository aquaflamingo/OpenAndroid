/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robertsimoes.conscious.R;
import com.robertsimoes.conscious.data.models.Thought;

import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.ThoughtViewHolder> {

    private List<Thought> dataset;
    private ViewGroup viewGroupParent;

    public MainRVAdapter(List<Thought> dataset) {
        this.dataset = dataset;
    }

    @Override
    public ThoughtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.viewGroupParent = parent;
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_thought, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ThoughtViewHolder vh = new ThoughtViewHolder(v);
        return vh;
    }

    /**
     * Replace contents of view here
     * @param holder the view holder to modify
     * @param position which item
     */
    @Override
    public void onBindViewHolder(ThoughtViewHolder holder, int position) {
        Thought t;
        try {
            t=dataset.get(position);
            holder.title.setText(t.getTitle());
            holder.body.setText(t.getBody());
            // Substring because don't want second
            holder.timeStamp.setText(t.getTimeStamp().substring(0,16));
            holder.hash.setText(t.getHash().substring(0,9));
        } catch (Exception e) {
            Context c  = this.getContext();
            holder.title.setText(c.getResources().getString(R.string.default_cardview_title));
            holder.body.setText(c.getResources().getString(R.string.default_cardview_body));
            holder.timeStamp.setText(c.getResources().getString(R.string.default_cardview_time_stamp));
            holder.hash.setText(c.getResources().getString(R.string.default_cardview_hash));
        }

    }

    public Context getContext() {
        return viewGroupParent.getContext();
    }

    public static class ThoughtViewHolder extends RecyclerView.ViewHolder {
        public static final String EXTRA_THOUGHT_DETAILS = "com.pressurelabs.conscious.EXTRA_THOUGHT_DETAILS";
        public TextView timeStamp;
        public TextView title;
        public TextView body;
        public TextView hash;
        private CardView container;
        /**
         * Passed view is cardview, find textviews by lookup based on item view
         * @param itemView, cardview associated with text views
         */
        public ThoughtViewHolder(final View itemView) {
            super(itemView);

            timeStamp = (TextView) itemView.findViewById(R.id.tv_card_thought_timestamp);
            title = (TextView) itemView.findViewById(R.id.tv_card_thought_title);
            body = (TextView) itemView.findViewById(R.id.tv_card_thought_body);
            hash = (TextView) itemView.findViewById(R.id.tv_card_thought_hash);

            container = (CardView) itemView.findViewById(R.id.card_thought);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent showIntent = new Intent(itemView.getContext(),ShowThoughtActivity.class);
                    showIntent.putExtra(EXTRA_THOUGHT_DETAILS,
                            new String[]{timeStamp.getText().toString(),
                                    title.getText().toString(),
                                    body.getText().toString()});

                    itemView.getContext().startActivity(showIntent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
