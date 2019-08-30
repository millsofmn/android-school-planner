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

public class MentorListAdapter extends RecyclerView.Adapter<MentorListAdapter.ViewHolder> {
    private List<Mentor> data = new ArrayList<>();

    private OnMentorListener onMentorListener;

    public MentorListAdapter(OnMentorListener onMentorListener) {
        this.onMentorListener = onMentorListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mentor_details, parent, false);

        return new ViewHolder(cardView, onMentorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView mentorName = cardView.findViewById(R.id.tv_mentor_name);
        mentorName.setText(data.get(position).getName());

        TextView phoneNumbers = cardView.findViewById(R.id.tv_mentor_phone);
        phoneNumbers.setText(data.get(position).getPhoneNumber());

        TextView emailAddresses = cardView.findViewById(R.id.tv_mentor_emails);
        emailAddresses.setText(data.get(position).getEmailAddress());

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

    public Mentor getSelectedMentor(int position){
        if(!data.isEmpty()){
            if(data.size() > 0){
                return data.get(position);
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public OnMentorListener onMentorListener;

        public ViewHolder(@NonNull CardView cardView, OnMentorListener onMentorListener) {
            super(cardView);
            this.cardView = cardView;
            this.onMentorListener = onMentorListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMentorListener.onMentorClick(getAdapterPosition());
        }
    }

    public int getId(int position){
        return data.get(position).getId();
    }

    public interface OnMentorListener {
        void onMentorClick(int position);
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
