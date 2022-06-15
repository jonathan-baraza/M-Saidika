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
import android.widget.Toast;

import com.example.m_saidika.Adapters.AllUsersAdapter;
import com.example.m_saidika.Models.Profile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class AllChatsActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public RecyclerView allChatsRecView,allUsersRecView;
    public FloatingActionButton fab;
    public RelativeLayout chatsLayout,allUsersLayout;

    public ImageView closeAllUsersLayout;
    public EditText search;

    public LinearLayoutManager layoutManager;
    public AllUsersAdapter adapter;
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
        allChatsRecView=findViewById(R.id.allChatsRecView);
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
                            if(fullName.toLowerCase().startsWith(searchParameter.toLowerCase())){
                                searchedUsers.add(user);
                            }
                        }
                    }
                    adapter.setAllUsers(searchedUsers);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.setAllUsers(allUsersHolder);
                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();
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

    private void initAllUsersRecView() {
        allUsersRecView=findViewById(R.id.allUsersRecView);
        layoutManager=new LinearLayoutManager(AllChatsActivity.this);
        allUsersRecView.setLayoutManager(layoutManager);
        allUsers=new ArrayList<>();
        adapter =new AllUsersAdapter(allUsers,AllChatsActivity.this);
        allUsersRecView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}