package com.example.phygen_java.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java.R;
import com.example.phygen_java.model.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Question question);
    }

    public QuestionAdapter(List<Question> list, OnItemClickListener listener) {
        this.questionList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.tvContent.setText(q.getContent());
        holder.tvLevel.setText("Độ khó: " + q.getDifficultyLevel());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(q);
        });
    }

    @Override
    public int getItemCount() {
        return questionList != null ? questionList.size() : 0;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvLevel;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }
    }
}