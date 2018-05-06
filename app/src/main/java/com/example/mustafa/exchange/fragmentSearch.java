package com.example.mustafa.exchange;
import android.app.Notification;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class fragmentSearch extends Fragment {

    ArrayList<String> useremailsFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> usercommentFromFB;

    ArrayList<String> useremailsFromFB1;
    ArrayList<String> userimageFromFB1;
    ArrayList<String> usercommentFromFB1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Search_Adapter adapter;
    Search_Adapter adapter1;

    ListView listView;
    String receiving="";
    String istenen="";


    ListView matched;
    EditText SearchWord;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_others,container,false);
        useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();

        useremailsFromFB1 = new ArrayList<String>();
        usercommentFromFB1 = new ArrayList<String>();
        userimageFromFB1 = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        SearchWord=view.findViewById(R.id.SearchWord);

        adapter = new Search_Adapter(useremailsFromFB,userimageFromFB,usercommentFromFB,this.getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        adapter1=new Search_Adapter(useremailsFromFB1,userimageFromFB1,usercommentFromFB1,this.getActivity());
        matched=view.findViewById(R.id.exactmatch);
        matched.setAdapter(adapter1);

        SearchWord.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    getDataFromFirebase(s.toString());
                    getDataExactMatch(s.toString());
                    System.out.println("receiving = "+ receiving);
                    System.out.println("istenen = "+istenen);

                }
                    else{
                    listView.removeAllViewsInLayout();
                    usercommentFromFB.clear();
                    useremailsFromFB.clear();
                    userimageFromFB.clear();

                    matched.removeAllViewsInLayout();
                    usercommentFromFB1.clear();
                    useremailsFromFB1.clear();
                    userimageFromFB1.clear();

                }

            }
        });
        return view;
    }

    protected void getDataFromFirebase(final String searchword) {

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usercommentFromFB.clear();
                useremailsFromFB.clear();
                userimageFromFB.clear();
                listView.removeAllViewsInLayout();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if(hashMap.get("itemname").toString().toLowerCase().contains(searchword.toString().toLowerCase())
                            ||hashMap.get("itemname").toString().toUpperCase().contains(searchword.toString().toUpperCase())) {
                        useremailsFromFB.add(hashMap.get("useremail"));
                        userimageFromFB.add(hashMap.get("downloadurl"));
                        usercommentFromFB.add(hashMap.get("itemname"));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void getDataExactMatch(final String searchword) {
        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usercommentFromFB1.clear();
                useremailsFromFB1.clear();
                userimageFromFB1.clear();
                matched.removeAllViewsInLayout();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if(hashMap.get("useremail").equals(MainActivity.userEmail)&&!receiving.contains( hashMap.get("itemname"))) {

                        receiving=receiving+" "+ hashMap.get("itemname")+",";
                    }
                    if(!hashMap.get("useremail").equals(MainActivity.userEmail)&&!istenen.contains(hashMap.get("desiredthing"))){
                        istenen=istenen+" "+hashMap.get("desiredthing")+",";
                    }
                    if(!hashMap.get("useremail").equals(MainActivity.userEmail)&&hashMap.get("itemname").contains(searchword.toString().toLowerCase())
                            ||hashMap.get("itemname").contains(searchword.toString().toUpperCase())
                            && istenen.contains(receiving)

                            ){
                        useremailsFromFB1.add(hashMap.get("useremail"));
                        userimageFromFB1.add(hashMap.get("downloadurl"));
                        usercommentFromFB1.add(hashMap.get("itemname"));

                    }
                    adapter1.notifyDataSetChanged();

                 }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
