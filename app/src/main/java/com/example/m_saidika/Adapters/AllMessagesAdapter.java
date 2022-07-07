package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.ChatActivity;
import com.example.m_saidika.Models.Message;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AllMessagesAdapter extends RecyclerView.Adapter<AllMessagesAdapter.ViewHolder>{
    public Context mContext;
    public ArrayList<Message> allMessages;
    private FirebaseUser fUser;

    public AllMessagesAdapter(Context mContext, ArrayList<Message> allMessages) {
        this.mContext = mContext;
        this.allMessages = allMessages;
        this.fUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message=allMessages.get(position);

        String userId;

        if(message.getSenderId().equals(fUser.getUid())){
            userId=message.getRecipientId();
            holder.message.setTypeface(null,Typeface.NORMAL);
        }else{
            userId=message.getSenderId();
            holder.message.setTypeface(null, Typeface.BOLD);
        }

        if(message.getIsPhoto().equals("true")){
            holder.message.setVisibility(View.GONE);
            holder.photoMessage.setVisibility(View.VISIBLE);
        }else{
            holder.photoMessage.setVisibility(View.GONE);
            holder.message.setText(message.getMessage());
        }

        holder.time.setText(message.getTime());

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userData=snapshot.getValue(Profile.class);
                if(userData.getPhoto().length()>0){
                    Picasso.get().load(userData.getPhoto()).placeholder(R.drawable.loader2).into(holder.profilePic);
                }else{
                    holder.profilePic.setImageResource(R.drawable.avatar1);
                }
                holder.name.setText(userData.getFirstName()+" "+userData.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra("recipientId",userId);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView profilePic;
        public TextView name,message,photoMessage,time;
        public RelativeLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profilePic);
            name=itemView.findViewById(R.id.name);
            message=itemView.findViewById(R.id.message);
            photoMessage=itemView.findViewById(R.id.photoMessage);
            time=itemView.findViewById(R.id.time);
            container=itemView.findViewById(R.id.container);
        }
    }
}
