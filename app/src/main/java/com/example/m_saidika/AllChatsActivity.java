package com.example.m_saidika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.m_saidika.Adapters.AllMessagesAdapter;
import com.example.m_saidika.Adapters.AllUsersAdapter;
import com.example.m_saidika.Models.Message;
import com.example.m_saidika.Models.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class AllChatsActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public RecyclerView allMessagesRecView,allUsersRecView;
    public FloatingActionButton fab;
    public RelativeLayout chatsLayout,allUsersLayout;

    public ImageView closeAllUsersLayout;
    public EditText search;

    public LinearLayoutManager layoutManager;
    public AllUsersAdapter allUsersAdapter;
    public AllMessagesAdapter allMessagesAdapter;
    public ArrayList<Message> allMessages;
    public ArrayList<Message> userMessages;
    public ArrayList<Profile> allUsers;
    public ArrayList<Profile> allUsersHolder=new ArrayList<>();

    private FirebaseUser fUser;
    private String searchParameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbar);
        fab=findViewById(R.id.fab);

        chatsLayout=findViewById(R.id.chatsLayout);
        allUsersLayout=findViewById(R.id.allUsersLayout);

        closeAllUsersLayout=findViewById(R.id.closeAllUsersLayout);
        search=findViewById(R.id.search);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Chats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //For all messages
        initAllMessages();

        //For all users
        initAllUsersRecView();

        //Search for users
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                searchParameter=editable.toString();
                if(searchParameter.length()>0){
                    ArrayList<Profile> searchedUsers=new ArrayList<>();
                    if(allUsers.size()>0){
                        searchedUsers.clear();
                        for(Profile user:allUsers){
                            String fullName=user.getFirstName()+" "+user.getLastName();
                            if(fullName.toLowerCase().contains(searchParameter.toLowerCase())){
                                searchedUsers.add(user);
                            }

                        }
                    }
                    allUsersAdapter.setAllUsers(searchedUsers);
                    allUsersAdapter.notifyDataSetChanged();
                }else{
                    allUsersAdapter.setAllUsers(allUsersHolder);
                    allUsersAdapter.notifyDataSetChanged();

                }
                allUsersAdapter.notifyDataSetChanged();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                chatsLayout.setVisibility(View.GONE);
                allUsersLayout.setVisibility(View.VISIBLE);
            }
        });

        closeAllUsersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUsersLayout.setVisibility(View.GONE);
                chatsLayout.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initAllMessages() {
        allMessagesRecView=findViewById(R.id.allMessagesRecView);
        layoutManager=new LinearLayoutManager(AllChatsActivity.this);
        allMessagesRecView.setLayoutManager(layoutManager);
        allMessages=new ArrayList<>();
        userMessages=new ArrayList<>();
        allMessagesAdapter=new AllMessagesAdapter(AllChatsActivity.this,userMessages);
        allMessagesRecView.setAdapter(allMessagesAdapter);
        allMessagesAdapter.notifyDataSetChanged();

        fetchAllMessages();

    }

    private void fetchAllMessages() {
        FirebaseDatabase.getInstance().getReference().child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allMessages.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message=snapshot.getValue(Message.class);
                    if(message.getSenderId().equals(fUser.getUid()) || message.getRecipientId().equals(fUser.getUid())){
                        allMessages.add(message);
                    }
                }
                filterMessages(allMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterMessages(ArrayList<Message> allMessages) {
        //Reverse to get the latest first
        Collections.reverse(allMessages);
        //Create array of ids
        ArrayList<String> ids=new ArrayList<>();
        ids.clear();
        userMessages.clear();
        for(Message message:allMessages){
            String comboOne=message.getSenderId()+message.getRecipientId();
            String comboTwo=message.getRecipientId()+message.getSenderId();
            if(!(ids.contains(comboOne) || ids.contains(comboTwo))){
                userMessages.add(message);
                ids.add(comboOne);
                ids.add(comboTwo);
            }
        }
        allMessagesAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle("My Chats ("+userMessages.size()+")");
    }

    private void initAllUsersRecView() {
        allUsersRecView=findViewById(R.id.allUsersRecView);
        layoutManager=new LinearLayoutManager(AllChatsActivity.this);
        allUsersRecView.setLayoutManager(layoutManager);
        allUsers=new ArrayList<>();
        allUsersAdapter =new AllUsersAdapter(allUsers,AllChatsActivity.this);
        allUsersRecView.setAdapter(allUsersAdapter);
        allUsersAdapter.notifyDataSetChanged();
        getAllUsers();
    }

    private void getAllUsers() {
        FirebaseDatabase.getInstance().getReference().child("Profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsersHolder.clear();
                allUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Profile userData=snapshot.getValue(Profile.class);
                    if(!fUser.getUid().equals(userData.getUserId())){
                        allUsers.add(userData);
                        allUsersHolder.add(userData);
                    }

                }

                allUsersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}