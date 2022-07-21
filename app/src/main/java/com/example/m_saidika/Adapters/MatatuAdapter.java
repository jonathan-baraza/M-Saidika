package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.m_saidika.Models.Matatu;
import com.example.m_saidika.Models.Passenger;
import com.example.m_saidika.R;
import com.example.m_saidika.TransportActivity;
import com.example.m_saidika.ViewMatatuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatatuAdapter extends RecyclerView.Adapter<MatatuAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Matatu> allMatatus;
    private FirebaseUser fUser;
    private String serviceId;

    public MatatuAdapter(Context mContext, ArrayList<Matatu> allMatatus,String serviceId) {
        this.mContext = mContext;
        this.allMatatus = allMatatus;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
        this.serviceId=serviceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.matatu_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(R.drawable.vehicle).into(holder.image);
        Matatu matatu=allMatatus.get(position);

        holder.destination.setText("Destination: "+matatu.getDestination());
        holder.departureTime.setText("Departure time: "+matatu.getDepartureTime());
        holder.price.setText("Fare price: Ksh "+matatu.getFarePrice()+"/=");

        FirebaseDatabase.getInstance().getReference().child("Passengers").child(matatu.getMatatuId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Passenger> allPassengers =new ArrayList<>();
                allPassengers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Passenger passenger=snapshot.getValue(Passenger.class);
                    allPassengers.add(passenger);
                }

                int seatsRem=Integer.parseInt(matatu.getCapacity())-allPassengers.size();
                holder.capacity.setText("Seats remaining: #"+seatsRem);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.matatuContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ViewMatatuActivity.class);
                intent.putExtra("matatuId",matatu.getMatatuId());
                intent.putExtra("serviceId",serviceId);
                mContext.startActivity(intent);
            }
        });

        if(fUser.getUid().equals(serviceId)){
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child("Transport").child(serviceId).child(matatu.getMatatuId()).removeValue();
                Toast.makeText(mContext, "Matatu deleted successfully", Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(mContext, TransportActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return allMatatus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout matatuContainer;
        private TextView destination,departureTime,capacity,price,btnDelete;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image);
            matatuContainer=itemView.findViewById(R.id.matatuContainer);
            destination=itemView.findViewById(R.id.destination);
            departureTime=itemView.findViewById(R.id.departureTime);
            capacity=itemView.findViewById(R.id.capacity);
            price=itemView.findViewById(R.id.price);
            btnDelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
