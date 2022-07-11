package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.ChatActivity;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder>{
    private ArrayList<Profile> allUsers;
    private Context mContext;
    private FirebaseUser fUser;

    public AllUsersAdapter(ArrayList<Profile> allUsers, Context mContext) {
        this.allUsers = allUsers;
        this.mContext = mContext;
        fUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
       ViewHolder holder=new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile authUserProfile=snapshot.getValue(Profile.class);
                if(authUserProfile.getRole().equals("admin")){
                    holder.btnCall.setVisibility(View.GONE);
                    holder.btnStartChat.setVisibility(View.GONE);
                    holder.adminButtons.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Profile userProfile=allUsers.get(position);
        if(userProfile.getPhoto().length()>0){
            Picasso.get().load(userProfile.getPhoto()).placeholder(R.drawable.loader2).into(holder.profilePic);
        }else{
            holder.profilePic.setImageResource(R.drawable.avatar1);
        }

        if(userProfile.getStatus().equals("active")){
            holder.btnEnable.setVisibility(View.GONE);
            holder.btnDisable.setVisibility(View.VISIBLE);
        }else{
            holder.btnEnable.setVisibility(View.VISIBLE);
            holder.btnDisable.setVisibility(View.GONE);
        }

        holder.name.setText(userProfile.getFirstName()+" "+userProfile.getLastName());

        holder.btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra("recipientId",userProfile.getUserId());
                mContext.startActivity(intent);

            }
        });

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+userProfile.getPhone()));
                mContext.startActivity(intent);
            }
        });

        //Disable users
        holder.btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> updatedStatus=new HashMap<>();
                updatedStatus.put("status","disabled");
                FirebaseDatabase.getInstance().getReference().child("Profiles").child(userProfile.getUserId()).updateChildren(updatedStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "User disabled", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "Failed to disable user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //Enable users
        holder.btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> updatedStatus=new HashMap<>();
                updatedStatus.put("status","active");
                FirebaseDatabase.getInstance().getReference().child("Profiles").child(userProfile.getUserId()).updateChildren(updatedStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "User activated", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "Failed to activate user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public void setAllUsers(ArrayList<Profile> allUsers) {
        this.allUsers = allUsers;
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profilePic;
        private TextView name;
        private ImageView btnStartChat,btnCall;
        private Button btnDisable,btnEnable;
        private LinearLayout adminButtons;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profilePic);
            name=itemView.findViewById(R.id.name);
            btnStartChat=itemView.findViewById(R.id.btnStartChat);
            btnCall=itemView.findViewById(R.id.btnCall);
            btnDisable=itemView.findViewById(R.id.btnDisable);
            btnEnable=itemView.findViewById(R.id.btnEnable);
            adminButtons=itemView.findViewById(R.id.adminButtons);
        }
    }
}
