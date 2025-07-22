package com.example.phygen_java.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java.R;
import com.example.phygen_java.model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private static final String TAG = "LessonAdapter";

    private final List<Lesson> lessonList;
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessonList, OnLessonClickListener listener) {
        this.lessonList = lessonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.tvLessonName.setText(lesson.getName());
        holder.tvLessonId.setText("ID: " + (lesson.getId() > 0 ? lesson.getId() : "Invalid"));
        holder.tvChapterId.setText("Chapter ID: " + lesson.getChapterId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && lesson != null && lesson.getId() > 0) {
                Log.d(TAG, "Click lesson with ID: " + lesson.getId() + ", Name: " + lesson.getName());
                listener.onLessonClick(lesson);
            } else {
                Log.e(TAG, "Invalid lesson data - ID: " + (lesson != null ? lesson.getId() : "null"));
                Toast.makeText(holder.itemView.getContext(), "Lỗi: Dữ liệu bài học không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonName, tvLessonId, tvChapterId;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.tvLessonName); 
            tvLessonId = itemView.findViewById(R.id.tvLessonId);     
            tvChapterId = itemView.findViewById(R.id.tvChapterId);   
        }
    }
}