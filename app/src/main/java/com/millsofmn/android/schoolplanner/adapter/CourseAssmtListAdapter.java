package com.millsofmn.android.schoolplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Assessment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseAssmtListAdapter  extends RecyclerView.Adapter<CourseAssmtListAdapter.ViewHolder> {
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

    private List<Assessment> data = new ArrayList<>();

    private OnListener onListener;

    public CourseAssmtListAdapter(OnListener onListener) {
        this.onListener = onListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assmt_details, parent, false);

        return new ViewHolder(cardView, onListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = (CardView)holder.view;

        TextView tvTitle = cardView.findViewById(R.id.tv_ass_title);
        tvTitle.setText(data.get(position).getTitle());

        TextView tvPerformanceType = cardView.findViewById(R.id.tv_ass_performance_type);
        tvPerformanceType.setText(data.get(position).getPerformanceType());


        TextView courseDates = cardView.findViewById(R.id.tv_ass_due_date);
        TextView alert = cardView.findViewById(R.id.tv_ass_alert);

        if(data.get(position).getDueDate() != null){
            courseDates.setText(dateFormat.format(data.get(position).getDueDate()));

            if(data.get(position).isAlertOnDueDate()){
                alert.setText("Alert On");
            } else {
                alert.setText("Alert Off");
            }

        } else {
            courseDates.setVisibility(View.GONE);
            alert.setVisibility(View.GONE);
        }
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
