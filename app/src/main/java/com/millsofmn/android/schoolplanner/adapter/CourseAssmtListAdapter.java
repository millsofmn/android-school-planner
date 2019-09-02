package com.millsofmn.android.schoolplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;

import java.util.ArrayList;
import java.util.List;

public class CourseAssmtListAdapter  extends RecyclerView.Adapter<CourseAssmtListAdapter.ViewHolder> {

    private List<Assessment> data = new ArrayList<>();

    private OnListener onListener;

    public CourseAssmtListAdapter(OnListener onListener) {
        this.onListener = onListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(linearLayout, onListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayout cardView = (LinearLayout)holder.view;

        Assessment mentor = data.get(position);

        TextView courseTitle = cardView.findViewById(R.id.tv_line_item);
        courseTitle.setText(mentor.getTitle() + " (" + mentor.getPerformanceType() + ")");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Assessment> newData){
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

    public Assessment getSelectedItem(int position){
        if(!data.isEmpty()){
            if(data.size() > 0){
                return data.get(position);
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewGroup view;
        public OnListener onListener;

        public ViewHolder(@NonNull ViewGroup view, OnListener onListener) {
            super(view);
            this.view = view;
            this.onListener = onListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListener.onCourseAssessmentClick(getAdapterPosition());
        }
    }

    public interface OnListener {
        void onCourseAssessmentClick(int position);
    }

    class DataDiffCallBack extends DiffUtil.Callback {
        private final List<Assessment> oldData, newData;

        public DataDiffCallBack(List<Assessment> oldData, List<Assessment> newData) {
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
