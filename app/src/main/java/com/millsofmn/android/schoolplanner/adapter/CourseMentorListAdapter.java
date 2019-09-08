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
import com.millsofmn.android.schoolplanner.db.entity.Mentor;

import java.util.ArrayList;
import java.util.List;

public class CourseMentorListAdapter extends RecyclerView.Adapter<CourseMentorListAdapter.ViewHolder> {

    private List<Mentor> data = new ArrayList<>();

    private OnListener onListener;

    public CourseMentorListAdapter(CourseMentorListAdapter.OnListener onListener) {
        this.onListener = onListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_mentor, parent, false);

        return new ViewHolder(linearLayout, onListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = (CardView) holder.view;

        Mentor mentor = (Mentor)data.get(position);

        TextView tvMentorName = cardView.findViewById(R.id.tv_line_1);
        tvMentorName.setText(mentor.getName());

        TextView tvEmail = cardView.findViewById(R.id.tv_line_2);
        tvEmail.setText("E: " + mentor.getEmailAddress());

        TextView tvPhone = cardView.findViewById(R.id.tv_line_3);
        tvPhone.setText("P: " + mentor.getPhoneNumber());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Mentor> newData){
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

    public Mentor getSelectedItem(int position){
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
            onListener.onCourseMentorClick(getAdapterPosition());
        }
    }

    public interface OnListener {
        void onCourseMentorClick(int position);
    }

    class DataDiffCallBack extends DiffUtil.Callback {
        private final List<Mentor> oldData, newData;

        public DataDiffCallBack(List<Mentor> oldData, List<Mentor> newData) {
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
