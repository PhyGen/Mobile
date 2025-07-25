package com.example.phygen_java_2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phygen_java_2.R;
import com.example.phygen_java_2.model.ExamHistory;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<ExamHistory> historyList;

    public HistoryAdapter(List<ExamHistory> historyList) {
        this.historyList = historyList;
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
        ExamHistory history = historyList.get(position);
        holder.tvUsername.setText("User: " + history.getUsername());
        holder.tvCreatedAt.setText("Thời gian: " + history.getCreatedAt());
        holder.tvFormatType.setText("Loại: " + history.getFormat().getType());
        holder.tvFormatQuestions.setText("Số câu: " + history.getFormat().getQuestions());
        holder.tvFormatImage.setText("Có hình ảnh: " + (history.getFormat().isHasImage() ? "Có" : "Không"));
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
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