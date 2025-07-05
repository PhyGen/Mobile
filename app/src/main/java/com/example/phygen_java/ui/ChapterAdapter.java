package com.example.phygen_java.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phygen_java.R;
import com.example.phygen_java.model.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<Chapter> chapterList;

    public ChapterAdapter(List<Chapter> chapters) {
        this.chapterList = chapters;
        Log.d("ChapterAdapter", "Adapter được tạo với " + chapters.size() + " chương.");
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.tvChapterName.setText(chapter.getName());
        holder.tvSemester.setText("Học kỳ: " + chapter.getSemesterId());

        // 🔍 Log để kiểm tra dữ liệu từng item
        Log.d("ChapterAdapter", "Đang bind chapter: " + chapter.getName());
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterName, tvSemester;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tvChapterName);
            tvSemester = itemView.findViewById(R.id.tvSemester);
        }
    }
}
