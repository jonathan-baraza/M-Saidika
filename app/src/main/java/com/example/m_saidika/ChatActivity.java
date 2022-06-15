package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_saidika.Adapters.MessagesAdapter;
import com.example.m_saidika.Models.Message;
import com.example.m_saidika.Models.Profile;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    public ImageView backArrow;
    public CircleImageView recipientProfilePic;
    public TextView recipientName;
    private String recipientId;
    private FirebaseUser fUser;
    
    public EditText message;
    private ImageButton btnSend,btnSendDisabled,btnSelectPhoto;


    public RecyclerView chatsRecView;
    public LinearLayoutManager layoutManager;
    public MessagesAdapter adapter;
    public ArrayList<Message> allMessages;

    public String recipientProfilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backArrow=findViewById(R.id.backArrow);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        recipientProfilePic=findViewById(R.id.recipientProfilePic);
        recipientName=findViewById(R.id.recipientName);
        
        message=findViewById(R.id.message);
        btnSend=findViewById(R.id.btnSend);
        btnSendDisabled=findViewById(R.id.btnSendDisabled);
        
        btnSelectPhoto=findViewById(R.id.btnSelectPhoto);
        
        Intent intent=getIntent();
        recipientId=intent.getStringExtra("recipientId");
        
        
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String messageTxt=editable.toString();
                if(messageTxt.length()>0){
                    btnSendDisabled.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }else{
                    btnSend.setVisibility(View.GONE);
                    btnSendDisabled.setVisibility(View.VISIBLE);
                }
            }
        });


        //view dp
        recipientProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this, ViewFullPhotoActivity.class);
                intent.putExtra("photoUrl",recipientProfilePicUrl);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(recipientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile userData=snapshot.getValue(Profile.class);
                if(userData.getPhoto().length()>0){
                    Picasso.get().load(userData.getPhoto()).placeholder(R.drawable.loader2).into(recipientProfilePic);
                    recipientProfilePicUrl=userData.getPhoto();
                }else{
                    recipientProfilePic.setImageResource(R.drawable.avatar1);
                }
                recipientName.setText(userData.getFirstName()+" "+userData.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //FetchMessages
        InitMessagesRecyclerView();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt=message.getText().toString();
                sendMessage(messageTxt,"false");
            }
        });
        
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(ChatActivity.this);
            }
        });

    }

    private void InitMessagesRecyclerView() {
        chatsRecView=findViewById(R.id.chatsRecView);
        layoutManager=new LinearLayoutManager(ChatActivity.this);
        chatsRecView.setLayoutManager(layoutManager);
        allMessages=new ArrayList<>();
        adapter=new MessagesAdapter(ChatActivity.this,allMessages);
        chatsRecView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fetchMessages();
    }

    private void fetchMessages() {
        FirebaseDatabase.getInstance().getReference().child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allMessages.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message messageInstance=snapshot.getValue(Message.class);
                    if((messageInstance.getSenderId().equals(fUser.getUid()) && messageInstance.getRecipientId().equals(recipientId)) || (messageInstance.getSenderId().equals(recipientId) && messageInstance.getRecipientId().equals(fUser.getUid()))){
                        allMessages.add(messageInstance);
                    }
                }
                adapter.notifyDataSetChanged();
                chatsRecView.scrollToPosition(allMessages.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String messageTxt,String isPhoto) {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference().child("Messages");
        //Getting current time
        DateFormat df=new SimpleDateFormat("h:mm a");
        String time=df.format(Calendar.getInstance().getTime());

        HashMap<String,Object> messageData=new HashMap<>();
        String messageId=dbRef.push().getKey();
        messageData.put("messageId",messageId);
        messageData.put("senderId",fUser.getUid());
        messageData.put("recipientId",recipientId);
        messageData.put("message",messageTxt);
        messageData.put("isPhoto",isPhoto);
        messageData.put("time",time);
        messageData.put("isDelivered","false");

        dbRef.child(messageId).setValue(messageData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Message has been sent successfully so clearing the edit text
                    message.setText("");
                }else{
                    System.out.println(task.getException().toString());
                }
            }
        });
    }

    private void insertPhotoToDB(Uri imageUri) {
        String[] splitArray=imageUri.getLastPathSegment().split("\\.");
        String fileExtension=splitArray[splitArray.length-1];
        StorageReference storageRef= FirebaseStorage.getInstance().getReference().child("Chats").child(System.currentTimeMillis()+fileExtension);
        StorageTask uploadTask=storageRef.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    return null;
                }else{
                    return storageRef.getDownloadUrl();
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri result=task.getResult();
                String downloadUrl=result.toString();
                sendMessage(downloadUrl,"true");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            Uri imageUri=result.getUri();
            insertPhotoToDB(imageUri);
        }
    }


}