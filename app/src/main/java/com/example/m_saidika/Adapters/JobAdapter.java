package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.JobActivity;
import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.JobItem;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewApplication;
import com.example.m_saidika.ViewJobActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    public Context mContext;
    public List<JobItem> allJobItems;
    private FirebaseUser fUser;

    public JobAdapter(Context mContext, List<JobItem> allJobItems) {
        this.mContext = mContext;
        this.allJobItems = allJobItems;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @NonNull
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.ViewHolder holder, int position) {
        JobItem jobItem = allJobItems.get(position);
        holder.jobsCompanyName.setText("Company Name: " + jobItem.getCompanyName());
        holder.jobsLocation.setText("Location: " + jobItem.getLocation());
        holder.jobsDescription.setText("Description: " + jobItem.getDescription());

        holder.jobsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewJobActivity.class);
                intent.putExtra("jobId", jobItem.getJobId());
                intent.putExtra("owner", jobItem.getOwner());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return allJobItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView jobsCompanyName, jobsLocation, jobsDescription;
        public CardView jobsCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobsCompanyName=itemView.findViewById(R.id.jobsCompanyName);
            jobsLocation=itemView.findViewById(R.id.jobsLocation);
            jobsDescription=itemView.findViewById(R.id.jobsDescription);
            jobsCard=itemView.findViewById(R.id.jobsCard);

        }
    }

}
