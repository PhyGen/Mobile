package com.example.phygen_java.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phygen_java.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnRecent).setOnClickListener(v ->
                startActivity(new Intent(this, RecentExamActivity.class)));
        findViewById(R.id.btnViewProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        findViewById(R.id.btnViewProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        findViewById(R.id.btnViewExams).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExamActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnViewQuestions).setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            startActivity(intent);
        });
        Button btnViewChapters = findViewById(R.id.btnViewChapters);
        btnViewChapters.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChapterActivity.class);
            startActivity(intent);
        });
        Button btnViewSemesters = findViewById(R.id.btnViewSemesters);
        btnViewSemesters.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SemesterActivity.class);
            startActivity(intent);
        });
        Button btnViewGrades = findViewById(R.id.btnViewGrades);
        btnViewGrades.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GradeActivity.class);
            startActivity(intent);
        });
        MaterialButton btnViewSolutions = findViewById(R.id.btnViewSolutions);
        btnViewSolutions.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SolutionActivity.class);
            startActivity(intent);
        });

    }
}
