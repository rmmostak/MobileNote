package com.rmproduct.locknote.MobileNote;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rmproduct.locknote.AboutActivity;
import com.rmproduct.locknote.DatabaseHelper;
import com.rmproduct.locknote.ForceUpdate.ForceUpdateChecker;
import com.rmproduct.locknote.LockNote.SecurityCheck;
import com.rmproduct.locknote.MakeOnline.Authentication;
import com.rmproduct.locknote.R;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForceUpdateChecker.OnUpdateNeededListener {

    private SwipeRefreshLayout refreshLayout;
    private TextView notice;
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private List<Model> modelList = new ArrayList<>();
    private long backPressedTime;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        reference = FirebaseDatabase.getInstance().getReference("Notes");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        notice = findViewById(R.id.notice);
        recyclerView = findViewById(R.id.recycler);
        actionButton = findViewById(R.id.actionButton);
        databaseHelper = new DatabaseHelper(this);
        modelList.addAll(databaseHelper.noteList());

        if (modelList.isEmpty()) {
            notice.setVisibility(View.VISIBLE);
        } else {
            refreshLayout = findViewById(R.id.refreshLayout);

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                }
            });

            adapter = new NoteAdapter(this, modelList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MakeNote.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Press Again to Exit!", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.encrypted_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //startActivity(new Intent(HomePage.this, SecurityCheck.class));

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update this app and enjoy more functions.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                //redirectStore(updateUrl);
                                //Toast.makeText(getApplicationContext(), "This is the link for update", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
        dialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pass) {
            Intent intent = new Intent(HomePage.this, SecurityCheck.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions options = null;
            options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            startActivity(intent, options.toBundle());

        } else if (id == R.id.nav_online) {

            startActivity(new Intent(HomePage.this, Authentication.class));

        } else if (id == R.id.nav_tools) {
            startActivity(new Intent(HomePage.this, MainActivity.class));

        } else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "https://rmproduct121.blogspot.com/2020/10/mobile-note-android-app.html";
            String shareSubject = "Lock Note Android App";

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            startActivity(Intent.createChooser(shareIntent, "Share App Using..."));

        } else if (id == R.id.nav_about) {

            startActivity(new Intent(HomePage.this, AboutActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.toString();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String noteID = reference.push().getKey();
        final Model model1 = new Model();

        reference.child(noteID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model model2 = snapshot.getValue(Model.class);
                    Model model = new Model();
                    if (model != null && model2 != null) {
                        if (model1.getId() != model2.getId()) {
                            Model model3 = new Model(noteID, model.getDate(), model.getTime(), model.getTitle(), model.getBody(), model.getId());
                            reference.setValue(model3);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
