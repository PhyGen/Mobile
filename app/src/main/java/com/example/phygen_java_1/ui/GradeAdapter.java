package com.example.phygen_java_1.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phygen_java_1.R;
import com.example.phygen_java_1.model.Grade;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> gradeList;

    public GradeAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList;
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
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvGradeName;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGradeName = itemView.findViewById(R.id.tvGradeName);
        }
    }
}
