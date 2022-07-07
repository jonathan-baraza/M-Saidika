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

import com.example.m_saidika.Models.HouseItem;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewHouseActivity;
import com.example.m_saidika.ViewJobActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    public Context mContext;
    public List<HouseItem> allHouseItems;
    private FirebaseUser fUser;

    public HouseAdapter(Context mContext, List<HouseItem> allHouseItems) {
        this.mContext = mContext;
        this.allHouseItems = allHouseItems;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public HouseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseAdapter.ViewHolder holder, int position) {
        HouseItem houseItem = allHouseItems.get(position);
        Picasso.get().load(houseItem.getApartmentPic()).placeholder(R.drawable.ic_house).into(holder.housePic);
        holder.houseName.setText(houseItem.getApartmentName());
        holder.houseLocation.setText("Location: " + houseItem.getLocation());
        holder.houseRent.setText("Rent: " + houseItem.getRent());

        holder.houseContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewHouseActivity.class);
                intent.putExtra("houseId", houseItem.getHouseId());
                intent.putExtra("owner", houseItem.getOwner());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allHouseItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView houseName, houseLocation, houseRent;
        public RelativeLayout houseContainer;
        public ImageView housePic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            houseContainer=itemView.findViewById(R.id.houseContainer);
            housePic=itemView.findViewById(R.id.housePic);
            houseName=itemView.findViewById(R.id.houseName);
            houseLocation=itemView.findViewById(R.id.houseLocation);
            houseRent=itemView.findViewById(R.id.houseRent);


        }
    }
}
