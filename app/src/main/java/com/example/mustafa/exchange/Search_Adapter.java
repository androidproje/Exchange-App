package com.example.mustafa.exchange;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Search_Adapter extends ArrayAdapter<String> {

    private final ArrayList<String> useremail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final ArrayList<String> userdesired;
    ImageView send;
    static String sendEmail;
    static String kiminyolla;
    static  String yenidenyolla;

    private final Activity context;


    public Search_Adapter(ArrayList<String> useremail, ArrayList<String> userImage, ArrayList<String> userComment, ArrayList<String> userdesired, Activity context) {
        super(context, R.layout.search_item_list,useremail);
        this.useremail = useremail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.userdesired = userdesired;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.search_item_list,null,true);

        TextView useremailText=(TextView) customView.findViewById(R.id.username);
        TextView commentText=(TextView) customView.findViewById(R.id.commentText);
        ImageView imageView =(ImageView) customView.findViewById(R.id.imageView2);
        final TextView desiredthing=(TextView) customView.findViewById(R.id.desiredthing);
        send=customView.findViewById(R.id.notification);

        useremailText.setText(useremail.get(position));
        commentText.setText(userComment.get(position));
        desiredthing.setText(userdesired.get(position));

        kiminyolla=MainActivity.userEmail;

        // Picasso.with(context).load(userImage.get(position)).into(imageView);
        Picasso.get()
                .load(userImage.get(position))
                .resize(150, 150)
                .centerCrop()
                .into(imageView);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(),"Position ="+position,Toast.LENGTH_LONG).show();
                sendEmail=useremail.get(position);
                System.out.println("myitemss   "+ fragmentMyitems.MyItems);
                System.out.println("user item "+ userdesired.get(position));
                if(fragmentMyitems.MyItems.contains(userdesired.get(position))) {
                    sendNotification(sendEmail);

                }else{
                    Toast.makeText(getContext(),"İstenen iteme sahip değilsiniz : "+userdesired.get(position),Toast.LENGTH_LONG).show();
                }
            }
        });
        return customView;
    }



    private void sendNotification(final String send_Email)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic N2NhOWY4NWEtODE1Yi00ZWFmLTg1YWMtMmE3YmFhZWRlMDg5");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"f3813aa0-546a-4555-aa56-cf23bc652836\","
                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_Email + "\"}],"

                                + "\"headings\": {\"en\":\""+kiminyolla+" \"},"
                                + "\"contents\": {\"en\":\" Sizinle değişim yapmak istiyor\"},"
                                + "\"buttons\":  [{\"id\": \"id1\", \"text\": \"Onayla\", \"icon\": \"\"}, {\"id\": \"id2\", \"text\": \"İptal\", \"icon\": \"\"}]"
                                + "}";



                     //   System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                      //  System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                      //  System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}




