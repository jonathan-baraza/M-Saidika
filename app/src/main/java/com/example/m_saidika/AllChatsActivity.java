package com.example.m_saidika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AllChatsActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public RecyclerView allChatsRecView,allUsersRecView;
    public FloatingActionButton fab;
    public RelativeLayout chatsLayout,allUsersLayout;

    public ImageView closeAllUsersLayout;
    public EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        toolbar=findViewById(R.id.toolbar);
        allChatsRecView=findViewById(R.id.allChatsRecView);
        allUsersRecView=findViewById(R.id.allUsersRecView);
        fab=findViewById(R.id.fab);

        chatsLayout=findViewById(R.id.chatsLayout);
        allUsersLayout.findViewById(R.id.allUsersLayout);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatsLayout.setVisibility(View.GONE);
                allUsersLayout.setVisibility(View.VISIBLE);
            }
        });

        closeAllUsersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUsersLayout.setVisibility(View.GONE);
                chatsLayout.setVisibility(View.VISIBLE);
            }
        });


    }
}