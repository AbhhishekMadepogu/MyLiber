package com.abhishek.myliber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvChange;
    private CircleImageView ivperson;
    static final int pickimage=1;
    EditText edtemail,edtname;
    Uri imageuri;
    Button btnSave;
    public static SqliteHelper sqliteHelper;
    final  int reqcode=999;
    Bitmap bits;
    String one="1";
    int update=0;
    int checker=0;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        tvChange=findViewById(R.id.tvchange);
        ivperson=findViewById(R.id.ivperson);
        edtemail=findViewById(R.id.edtemail);
        edtname=findViewById(R.id.edtname);
        btnSave=findViewById(R.id.btnSaveprofile);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileActivity.this, "Intent", Toast.LENGTH_SHORT).show();
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery,"Select Profile Picture"),pickimage);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(ProfileActivity.this, "Save", Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(edtname.getText().toString())){
                    Toast.makeText(ProfileActivity.this, "Both the fields must not be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        updateRealmDatabase(edtemail.getText().toString().trim(), edtname.getText().toString().trim());
                        Toast.makeText(ProfileActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Error Occured Profile Activity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        updateUI();
    }

    private void updateRealmDatabase(String email, String name) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute (Realm realm) {
                Customer customer = realm.where(Customer.class).equalTo("email", email).findFirst();
                if(customer == null) {
                    Toast.makeText(ProfileActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                customer.setName(name);
            }
        });
    }
    private void updateUI() {
        RealmResults<Customer> result = realm.where(Customer.class)
                .findAll();
        edtname.setText(result.first().getName());
        edtemail.setText(result.first().getEmailId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private byte[] imagetoByte(ImageView ivperson) {
        Bitmap bitmap=((BitmapDrawable)ivperson.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.update_info:
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1){
            Toast.makeText(this, "Hii", Toast.LENGTH_SHORT).show();
            imageuri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                ivperson.setImageBitmap(bitmap);
                bits=bitmap;
                checker=1;

                //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                System.out.println("not working");
            }


        }
        else {
            Toast.makeText(this, "Notwork", Toast.LENGTH_SHORT).show();
        }
    }
}