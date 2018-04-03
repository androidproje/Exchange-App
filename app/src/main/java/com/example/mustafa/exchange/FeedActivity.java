package com.example.mustafa.exchange;


import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    static ArrayList<String> useremailsFromFB;
    static ArrayList<String> userimageFromFB;
    static ArrayList<String> usercommentFromFB;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    PostClass adapter;
    GridView gridView;
    RecyclerView recyclerView;
    SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        sectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.container);
        setupPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.phone);
        tabLayout.getTabAt(1).setIcon(R.drawable.car_icon);

      /* useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new PostClass(useremailsFromFB,userimageFromFB,usercommentFromFB,this);
        gridView = (GridView) findViewById(R.id.listview1);
        gridView.setAdapter(adapter);
*/
        //getDataFromFirebase();

    }
   public void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    useremailsFromFB.add(hashMap.get("useremail"));
                    userimageFromFB.add(hashMap.get("downloadurl"));
                    usercommentFromFB.add(hashMap.get("itemname"));

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void setupPager(ViewPager viewPager) {

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragmentPhone(), "phone");
        adapter.addFragment(new fragmentCars(),"cars");
        adapter.addFragment(new fragmentOther(),"others");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.refresh){
            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}