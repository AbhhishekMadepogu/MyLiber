package com.abhishek.myliber;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


public class Card extends RealmObject {
    @PrimaryKey int id;
    String cardNumber,cvv;

    public Card(int id, String cardNumber, String cvv) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    public Card() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

}
