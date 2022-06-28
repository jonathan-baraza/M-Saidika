package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.ChatActivity;
import com.example.m_saidika.Models.Profile;
import com.example.m_saidika.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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
        Profile userProfile=allUsers.get(position);
        if(userProfile.getPhoto().length()>0){
            Picasso.get().load(userProfile.getPhoto()).placeholder(R.drawable.loader2).into(holder.profilePic);
        }else{
            holder.profilePic.setImageResource(R.drawable.avatar1);
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
    }

    public void setAllUsers(ArrayList<Profile> allUsers) {
        this.allUsers = allUsers;
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profilePic;
        public TextView name;
        public Button btnStartChat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profilePic);
            name=itemView.findViewById(R.id.name);
            btnStartChat=itemView.findViewById(R.id.btnStartChat);
        }
    }
}
