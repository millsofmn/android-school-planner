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
import com.millsofmn.android.schoolplanner.db.entity.Term;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.ViewHolder> {
    private static final DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");

    private List<Term> data = new ArrayList<>();

    private OnTermListener onTermListener;

    public TermListAdapter(OnTermListener onTermListener) {
        this.onTermListener = onTermListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term_details, parent, false);

        return new ViewHolder(cardView, onTermListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView termTitle = cardView.findViewById(R.id.tv_term_title);
        termTitle.setText(data.get(position).getTitle());

        TextView termDates = cardView.findViewById(R.id.tv_term_dates);

        String dateText = "";
        if (data.get(position).getStartDate() != null) {
            dateText = dateFormat.format(data.get(position).getStartDate());
        }

        if(data.get(position).getEndDate() != null){
            if(!dateText.isEmpty()){
                dateText +=  " to ";
            }
            dateText += dateFormat.format(data.get(position).getEndDate());
        }
        termDates.setText(dateText);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Term> newData){
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

    public Term getSelectedTerm(int position){
        if(!data.isEmpty()){
            if(data.size() > 0){
                return data.get(position);
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public OnTermListener onTermListener;

        public ViewHolder(@NonNull CardView cardView, OnTermListener onTermListener) {
            super(cardView);
            this.cardView = cardView;
            this.onTermListener = onTermListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTermListener.onTermClick(getAdapterPosition());
        }
    }

    public interface OnTermListener {
        void onTermClick(int position);
    }

    class DataDiffCallBack extends DiffUtil.Callback {
        private final List<Term> oldData, newData;

        public DataDiffCallBack(List<Term> oldData, List<Term> newData) {
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
