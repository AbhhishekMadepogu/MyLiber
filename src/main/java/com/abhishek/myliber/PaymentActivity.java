package com.abhishek.myliber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmResults;

public class PaymentActivity extends AppCompatActivity {
    EditText edtCardnumber,edtCVV;
    Button btnsave;
    String strCardnumber,strCVV;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        edtCardnumber=findViewById(R.id.edtCredit);
        edtCVV=findViewById(R.id.edtCVV);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Payment");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        strCardnumber=edtCardnumber.getText().toString();
        strCVV=edtCVV.getText().toString();
        btnsave=findViewById(R.id.btnSavepayment);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtCardnumber.getText().toString())||TextUtils.isEmpty(edtCVV.getText().toString())){

                }
                else{
                    updateOrAddCreditCard(edtCardnumber.getText().toString(), edtCVV.getText().toString());
                    updateUI();
                }
            }
        });
        updateUI();
    }
        private void updateUI(){
            RealmResults<Customer> result = realm.where(Customer.class)
                    .findAll();
            edtCardnumber.setText(result.first().getCardNumber());
            edtCVV.setText(result.first().getCvv());
        }
        private String getCustomerEmail(){
            RealmResults<Customer> result = realm.where(Customer.class)
                    .findAll();
            return result.first().getEmailId();
        }

    private void updateOrAddCreditCard(String strCardnumber, String strCVV) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute (Realm realm) {
                Customer customer = realm.where(Customer.class).equalTo("email", getCustomerEmail()).findFirst();
                Log.v("customer",""+customer+"cardnumber"+strCardnumber);
                if(customer == null) {
                    Toast.makeText(PaymentActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                customer.setCardNumber(strCardnumber);
                customer.setCvv(strCVV);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}