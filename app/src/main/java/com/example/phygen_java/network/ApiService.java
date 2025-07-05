package com.example.phygen_java.network;

import com.example.phygen_java.model.Chapter;
import com.example.phygen_java.model.Exam;
import com.example.phygen_java.model.GenericResponse;
import com.example.phygen_java.model.Grade;
import com.example.phygen_java.model.LoginRequest;
import com.example.phygen_java.model.LoginResponse;
import com.example.phygen_java.model.Question;
import com.example.phygen_java.model.RegisterRequest;
import com.example.phygen_java.model.Profile;
import com.example.phygen_java.model.Semester;
import com.example.phygen_java.model.Solution;
import com.example.phygen_java.model.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Header;


public interface ApiService {
    @POST("signup")
    Call<GenericResponse> register(@Body RegisterRequest registerRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("account/{id}")
    Call<Profile> getAccount(@Path("id") int id, @Header("Authorization") String token);

    @GET("api/user/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @GET("chapters")
    Call<List<Chapter>> getChapters();

    @GET("api/exams")
    Call<List<Exam>> getAllExams();

    @POST("api/exams")
    Call<GenericResponse> createExam(@Body Exam exam);

    @GET("api/exams/user/{userId}")
    Call<List<Exam>> getMyExams(@Path("userId") int userId);

    @GET("api/exams/recent")
    Call<List<Exam>> getRecentExams();

    @GET("api/questions")
    Call<List<Question>> getAllQuestions();

    @POST("/api/questions")
    Call<Void> createQuestion(@Body Question question);

    @GET("api/questions/{id}")
    Call<Question> getQuestionById(@Path("id") int id);

    @GET("/api/semesters")
    Call<List<Semester>> getAllSemesters();

    @GET("/api/grades")
    Call<List<Grade>> getGrades();

    @POST("/api/solutions")
    Call<Void> createSolution(@Body Solution solution);

    @GET("solutions")
    Call<List<Solution>> getSolutions();

}

