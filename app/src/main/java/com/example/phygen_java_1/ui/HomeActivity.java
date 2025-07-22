package com.example.phygen_java_1.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phygen_java_1.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_create_exam) {
                startActivity(new Intent(this, CreateExamActivity.class));
            } else if (id == R.id.nav_my_exam) {
                startActivity(new Intent(this, MyExamActivity.class));
            } else if (id == R.id.nav_recent) {
                startActivity(new Intent(this, RecentExamActivity.class));
            } else if (id == R.id.nav_create_question) {
                startActivity(new Intent(this, CreateQuestionActivity.class));
            } else if (id == R.id.nav_chapter) {
                startActivity(new Intent(this, ChapterActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (id == R.id.nav_exam_list) {
                startActivity(new Intent(this, ExamActivity.class));
            } else if (id == R.id.nav_question) {
                startActivity(new Intent(this, QuestionActivity.class));
            } else if (id == R.id.nav_semester) {
                startActivity(new Intent(this, SemesterActivity.class));
            } else if (id == R.id.nav_grade) {
                startActivity(new Intent(this, GradeActivity.class));
            } else if (id == R.id.nav_solution) {
                startActivity(new Intent(this, SolutionActivity.class));
            } else if (id == R.id.nav_lesson) {
                startActivity(new Intent(this, LessonActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
