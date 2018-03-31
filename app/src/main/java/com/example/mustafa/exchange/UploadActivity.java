package com.example.mustafa.exchange;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.BitSet;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    int duration=100;
    EditText itemName;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    Uri selected;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        itemName=findViewById(R.id.itemname);
        imageView=findViewById(R.id.imageView);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();//veritabanına bağlandı
        mAuth=FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();

    }

    @SuppressLint("WrongConstant")
    public void upload(View view){

        Toast.makeText(getApplicationContext(),"lütfen bekleyiniz," +
                "yükleniyor.",duration).show();


        UUID uuıdImage=UUID.randomUUID();
        String imageName="images/"+uuıdImage+".jpg";//her resime ayrı bir unique isim verdik karışmaması için.

        StorageReference storageReference=mStorageRef.child(imageName);//firebasedeki storage images klasörünün içine ulaştık
        storageReference.putFile(selected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadURL=taskSnapshot.getDownloadUrl().toString();//resmin urlsine ulaşıcaz
                FirebaseUser user=mAuth.getCurrentUser();
                String userEmail=user.getEmail().toString();
                String userComment =itemName.getText().toString();
                UUID uuıd=UUID.randomUUID();
                String uuidString=uuıd.toString();

                myRef.child("Posts").child(uuidString).child("useremail").setValue(userEmail);
                myRef.child("Posts").child(uuidString).child("itemname").setValue(userComment);
                myRef.child("Posts").child(uuidString).child("downloadurl").setValue(downloadURL);
                Toast.makeText(getApplicationContext(),"Item Shared",Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(),FeedActivity.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void chooseImage(View view){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){//izin alınmadıysa izin alırız.
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);//izin almak için gereken kısım.
        }else{
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //eğer kullanıcı izin verdiyse galeriye bağlan.
                startActivityForResult(intent,2);
           }
      }
      @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//eğer izin yoksa izin aldıktan sonra ne yapılacağını belirler.
       if(requestCode==1){
           if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
               Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(intent,2);
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

          if(requestCode==2&&resultCode==RESULT_OK&&data!=null){
             selected=data.getData();

             try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected);
                imageView.setImageBitmap(bitmap);

             } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
