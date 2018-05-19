package com.example.mustafa.exchange;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Search_Adapter extends ArrayAdapter<String> {

    private final ArrayList<String> useremail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final ArrayList<String> userdesired;

    private final Activity context;


    public Search_Adapter(ArrayList<String> useremail, ArrayList<String> userImage, ArrayList<String> userComment, ArrayList<String> userdesired, Activity context) {
        super(context, R.layout.custom_view,useremail);
        this.useremail = useremail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.userdesired = userdesired;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.search_item_list,null,true);

        TextView useremailText=(TextView) customView.findViewById(R.id.username);
        TextView commentText=(TextView) customView.findViewById(R.id.commentText);
        ImageView imageView =(ImageView) customView.findViewById(R.id.imageView2);
        TextView desiredthing=(TextView) customView.findViewById(R.id.desiredthing);

        useremailText.setText(useremail.get(position));
        commentText.setText(userComment.get(position));
        desiredthing.setText(userdesired.get(position));

        // Picasso.with(context).load(userImage.get(position)).into(imageView);
        Picasso.get()
                .load(userImage.get(position))
                .resize(150, 150)
                .centerCrop()
                .into(imageView);
        return customView;
    }
}