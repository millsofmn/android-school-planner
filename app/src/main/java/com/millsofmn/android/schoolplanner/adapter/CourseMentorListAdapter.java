package com.millsofmn.android.schoolplanner.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.millsofmn.android.schoolplanner.R;
import com.millsofmn.android.schoolplanner.db.entity.Mentor;

public class CourseMentorListAdapter extends GenericListAdapter<Mentor> {

    public CourseMentorListAdapter(OnListener onListener) {
        super(onListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(linearLayout, onListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayout cardView = (LinearLayout)holder.view;

        Mentor mentor = (Mentor)data.get(position);

        TextView courseTitle = cardView.findViewById(R.id.tv_line_item);
        courseTitle.setText(mentor.getName());
    }
}
