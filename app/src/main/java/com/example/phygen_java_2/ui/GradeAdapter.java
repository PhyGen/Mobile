package com.example.phygen_java_2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.Grade;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> gradeList;
    private OnGradeClickListener listener;

    public interface OnGradeClickListener {
        void onGradeClick(Grade grade);
    }

    public GradeAdapter(List<Grade> gradeList, OnGradeClickListener listener) {
        this.gradeList = gradeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        holder.tvGradeName.setText("Khối: " + grade.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGradeClick(grade);
        });
    }

    @Override
    public int getItemCount() {
        return gradeList != null ? gradeList.size() : 0;
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvGradeName;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGradeName = itemView.findViewById(R.id.tvGradeName);
        }
    }
}