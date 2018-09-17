package com.example.jugjeetsingh.rippleanimation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class Main3Activity extends AppCompatActivity {
ImageButton imageButton;
EditText edt, edt1;
private static final int GALLERY_REQUEST= 1;
Button btn;
     private Uri imageuri = null;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        storageReference= FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        imageButton=findViewById(R.id.imageButton);
        edt=findViewById(R.id.editText);
        edt1=findViewById(R.id.editText2);
        btn=findViewById(R.id.button3);
progressBar= new ProgressDialog(this);

            imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();
            }


        });
    }

    private void startposting() {
        progressBar.setMessage("Blog Posting");
        progressBar.show();
       final String name= edt.getText().toString().trim();
        final String description=edt1.getText().toString().trim();
      if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) && imageuri !=null){

          StorageReference filepath=  storageReference.child("Blog Image").child(imageuri.getLastPathSegment());
filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

        progressBar.dismiss();

        String downloadUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
        DatabaseReference newPost = mDatabase.push();

        newPost.child("tittle").setValue(name);
        newPost.child("desc").setValue(description);
        newPost.child("image").setValue(downloadUrl.toString());

    }

});
      }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALLERY_REQUEST && resultCode== RESULT_OK){
             imageuri = data.getData();
            imageButton.setImageURI(imageuri);
        }

    }
}
