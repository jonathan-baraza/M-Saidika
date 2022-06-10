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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodServiceAdapter extends RecyclerView.Adapter<FoodServiceAdapter.ViewHolder>{

    public Context mContext;
    public ArrayList<ApplicationItem> allApplications;
    public FirebaseUser fUser;

    public FoodServiceAdapter(Context mContext, ArrayList<ApplicationItem> allApplications) {
        this.mContext = mContext;
        this.allApplications = allApplications;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_service_item,parent,false);
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
        holder.foodServiceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, FoodMenuActivity.class);
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
        public RelativeLayout foodServiceContainer;
        public ImageView image;
        public TextView name,description,location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            foodServiceContainer=itemView.findViewById(R.id.foodServiceContainer);
            location=itemView.findViewById(R.id.location);
        }
    }
}
