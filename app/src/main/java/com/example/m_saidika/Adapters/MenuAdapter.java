package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.Models.FoodItem;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewMenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    public Context mContext;
    public List<FoodItem> allItems;
    private FirebaseUser fUser;
    private String foodServiceId;

    public MenuAdapter(Context mContext, List<FoodItem> allItems,String foodServiceId) {
        this.mContext = mContext;
        this.allItems = allItems;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
        this.foodServiceId=foodServiceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem=allItems.get(position);
        holder.name.setText(foodItem.getName());
        holder.price.setText("Ksh "+foodItem.getPrice());
        Picasso.get().load(foodItem.getPhoto()).placeholder(R.drawable.loader2).into(holder.image);
        holder.parentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ViewMenuItem.class);
                intent.putExtra("foodServiceId",foodServiceId);
                intent.putExtra("photo",foodItem.getPhoto());
                intent.putExtra("name",foodItem.getName());
                intent.putExtra("price",foodItem.getPrice());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name,price;
        public ImageView image;
        public CardView parentContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.foodName);
            price=itemView.findViewById(R.id.foodPrice);
            image=itemView.findViewById(R.id.foodImage);
            parentContainer=itemView.findViewById(R.id.parentContainer);
        }
    }
}
