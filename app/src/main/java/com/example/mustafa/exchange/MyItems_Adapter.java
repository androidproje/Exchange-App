package com.example.mustafa.exchange;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyItems_Adapter extends ArrayAdapter<String> {

    private final ArrayList<String> useremail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final Activity context;
    DatabaseReference myRef;
    static ImageView delete;
    static int positionofitem;
    ImageView imageView;

    public MyItems_Adapter(ArrayList<String> useremail, ArrayList<String> userImage, ArrayList<String> userComment, Activity context,DatabaseReference myRef) {
        super(context, R.layout.myitems_list,useremail);
        this.useremail = useremail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.context = context;
        this.myRef=myRef;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.myitems_list,null,true);

        TextView useremailText=(TextView) customView.findViewById(R.id.username);
        TextView commentText=(TextView) customView.findViewById(R.id.commentText);
       imageView =(ImageView) customView.findViewById(R.id.imageView2);

        useremailText.setText(useremail.get(position));
        commentText.setText(userComment.get(position));

         delete=(ImageView) customView.findViewById(R.id.delete);
         positionofitem=position;

        // Picasso.with(context).load(userImage.get(position)).into(imageView);
        Picasso.get()
                .load(userImage.get(position))
                .resize(150, 150)
                .centerCrop()
                .into(imageView);




             delete(delete,position);


        return customView;
    }

    public void delete(ImageView delete, final int positions){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Remove Item");

                alertDialogBuilder
                        .setMessage("Do you want to delete this item?")
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher_round)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"Item is removed",Toast.LENGTH_LONG).show();
                                myRef=FirebaseDatabase.getInstance().getReference().child("Posts").child(fragmentMyitems.uuidofItems.get(positions));
                                myRef.removeValue();

                                fragmentMyitems.uuidofItems.remove(positions);

                                fragmentMyitems.adapter.remove(fragmentMyitems.adapter.getItem(positions));
                                fragmentMyitems.gridView.invalidateViews();
                                System.out.println("basılan position = "+ positions);
                                fragmentMyitems.adapter.notifyDataSetChanged();


                                Intent intent = new Intent(context, FeedActivity.class);
                                context.startActivity(intent);



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getContext(),"Did not remove item",Toast.LENGTH_SHORT).show();
                                System.out.println("basılan position = "+ positions);

                                dialog.dismiss();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

}