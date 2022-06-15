package com.example.m_saidika.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_saidika.Models.Message;
import com.example.m_saidika.R;
import com.example.m_saidika.ViewFullPhotoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{
    public Context mContext;
    public ArrayList<Message> allMessages;
    private FirebaseUser fUser;

    public MessagesAdapter(Context mContext, ArrayList<Message> allMessages) {
        this.mContext = mContext;
        this.allMessages = allMessages;
        fUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message=allMessages.get(position);

        if(message.getSenderId().equals(fUser.getUid())){
            holder.recipientLayout.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            if(message.isPhoto.equals("true")){
                holder.senderMessage.setVisibility(View.GONE);
                holder.senderPhoto.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getMessage()).placeholder(R.drawable.loader2).into(holder.senderPhoto);
                holder.senderPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPhoto(message.getMessage());
                    }
                });
            }else{
                holder.senderPhoto.setVisibility(View.GONE);
                holder.senderMessage.setVisibility(View.VISIBLE);
                holder.senderMessage.setText(message.getMessage());
            }
            holder.senderTime.setText(message.getTime());
        }else{
            holder.senderLayout.setVisibility(View.GONE);
            holder.recipientLayout.setVisibility(View.VISIBLE);

            if(message.isPhoto.equals("true")){
                holder.recipientMessage.setVisibility(View.GONE);
                holder.recipientPhoto.setVisibility(View.VISIBLE);
                Picasso.get().load(message.getMessage()).placeholder(R.drawable.loader2).into(holder.recipientPhoto);
                holder.recipientPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPhoto(message.message);
                    }
                });
            }else{
                holder.recipientPhoto.setVisibility(View.GONE);
                holder.recipientMessage.setVisibility(View.VISIBLE);
                holder.recipientMessage.setText(message.getMessage());
            }
            holder.recipientTime.setText(message.getTime());

        }


    }

    private void viewPhoto(String message) {
        Intent intent=new Intent(mContext, ViewFullPhotoActivity.class);
        intent.putExtra("photoUrl",message);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout senderLayout,recipientLayout;
        public TextView senderMessage,recipientMessage,senderTime,recipientTime;
        public ImageView senderPhoto,recipientPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderLayout=itemView.findViewById(R.id.senderLayout);
            recipientLayout=itemView.findViewById(R.id.recipientLayout);
            senderMessage=itemView.findViewById(R.id.senderMessage);
            recipientMessage=itemView.findViewById(R.id.recipientMessage);
            senderTime=itemView.findViewById(R.id.senderTime);
            recipientTime=itemView.findViewById(R.id.recipientTime);
            senderPhoto=itemView.findViewById(R.id.senderPhoto);
            recipientPhoto=itemView.findViewById(R.id.recipientPhoto);
        }
    }
}
