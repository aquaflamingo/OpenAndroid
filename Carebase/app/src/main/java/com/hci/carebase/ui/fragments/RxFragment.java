package com.hci.carebase.ui.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hci.carebase.R;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.domain.Prescription;
import com.hci.carebase.ui.activities.RxImageActivity;
import com.hci.carebase.util.Const;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RxFragment extends Fragment {

    @BindView(R.id.rx_rv_no_contents)
    TextView tvNoContents;

    @BindView(R.id.fragment_rx_recycle_view)
    RecyclerView recyclerView;

    private ListAdapter listAdapter;
    public static RxFragment newInstance(Bundle args) {
        RxFragment fragment = new RxFragment();
        if (args!=null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rx, container, false);
        ButterKnife.bind(this,view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        if (getArguments()==null) {

            tvNoContents.setVisibility(View.VISIBLE);

        } else {

            Patient p= (Patient) getArguments().getSerializable(Const.BUNDLE_KEY_PATIENT);
            if (p.getScripts()==null || p.getScripts().isEmpty()) {
                tvNoContents.setVisibility(View.VISIBLE);
            } else {

                ArrayList data = p.getScripts();

                listAdapter = new ListAdapter(data);
                recyclerView.setAdapter(listAdapter);

            }
        }

        return view;
    }

    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
        private ArrayList<Prescription> data;
        public ListAdapter(ArrayList<Prescription> _data){
            this.data = _data;
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView dose;
            TextView issued;
            TextView expired;
            TextView sideEffects;
            Button rxShow;
            public ViewHolder(View itemView){
                super(itemView);
                this.name = itemView.findViewById(R.id.rx_card_drug_name);
                this.dose = itemView.findViewById(R.id.rx_card_drug_dose);
                this.issued = itemView.findViewById(R.id.rx_card_drug_issued);
                this.expired = itemView.findViewById(R.id.rx_card_expires_date_gradient);
                this.sideEffects = itemView.findViewById(R.id.rx_card_drug_side_effects);
                this.rxShow = itemView.findViewById(R.id.rx_card_drug_show);
            }
        }
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_rx, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position){
            holder.name.setText(data.get(position).getDrugName());
            holder.dose.setText(data.get(position).getDosage());
            holder.issued.setText( "Issued: " + DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(data.get(position).getIssued()));
            holder.expired.setText(DateFormat.getDateInstance().format(data.get(position).getExpires()));
            holder.sideEffects.setText(data.get(position).getSideEffects());
            holder.rxShow.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), RxImageActivity.class).putExtra("drugName", data.get(position).getDrugName());
                startActivity(intent);
            });
        }
        @Override
        public int getItemCount(){
            return data.size();
        }
    }
}
