package com.millsofmn.android.schoolplanner.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mentor")
public class Mentor implements Parcelable, MyEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email_address")
    private String emailAddress;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    public Mentor() {
    }

    @Ignore
    public Mentor(@NonNull String name, String emailAddress, String phoneNumber) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    @Ignore
    public Mentor(int id, @NonNull String name, String emailAddress, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    protected Mentor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        emailAddress = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<Mentor> CREATOR = new Creator<Mentor>() {
        @Override
        public Mentor createFromParcel(Parcel in) {
            return new Mentor(in);
        }

        @Override
        public Mentor[] newArray(int size) {
            return new Mentor[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Mentor id(int id) {
        this.id = id;
        return this;
    }

    public Mentor name(@NonNull String name) {
        this.name = name;
        return this;
    }

    public Mentor emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Mentor phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(emailAddress);
        parcel.writeString(phoneNumber);
    }
}
