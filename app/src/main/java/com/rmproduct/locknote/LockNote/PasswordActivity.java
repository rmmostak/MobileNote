package com.rmproduct.locknote.LockNote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.MobileNote.MainActivity;
import com.rmproduct.locknote.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordActivity extends AppCompatActivity {

    private TextView notice;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private DatabaseHelper databaseHelper;
    private PassAdapter adapter;
    private List<PassModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        notice=findViewById(R.id.notice);
        recyclerView = findViewById(R.id.recycler);
        actionButton = findViewById(R.id.actionButton);
        refreshLayout=findViewById(R.id.refreshLayout);
        databaseHelper = new DatabaseHelper(this);

        databaseHelper=new DatabaseHelper(this);
        modelList.addAll(databaseHelper.passList());

        if (modelList.isEmpty()) {
            notice.setVisibility(View.VISIBLE);
        } else {

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            });

            adapter = new PassAdapter(this, modelList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordActivity.this, MakePassNote.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(PasswordActivity.this, MainActivity.class));

        super.onBackPressed();
    }
}
