package com.example.mustafa.exchange;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

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
    ArrayList<String> userdesiredFromFB;


    ArrayList<String> useremailsFromFB1;
    ArrayList<String> userimageFromFB1;
    ArrayList<String> usercommentFromFB1;
    ArrayList<String> userdesiredFromFB1;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Search_Adapter adapter;
    Search_Adapter adapter1;

    ListView listView;
    ListView matched;
    EditText SearchWord;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_others,container,false);
        useremailsFromFB = new ArrayList<String>();
        usercommentFromFB = new ArrayList<String>();
        userimageFromFB = new ArrayList<String>();
        userdesiredFromFB = new ArrayList<String>();


        useremailsFromFB1 = new ArrayList<String>();
        usercommentFromFB1 = new ArrayList<String>();
        userimageFromFB1 = new ArrayList<String>();
        userdesiredFromFB1 = new ArrayList<String>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();


        SearchWord=view.findViewById(R.id.SearchWord);

        adapter = new Search_Adapter(useremailsFromFB,userimageFromFB,usercommentFromFB, userdesiredFromFB, this.getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        adapter1=new Search_Adapter(useremailsFromFB1,userimageFromFB1,usercommentFromFB1, userdesiredFromFB1, this.getActivity());
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

                    }
                    else{
                    listView.removeAllViewsInLayout();
                    usercommentFromFB.clear();
                    useremailsFromFB.clear();
                    userimageFromFB.clear();
                    userdesiredFromFB.clear();

                    matched.removeAllViewsInLayout();
                    usercommentFromFB1.clear();
                    useremailsFromFB1.clear();
                    userimageFromFB1.clear();
                    userdesiredFromFB1.clear();

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
                userdesiredFromFB.clear();
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
                        userdesiredFromFB.add(hashMap.get("desiredthing"));
                        System.out.println("uuid kullanıcı!!!! ="+ UploadActivity.User_Uuid);
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
                userdesiredFromFB1.clear();

                matched.removeAllViewsInLayout();

                String bendeOlan="";
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if(hashMap.get("useremail").equals(MainActivity.userEmail) && !bendeOlan.contains(hashMap.get("itemname"))) {

                        bendeOlan+=hashMap.get("itemname")+'_';
                    }
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    Boolean b1=  !hashMap.get("useremail").equals(MainActivity.userEmail);

                    String itemname=hashMap.get("itemname").toLowerCase();

                    Boolean b3= itemname.contains(searchword.toString().toLowerCase())
                            && bendeOlan.contains(hashMap.get("desiredthing").toLowerCase().toLowerCase());
                    if(b1&&b3) {
                        useremailsFromFB1.add(hashMap.get("useremail"));
                        userimageFromFB1.add(hashMap.get("downloadurl"));
                        usercommentFromFB1.add(hashMap.get("itemname"));
                        userdesiredFromFB1.add(hashMap.get("desiredthing"));
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
