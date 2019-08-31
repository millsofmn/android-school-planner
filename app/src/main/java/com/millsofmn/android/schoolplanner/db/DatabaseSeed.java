package com.millsofmn.android.schoolplanner.db;


import com.millsofmn.android.schoolplanner.db.entity.Assessment;
import com.millsofmn.android.schoolplanner.db.entity.Course;
import com.millsofmn.android.schoolplanner.db.entity.CourseMentor;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;
import com.millsofmn.android.schoolplanner.db.entity.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseSeed {


    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, diff);
        return cal.getTime();
    }

    //public Term(int id, String title, Date startDate, Date endDate) {
    public static List<Term> getTerms(){
        List<Term> terms = new ArrayList<>();
        terms.add(new Term(1, "Term 1", getDate(-6), getDate(-3)));
        terms.add(new Term(2, "Term 2", getDate(-3), getDate(0)));
        terms.add(new Term(3, "Term 3", getDate(0), getDate(3)));
        terms.add(new Term(4, "Term 4", getDate(3), getDate(6)));

        return terms;
    }

    public static List<Course> getCourses(){
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1, 1,"Intro to Computers", "COMPLETED", false, getDate(-6), false, getDate(-5), "Intro was a good cours"));
        courses.add(new Course(2, 1,"More About Computers", "COMPLETED", false, getDate(-5), false, getDate(-4), "Intro was a hard cours"));
        courses.add(new Course(3, 1,"Being Human", "COMPLETED", false, getDate(-4), false, getDate(-3), "More about comp"));
        courses.add(new Course(4, 2,"Being Alien", "DROPPED", false, getDate(-3), false, getDate(-2), "Being human"));
        courses.add(new Course(5, 2,"Biology", "COMPLETED", false, getDate(-2), false, getDate(-1), "Being Alien"));
        courses.add(new Course(6, 2,"Dunking 101", "IN_PROGRESS", false, getDate(-1), false, getDate(0), "Almost alien"));
        courses.add(new Course(7, 3,"Android Development", "PLAN_TO_TAKE", false, getDate(0), false, getDate(1), "Keeping it alien"));
        courses.add(new Course(8, 3,"Drinking", "PLAN_TO_TAKE", false, getDate(1), false, getDate(2), "101 with donuts"));
        courses.add(new Course(9, 3,"How to Pick Things", "PLAN_TO_TAKE", false, getDate(2), false, getDate(3), "android"));
        courses.add(new Course(10, 4,"Intro to Course Names", "PLAN_TO_TAKE", false, getDate(3), false, getDate(4), "Drinking"));
        courses.add(new Course(11, 4,"Advance Computers", "PLAN_TO_TAKE", false, getDate(4), false, getDate(5), "Hangover"));
        courses.add(new Course(12, 4,"Clapping on One and Three", "PLAN_TO_TAKE", false, getDate(5), false, getDate(6), "pick things"));

        return courses;
    }

    public static List<Assessment> getAssessments(){
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment(1, 1, "Assessment", "OBJECTIVE", getDate(-5), false));
        assessments.add(new Assessment(16, 1, "Report", "OBJECTIVE", getDate(-5), false));
        assessments.add(new Assessment(2, 2, "Assessment", "OBJECTIVE", getDate(-4), false));
        assessments.add(new Assessment(3, 3, "Assessment", "PERFORMANCE", getDate(-3), false));
        assessments.add(new Assessment(4, 4, "Assessment", "PERFORMANCE", getDate(-2), false));
        assessments.add(new Assessment(5, 5, "Assessment", "OBJECTIVE", getDate(-1), false));
        assessments.add(new Assessment(6, 6, "Assessment", "PERFORMANCE", getDate(0), false));
        assessments.add(new Assessment(7, 7, "Assessment", "OBJECTIVE", getDate(1), false));
        assessments.add(new Assessment(8, 7, "Assessment", "PERFORMANCE", null, false));
        assessments.add(new Assessment(9, 8, "Assessment", "OBJECTIVE", null, false));
        assessments.add(new Assessment(10, 9, "Assessment", "PERFORMANCE", getDate(3), true));
        assessments.add(new Assessment(11, 10, "Assessment", "OBJECTIVE", getDate(4), true));
        assessments.add(new Assessment(12, 11, "Assessment", "OBJECTIVE", null, false));
        assessments.add(new Assessment(13, 12, "Assessment", "OBJECTIVE", getDate(6), false));
        assessments.add(new Assessment(14, 12, "Assessment", "PERFORMANCE", null, false));
        assessments.add(new Assessment(15, 12, "Assessment", "PERFORMANCE", getDate(6), true));

        return assessments;
    }

    public static List<Mentor> getMentors(){
        List<Mentor> mentors = new ArrayList<>();
        mentors.add(new Mentor(1, "Sally June","june.sally@exmple.com","507-555-1234"));
        mentors.add(new Mentor(2, "Sven Updohal","updahal.sven@exmple.com","507-555-4567"));
        mentors.add(new Mentor(3, "James Smith","smith.jamesy@exmple.com","507-555-7899"));
        mentors.add(new Mentor(4, "Saul Paul","paul.saul@exmple.com","507-555-7534"));
        mentors.add(new Mentor(5, "Negan Gull","bible@exmple.com","507-555-1599"));

        return mentors;
    }

    public static List<CourseMentor> getCourseMentors(){
        List<CourseMentor> courseMentors = new ArrayList<>();
        courseMentors.add(new CourseMentor(1, 1));
        courseMentors.add(new CourseMentor(2, 2));
        courseMentors.add(new CourseMentor(3, 3));
        courseMentors.add(new CourseMentor(4, 4));
        courseMentors.add(new CourseMentor(5, 5));
        courseMentors.add(new CourseMentor(6, 2));
        courseMentors.add(new CourseMentor(7, 3));
        courseMentors.add(new CourseMentor(8, 4));
        courseMentors.add(new CourseMentor(9, 5));
        courseMentors.add(new CourseMentor(10, 2));
        courseMentors.add(new CourseMentor(11, 3));
        courseMentors.add(new CourseMentor(12, 1));
        courseMentors.add(new CourseMentor(12, 2));
        courseMentors.add(new CourseMentor(12, 3));
        courseMentors.add(new CourseMentor(12, 4));
        courseMentors.add(new CourseMentor(12, 5));

        return courseMentors;
    }
}
