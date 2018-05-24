package com.example.mustafa.exchange;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedActivity extends AppCompatActivity {

    SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        OneSignal.sendTag("User_ID",MainActivity.userEmail);

        sectionsPageAdapter=new SectionsPageAdapter(getSupportFragmentManager());
        viewPager=(ViewPager)findViewById(R.id.container);
        setupPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

         tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#E0F7FA")));
         tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#E0F7FA"));//tablayout çizgi rengi
        // tabLayout.getTabAt(0).setIcon(R.drawable.phone);
        //tabLayout.getTabAt(1).setIcon(R.drawable.car_icon);

    }
    private void setupPager(ViewPager viewPager) {

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragmentAllitems(), "All items");
        adapter.addFragment(new fragmentMyitems(),"My items");
        adapter.addFragment(new fragmentSearch(),"Search Items");
        adapter.addFragment(new FragmentMultiple(),"Multiple Exchange");

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