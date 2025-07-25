package com.example.phygen_java_2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.phygen_java_2.R;
import com.example.phygen_java_2.util.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.example.phygen_java_2.model.LoginResponse;

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
        int roleId = SharedPrefManager.getInstance(this).getRoleId();
        Log.d("HomeActivity", "Retrieved roleId: " + roleId);

        Menu menu = navigationView.getMenu();
        if (roleId == 1) {
            // USER: chỉ show nav_profile & nav_history
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                if (item.getItemId() != R.id.nav_profile && item.getItemId() != R.id.nav_history) {
                    item.setVisible(false);
                }
            }
        } else if (roleId == 2 || roleId == 3) {
            // ADMIN/MODERATOR: show hết trừ create_exam
            MenuItem createExamItem = menu.findItem(R.id.nav_create_exam);
            if (createExamItem != null) {
                createExamItem.setVisible(false);
            }
        } else {
            // roleId không hợp lệ
            Log.w("HomeActivity", "Invalid roleId: " + roleId + ", hiding all items");
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }



        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_create_exam) {
                Toast.makeText(HomeActivity.this, "Tính năng này chỉ hỗ trợ trên web", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(this, HistoryActivity.class));
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