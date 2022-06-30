package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.FoodMenuActivity;
import com.example.m_saidika.Models.ApplicationItem;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewTransportActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ApplicationServiceAdapter extends RecyclerView.Adapter<ApplicationServiceAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<ApplicationItem> allApplications;
    private FirebaseUser fUser;
    private String serviceType;

    public ApplicationServiceAdapter(Context mContext, ArrayList<ApplicationItem> allApplications,String serviceType) {
        this.mContext = mContext;
        this.allApplications = allApplications;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
        this.serviceType=serviceType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.application_service_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationItem applicationItem=allApplications.get(position);
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(applicationItem.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile=snapshot.getValue(Profile.class);
                if(profile.getPhoto().length()>0){
                    Picasso.get().load(profile.getPhoto()).placeholder(R.drawable.loader2).into(holder.image);
                }else{
                    holder.image.setImageResource(R.drawable.cartoon_restaurant);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.name.setText(applicationItem.getCompanyName());
        holder.description.setText(applicationItem.getDescription());
        holder.location.setText("Location: "+applicationItem.getLocation());
        holder.serviceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(serviceType.equals("Food")){
                     intent=new Intent(mContext, FoodMenuActivity.class);
                }else{
                     intent=new Intent(mContext, ViewTransportActivity.class);
                }

                intent.putExtra("id",applicationItem.getUserId());
                intent.putExtra("title",applicationItem.getCompanyName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allApplications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout serviceContainer;
        public ImageView image;
        public TextView name,description,location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            serviceContainer =itemView.findViewById(R.id.foodServiceContainer);
            location=itemView.findViewById(R.id.location);
        }
    }
}
