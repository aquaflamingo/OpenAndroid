package com.hci.carebase.ui.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hci.carebase.R;
import com.hci.carebase.domain.Appointment;
import com.hci.carebase.domain.Patient;
import com.hci.carebase.domain.Summary;
import com.hci.carebase.ui.MainActivity;
import com.hci.carebase.util.Commons;
import com.hci.carebase.util.Const;
import com.hci.carebase.util.Faker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InboxFragment extends Fragment {



    private MainActivityCallback cb;
    @BindView(R.id.summaries_rv_no_contents) TextView tvNoContents;

    @BindView(R.id.fragment_inbox_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.inbox_progress_bar_subtitle) TextView tvProgressbarSubtitle;
    @BindView(R.id.inbox_cv_upcoming_appt) CardView cvUpcomingAppts;
    // Number of appointments
    @BindView(R.id.tv_inbox_treatments_done) TextView tvTreatmentsDone;
    // Number of appointments (weekly till end)
    @BindView(R.id.tv_inbox_treatments_to_go) TextView tvTreatmentsToGo;
    @BindView(R.id.inbox_button_more_info_appt) Button buttonMoreInfo;
    @BindView(R.id.tv_inbox_appt_doctor) TextView tvDrAppts;
    @BindView(R.id.tv_inbox_appt_date) TextView tvDateAppts;

    private Patient p;
    private ListAdapter listAdapter;


    public InboxFragment() {
    }


    public static InboxFragment newInstance(Bundle args) {
        InboxFragment fragment = new InboxFragment();
        if (args!=null) {
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this,root);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (getArguments()==null) {

        } else {
            p = (Patient) getArguments().getSerializable(Const.BUNDLE_KEY_PATIENT);

            /* Build Progress bar */
            buildProgressBar(p);

            /* Show progress */
            /* default treatment 50 */
            if (p.getAppointments()==null || p.getAppointments().isEmpty()) {
                tvProgressbarSubtitle.setText("0.0");
            } else {
            /* 52 weeks assumed */
                int treatmentsDone = p.getAppointments().size();
                int toGo = 52-treatmentsDone;
                float q = (treatmentsDone)/52;

                String percent =  String.format("%.1f",(q));
                tvTreatmentsDone.setText(String.valueOf(treatmentsDone));
                tvTreatmentsToGo.setText(String.valueOf((toGo)));
                tvProgressbarSubtitle.setText(percent);
            }


            /* Build appointments */
            buildAppointments(p);

            if (p.getSummaries()==null || p.getSummaries().isEmpty()) {
                tvNoContents.setVisibility(View.VISIBLE);
            } else {
                ArrayList data = p.getSummaries();
                listAdapter = new ListAdapter(data);
                recyclerView.setAdapter(listAdapter);
            }

        }

        return root;
    }

    private void buildAppointments(Patient p) {
        if (p.getAppointments()==null || p.getAppointments().isEmpty()) {
            cvUpcomingAppts.setVisibility(View.GONE);
        } else {
            int in = Commons.getMostUpcomingAppointmentIndex(p);
            Appointment a = p.getAppointments().get(in);
            SimpleDateFormat f = new SimpleDateFormat("EEE, MMM dd yyyy, HH:mm");
            String sdf = f.format(a.getDateFor());
            tvDateAppts.setText(sdf);
            String dr = a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName();
            tvDrAppts.setText(dr);
        }
    }
//
    private void buildProgressBar(Patient p) {
//
//        /* First time using app no appointments so 52 treatments left */
        if (p.getAppointments()==null || p.getAppointments().isEmpty()) {
//            progressBar.setProgress(1);
            tvTreatmentsDone.setText("1");
            float q = (1)/52;
            tvTreatmentsToGo.setText(String.valueOf((52-1)));
            String percent = String.format("%.1f",(q));
            tvProgressbarSubtitle.setText(percent);
        } else {
            /* 52 weeks assumed */
            int treatmentsDone = p.getAppointments().size();
            int toGo = 52-treatmentsDone;
            float q = (treatmentsDone)/52;
            String percent =  String.format("%.1f",(q));
            tvTreatmentsDone.setText(String.valueOf(treatmentsDone));
            tvTreatmentsToGo.setText(String.valueOf((toGo)));
            tvProgressbarSubtitle.setText(percent);
        }
    }

    @OnClick(R.id.inbox_button_more_info_appt)
    public void onMoreInfoButtonClick(View v) {
        ((MainActivity) getActivity()).onMoreInfoClick();
    }

    public class ListAdapter extends RecyclerView.Adapter<InboxFragment.ListAdapter.ViewHolder>{
        private ArrayList<Summary> data;
        public ListAdapter(ArrayList<Summary> _data){
            this.data = _data;
        }
        public class ViewHolder extends RecyclerView.ViewHolder{

            LinearLayout linearLayout;
            TextView title;
            TextView issued;
            TextView description;

            public ViewHolder(View itemView){
                super(itemView);
                this.linearLayout = itemView.findViewById(R.id.item_summary_linear_layout);
                this.title = itemView.findViewById(R.id.item_summary_title);
                this.description= itemView.findViewById(R.id.item_summary_description);
                this.issued = itemView.findViewById(R.id.item_summary_issued);


            }
        }
        @Override
        public InboxFragment.ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_summary, parent, false);
            InboxFragment.ListAdapter.ViewHolder viewHolder = new InboxFragment.ListAdapter.ViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(InboxFragment.ListAdapter.ViewHolder holder, final int position){
            holder.title.setText(data.get(position).getSummaryTitle());
            holder.description.setText(data.get(position).getDescription());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            String date = sdf.format(data.get(position).getDateIssued());

            holder.issued.setText(date);
            holder.linearLayout.setOnClickListener(view -> {

            });

        }
        @Override
        public int getItemCount(){
            return data.size();
        }
    }

}
