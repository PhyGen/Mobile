package com.example.phygen_java_2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.ExamHistory;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<ExamHistory> historyList;

    public HistoryAdapter(List<ExamHistory> historyList) {
        this.historyList = historyList != null ? new ArrayList<>(historyList) : new ArrayList<>();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if (historyList != null && position >= 0 && position < historyList.size()) {
            ExamHistory history = historyList.get(position);
            holder.tvUsername.setText("User: " + (history.getUsername() != null ? history.getUsername() : "Unknown"));
            holder.tvCreatedAt.setText("Thời gian: " + (history.getCreatedAt() != null ? history.getCreatedAt() : "N/A"));
            if (history.getFormat() != null) {
                holder.tvFormatType.setText("Loại: " + (history.getFormat().getType() != null ? history.getFormat().getType() : "N/A"));
                holder.tvFormatQuestions.setText("Số câu: " + history.getFormat().getQuestions()); // int, không cần null check
                holder.tvFormatImage.setText("Có hình ảnh: " + (history.getFormat().isHasImage() ? "Có" : "Không")); // boolean, không cần null check
            } else {
                holder.tvFormatType.setText("Loại: N/A");
                holder.tvFormatQuestions.setText("Số câu: 0");
                holder.tvFormatImage.setText("Có hình ảnh: N/A");
            }
        }
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public void updateData(List<ExamHistory> data) {
        if (data == null) {
            historyList = new ArrayList<>();
        } else {
            historyList.clear();
            historyList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvCreatedAt, tvFormatType, tvFormatQuestions, tvFormatImage;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvFormatType = itemView.findViewById(R.id.tvFormatType);
            tvFormatQuestions = itemView.findViewById(R.id.tvFormatQuestions);
            tvFormatImage = itemView.findViewById(R.id.tvFormatImage);
        }
    }
}