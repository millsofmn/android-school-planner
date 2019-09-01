package com.millsofmn.android.schoolplanner.ui.course;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.millsofmn.android.schoolplanner.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseEditFragment extends Fragment {
    public static final String TAG = "Edit Course++++++";
    private static final SimpleDateFormat fmtDate = new SimpleDateFormat("E, MMM d, yyyy");
    private static final SimpleDateFormat fmtTime = new SimpleDateFormat("h:mm a", Locale.getDefault());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int courseId;
    private int termId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText etCourseTitle;
    private Spinner spCourseStatus;
    private Button btnCourseStartDate;
    private Button btnCourseStartTime;
    private CheckBox cbCourseAlertOnStart;
    private Button btnCourseEndDate;
    private Button btnCourseEndTime;
    private CheckBox cbCourseAlertOnEnd;

    private Button lastButtonClicked;

    public CourseEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseEditFragment newInstance() {
//    public static CourseEditFragment newInstance(String param1, String param2) {
        CourseEditFragment fragment = new CourseEditFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CourseEditFragment newInstance(int courseId, int termId){
        CourseEditFragment CourseEditFragment = new CourseEditFragment();
        Bundle args = new Bundle();
        args.putInt(CourseActivity.COURSE_ID_EXTRA, courseId);
        args.putInt(CourseListActivity.TERM_ID_EXTRA, termId);

        CourseEditFragment.setArguments(args);

        return CourseEditFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_edit, container, false);


        courseId = getArguments().getInt(CourseActivity.COURSE_ID_EXTRA, -1);
        termId = getArguments().getInt(CourseListActivity.TERM_ID_EXTRA, -1);
        Log.i(TAG, "CourseId=" + courseId + ", TermId=" + termId);

        etCourseTitle = view.findViewById(R.id.et_course_title);
        spCourseStatus = view.findViewById(R.id.sp_course_status);

        btnCourseStartDate = view.findViewById(R.id.btn_course_start_date);
        btnCourseStartDate.setOnClickListener(v -> showDatePickerDialog(btnCourseStartDate));

        btnCourseStartTime = view.findViewById(R.id.btn_course_start_time);
        btnCourseStartTime.setOnClickListener(v -> showTimePickerDialog(btnCourseStartTime));

        cbCourseAlertOnStart = view.findViewById(R.id.cb_course_alert_start);
        btnCourseEndDate = view.findViewById(R.id.btn_course_end_date);
        btnCourseEndDate.setOnClickListener(v -> showDatePickerDialog(btnCourseEndDate));

        btnCourseEndTime = view.findViewById(R.id.btn_course_end_time);
        btnCourseEndTime.setOnClickListener(v -> showTimePickerDialog(btnCourseEndTime));

        cbCourseAlertOnEnd = view.findViewById(R.id.cb_course_alert_end);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showDatePickerDialog(Button lastButtonClicked) {
        this.lastButtonClicked = lastButtonClicked;
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                getActivity(),
                setDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private final DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
        final Calendar calendar = Calendar.getInstance();
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            lastButtonClicked.setText(fmtDate.format(calendar.getTime()));
        }
    };

    private void showTimePickerDialog(Button lastButtonClicked){
        this.lastButtonClicked = lastButtonClicked;
        final Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(
                    getActivity(),
                    setTime,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false).show();
    }

    private final TimePickerDialog.OnTimeSetListener setTime = new TimePickerDialog.OnTimeSetListener() {
        final Calendar calendar = Calendar.getInstance();
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            lastButtonClicked.setText(fmtTime.format(calendar.getTime()));
        }
    };

}
