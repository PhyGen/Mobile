package com.example.phygen_java.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.phygen_java.R;
import com.example.phygen_java.model.Solution;
import java.util.List;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.SolutionViewHolder> {
    private List<Solution> solutionList;

    public SolutionAdapter(List<Solution> solutions) {
        this.solutionList = solutions;
    }

    @NonNull
    @Override
    public SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solution, parent, false);
        return new SolutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionViewHolder holder, int position) {
        Solution solution = solutionList.get(position);
        holder.tvContent.setText("Lời giải: " + solution.getContent());
        holder.tvExplanation.setText("Giải thích: " + solution.getExplanation());
    }

    @Override
    public int getItemCount() {
        return solutionList.size();
    }

    static class SolutionViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvExplanation;

        public SolutionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
        }
    }
}
