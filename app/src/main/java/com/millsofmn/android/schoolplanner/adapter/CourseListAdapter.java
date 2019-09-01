package com.millsofmn.android.schoolplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Course;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    private List<Course> data = new ArrayList<>();

    private OnCourseListener onCourseListener;

    public CourseListAdapter(OnCourseListener onCourseListener) {
        this.onCourseListener = onCourseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_details, parent, false);

        return new ViewHolder(cardView, onCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView courseTitle = cardView.findViewById(R.id.et_course_title);
        courseTitle.setText(data.get(position).getTitle());

        TextView courseDates = cardView.findViewById(R.id.tv_course_dates);
        if(data.get(position).getStartDate() != null){
            courseDates.setText(dateFormat.format(data.get(position).getStartDate()) + " to " + dateFormat.format(data.get(position).getEndDate()));
        }

        TextView courseStatus = cardView.findViewById(R.id.sp_course_status);
        courseStatus.setText(data.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Course> newData){
        if(data != null) {
            DataDiffCallBack dataDiffCallBack = new DataDiffCallBack(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallBack);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            data = newData;
        }
    }

    public Course getSelectedCourse(int position){
        if(!data.isEmpty()){
            if(data.size() > 0){
                return data.get(position);
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public OnCourseListener onCourseListener;

        public ViewHolder(@NonNull CardView cardView, OnCourseListener onCourseListener) {
            super(cardView);
            this.cardView = cardView;
            this.onCourseListener = onCourseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCourseListener.onCourseClick(getAdapterPosition());
        }
    }

    public interface OnCourseListener {
        void onCourseClick(int position);
    }

    class DataDiffCallBack extends DiffUtil.Callback {
        private final List<Course> oldData, newData;

        public DataDiffCallBack(List<Course> oldData, List<Course> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }

        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).getId() == newData.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }
    }
}
