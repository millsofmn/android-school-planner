package com.millsofmn.android.schoolplanner.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "assessment",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "course_id",
                onDelete = ForeignKey.CASCADE))
public class Assessment implements Parcelable, MyEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "course_id")
    private int courseId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "performance_type")
    private String performanceType;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @ColumnInfo(name = "alert")
    private boolean alertOnDueDate;

    public Assessment() {
    }


    @Ignore
    public Assessment(int courseId, @NonNull String title, @NonNull String performanceType, Date dueDate, boolean alertOnDueDate) {
        this.courseId = courseId;
        this.title = title;
        this.performanceType = performanceType;
        this.dueDate = dueDate;
        this.alertOnDueDate = alertOnDueDate;
    }

    @Ignore
    public Assessment(int id, int courseId, @NonNull String title, @NonNull String performanceType, Date dueDate, boolean alertOnDueDate) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.performanceType = performanceType;
        this.dueDate = dueDate;
        this.alertOnDueDate = alertOnDueDate;
    }

    protected Assessment(Parcel in) {
        id = in.readInt();
        courseId = in.readInt();
        title = in.readString();
        performanceType = in.readString();
        alertOnDueDate = in.readByte() != 0;
        if (in.readByte() == 0) {
            dueDate = null;
        } else {
            dueDate = new Date(in.readLong());
        }
    }

    public static final Creator<Assessment> CREATOR = new Creator<Assessment>() {
        @Override
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        @Override
        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(courseId);
        parcel.writeString(title);
        parcel.writeString(performanceType);
        parcel.writeByte((byte) (alertOnDueDate ? 1 : 0));
        if (dueDate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(dueDate.getTime());
        }
    }

    public Assessment id(int id) {
        this.id = id;
        return this;
    }

    public Assessment courseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public Assessment title(@NonNull String title) {
        this.title = title;
        return this;
    }

    public Assessment performanceType(@NonNull String performanceType) {
        this.performanceType = performanceType;
        return this;
    }

    public Assessment dueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public Assessment alertOnDueDate(boolean alertOnDueDate) {
        this.alertOnDueDate = alertOnDueDate;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getPerformanceType() {
        return performanceType;
    }

    public void setPerformanceType(@NonNull String performanceType) {
        this.performanceType = performanceType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isAlertOnDueDate() {
        return alertOnDueDate;
    }

    public void setAlertOnDueDate(boolean alertOnDueDate) {
        this.alertOnDueDate = alertOnDueDate;
    }
}
