package com.example.mustafa.exchange;

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


public class FragmentMultiple extends android.support.v4.app.Fragment {


    ArrayList<String> useremailsFromFB_1kişi;
    ArrayList<String> userimageFromFB_1kişi;
    ArrayList<String> usercommentFromFB_1kişi;
    ArrayList<String> userdesiredFromFB_1kişi;

    ArrayList<String> useremailsFromFB_2kişi;
    ArrayList<String> userimageFromFB_2kişi;
    ArrayList<String> usercommentFromFB_2kişi;
    ArrayList<String> userdesiredFromFB_2kişi;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Multiple_Exchange_Adapter adapter;
    ListView listView;
    EditText SearchWord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_multiple,container,false);

        SearchWord=view.findViewById(R.id.SearchWord);
        useremailsFromFB_1kişi = new ArrayList<String>();
        usercommentFromFB_1kişi = new ArrayList<String>();
        userimageFromFB_1kişi = new ArrayList<String>();
        userdesiredFromFB_1kişi = new ArrayList<String>();

        useremailsFromFB_2kişi= new ArrayList<String>();
        usercommentFromFB_2kişi = new ArrayList<String>();
        userimageFromFB_2kişi = new ArrayList<String>();
        userdesiredFromFB_2kişi = new ArrayList<String>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new Multiple_Exchange_Adapter(useremailsFromFB_1kişi,userimageFromFB_1kişi,usercommentFromFB_1kişi, userdesiredFromFB_1kişi, useremailsFromFB_2kişi,userimageFromFB_2kişi,usercommentFromFB_2kişi, userdesiredFromFB_2kişi, this.getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

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
                    }
                else{
                    listView.removeAllViewsInLayout();
                    usercommentFromFB_1kişi.clear();
                    useremailsFromFB_1kişi.clear();
                    userimageFromFB_1kişi.clear();
                    userdesiredFromFB_1kişi.clear();

                    usercommentFromFB_2kişi.clear();
                    useremailsFromFB_2kişi.clear();
                    userimageFromFB_2kişi.clear();
                    usercommentFromFB_2kişi.clear();

                }

            }
        });
        return  view;
    }

    protected void getDataFromFirebase(final String searchword) {

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String kişi_1_istenen="";
                String kişi_1_mailler="";
                String bendeOlan="";
                String kişi_2="";
                String kişi_2_itemName="";
                String aracı="";

                usercommentFromFB_1kişi.clear();
                useremailsFromFB_1kişi.clear();
                userimageFromFB_1kişi.clear();
                userdesiredFromFB_1kişi.clear();

                usercommentFromFB_2kişi.clear();
                useremailsFromFB_2kişi.clear();
                userimageFromFB_2kişi.clear();
                userdesiredFromFB_2kişi.clear();
                listView.removeAllViewsInLayout();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    Boolean b1=  !hashMap.get("useremail").equals(MainActivity.userEmail);
                    String itemname=hashMap.get("itemname").toLowerCase();

                    Boolean b2= itemname.contains(searchword.toString().toLowerCase());

                    if(hashMap.get("useremail").equals(MainActivity.userEmail)&& !bendeOlan.contains(hashMap.get("itemname"))){
                        bendeOlan+= hashMap.get("itemname")+"_";
                    }else if (b1&& b2 && !kişi_1_istenen.contains(hashMap.get("desiredthing")))
                    {
                            kişi_1_istenen+= hashMap.get("desiredthing")+"_";
                            kişi_1_mailler+=hashMap.get("useremail")+"_";
                    }

                }
             /*  System.out.println("1. kişinin istedikleri = "+kişi_1_istenen+"\n"
                        +" bende olanlar = " +bendeOlan +"\n"
                        +" 1.kişi mailler = "+ kişi_1_mailler);*/

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    Boolean b2= !kişi_1_mailler.toString().toLowerCase().contains(hashMap.get("useremail").toLowerCase());
                    Boolean b3  =kişi_1_istenen.toString().toLowerCase().contains(hashMap.get("itemname").toLowerCase());
                    Boolean b4=bendeOlan.toString().toLowerCase().contains(hashMap.get("desiredthing").toLowerCase());

                    if(b2 && b3&&b4)
                    {

                        usercommentFromFB_2kişi.add(hashMap.get("itemname"));
                        useremailsFromFB_2kişi.add(hashMap.get("useremail"));
                        userimageFromFB_2kişi.add(hashMap.get("downloadurl"));
                        userdesiredFromFB_2kişi.add(hashMap.get("desiredthing"));
                        kişi_2 += hashMap.get("useremail");
                        kişi_2_itemName+= hashMap.get("itemname");

                    }
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    if(kişi_1_mailler.toString().toLowerCase().contains(hashMap.get("useremail").toLowerCase())&&
                            kişi_2_itemName.toString().toLowerCase().contains(hashMap.get("desiredthing").toLowerCase())){

                        aracı+=hashMap.get("useremail");

                        usercommentFromFB_1kişi.add(hashMap.get("itemname"));
                        useremailsFromFB_1kişi.add(hashMap.get("useremail"));
                        userimageFromFB_1kişi.add(hashMap.get("downloadurl"));
                        userdesiredFromFB_1kişi.add(hashMap.get("desiredthing"));

                    }
                    adapter.notifyDataSetChanged();
                }
               // System.out.println("aracı   =="+ aracı);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
