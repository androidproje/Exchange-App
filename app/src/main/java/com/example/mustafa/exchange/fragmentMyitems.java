package com.example.mustafa.exchange;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;

public class fragmentMyitems extends Fragment {

    ArrayList<String> useremailsFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercommentFromFB;
    static ArrayList<String> uuidofItems;

    FirebaseDatabase firebaseDatabase;
    static DatabaseReference myRef;
    static MyItems_Adapter adapter;
    ImageView deleteitems;
    static String MyItems;

     static   GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         final View view=inflater.inflate(R.layout.fragment_cars,container,false);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.myitems_list,null,true);

        useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        uuidofItems=new ArrayList<String>();

        deleteitems=(ImageView) view.findViewById(R.id.delete);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new MyItems_Adapter(useremailsFromFB,userimageFromFB,usercommentFromFB,this.getActivity(),myRef);
        gridView = (GridView) view.findViewById(R.id.gridview1);
        gridView.setAdapter(adapter);
        getDataFromFirebase();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("UUIDofItem",uuidofItems.get(position));
                startActivity(intent);
                 }
        });
        return view;
    }
    protected void getDataFromFirebase() {

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                       if(hashMap.get("useremail").equals(MainActivity.userEmail)) {

                           useremailsFromFB.add(hashMap.get("useremail"));
                           userimageFromFB.add(hashMap.get("downloadurl"));
                           usercommentFromFB.add(hashMap.get("itemname"));
                           uuidofItems.add(ds.getKey());
                           MyItems+=hashMap.get("itemname");

                           adapter.notifyDataSetChanged();
                       }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                }
        });

    }
}

