package com.abhishek.myliber;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class FirstProfileActivity extends AppCompatActivity {
    private TextView tvChange;
    private CircleImageView ivperson;
    static final int pickimage=1;
    EditText edtemailfirst,edtnamefirst;
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
        setContentView(R.layout.activity_first_profile);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        tvChange=findViewById(R.id.tvchangefirst);
        ivperson=findViewById(R.id.ivpersonfirst);
        edtemailfirst=findViewById(R.id.edtemailfirst);
        edtnamefirst=findViewById(R.id.edtnamefirst);
        btnSave=findViewById(R.id.btnSaveprofilefirst);

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
                if(TextUtils.isEmpty(edtemailfirst.getText().toString())||TextUtils.isEmpty(edtnamefirst.getText().toString())){
                    Toast.makeText(FirstProfileActivity.this, "Both the fields must not be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        writeToRealmDatabase(edtemailfirst.getText().toString().trim(), edtnamefirst.getText().toString().trim());
                        Toast.makeText(FirstProfileActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(FirstProfileActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void writeToRealmDatabase(String email, String name) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Customer customer = bgRealm.createObject(Customer.class);
                customer.setName(name);
                customer.setEmail(email);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(FirstProfileActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                Log.v("database","Data Inserted");
                startActivity(new Intent(FirstProfileActivity.this, MainActivity.class));
                finish();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Toast.makeText(FirstProfileActivity.this, "Error Inserting Data", Toast.LENGTH_SHORT).show();
                Log.e("database",error.getMessage());
            }
        });
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
    public void onBackPressed()
    {
        Toast.makeText(this, "Unfortunately, you can't go back unless you fill and your the details. ", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}