package com.millsofmn.android.schoolplanner.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.millsofmn.android.schoolplanner.db.entity.MyEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericListAdapter<T extends MyEntity> extends RecyclerView.Adapter<GenericListAdapter.ViewHolder> {

    protected List<T> data = new ArrayList<>();

    protected OnListener onListener;

    public GenericListAdapter(OnListener onListener) {
        this.onListener = onListener;
    }

    abstract public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    abstract public void onBindViewHolder(@NonNull ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> newData){
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

    public T getSelectedItem(int position){
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
            onListener.onClick(getAdapterPosition());
        }
    }

    public interface OnListener {
        void onClick(int position);
    }

    class DataDiffCallBack extends DiffUtil.Callback {
        private final List<T> oldData, newData;

        public DataDiffCallBack(List<T> oldData, List<T> newData) {
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