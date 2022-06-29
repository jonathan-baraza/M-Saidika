package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.Models.OrderItem;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewOrderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private FirebaseUser fUser;
    private ArrayList<OrderItem> allOrders;
    private Context mContext;

    public OrderAdapter(ArrayList<OrderItem> allOrders, Context mContext) {
        this.allOrders = allOrders;
        this.mContext = mContext;
        this.fUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.food_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem=allOrders.get(position);
        holder.foodName.setText(orderItem.getName());
        holder.status.setText("Status: "+orderItem.getStatus());
        holder.time.setText("Ordered at "+orderItem.getTime());
        //getting the user using the id
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(orderItem.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile customerProfile=snapshot.getValue(Profile.class);
                holder.customerName.setText("Customer: "+customerProfile.getFirstName()+" "+customerProfile.getLastName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.orderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ViewOrderActivity.class);
                intent.putExtra("orderId",orderItem.getOrderId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout orderContainer;
        private TextView foodName,customerName,time,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderContainer=itemView.findViewById(R.id.orderContainer);
            foodName=itemView.findViewById(R.id.foodName);
            customerName=itemView.findViewById(R.id.customerName);
            time=itemView.findViewById(R.id.time);
            status=itemView.findViewById(R.id.status);
        }
    }
}
