package com.millsofmn.android.schoolplanner.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "course_mentor",
        indices = {
                @Index(value = "course_id"),
                @Index(value = "mentor_id", unique = true)},
        primaryKeys = {"course_id", "mentor_id"},
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "id",
                        childColumns = "course_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Mentor.class,
                        parentColumns = "id",
                        childColumns = "mentor_id",
                        onDelete = ForeignKey.CASCADE)})
public class CourseMentor implements Parcelable {

    @ColumnInfo(name = "course_id")
    public int courseId;

    @ColumnInfo(name = "mentor_id")
    public int mentorId;

    public CourseMentor() {
    }

    public CourseMentor(int courseId, int mentorId) {
        this.courseId = courseId;
        this.mentorId = mentorId;
    }

    protected CourseMentor(Parcel in) {
        courseId = in.readInt();
        mentorId = in.readInt();
    }

    public static final Creator<CourseMentor> CREATOR = new Creator<CourseMentor>() {
        @Override
        public CourseMentor createFromParcel(Parcel in) {
            return new CourseMentor(in);
        }

        @Override
        public CourseMentor[] newArray(int size) {
            return new CourseMentor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(courseId);
        parcel.writeInt(mentorId);
    }

    public CourseMentor courseId(int courseId) {
        this.courseId = courseId;
        return this;
    }

    public CourseMentor mentorId(int mentorId) {
        this.mentorId = mentorId;
        return this;
    }
}
