package com.example.phygen_java_2.network;

import com.example.phygen_java_2.model.Chapter;
import com.example.phygen_java_2.model.Exam;
import com.example.phygen_java_2.model.ExamHistory;
import com.example.phygen_java_2.model.GenericResponse;
import com.example.phygen_java_2.model.GoogleLoginRequest;
import com.example.phygen_java_2.model.Grade;
import com.example.phygen_java_2.model.Lesson;
import com.example.phygen_java_2.model.LessonListResponse;
import com.example.phygen_java_2.model.LoginRequest;
import com.example.phygen_java_2.model.LoginResponse;
import com.example.phygen_java_2.model.Question;
import com.example.phygen_java_2.model.QuestionResponse;
import com.example.phygen_java_2.model.RegisterRequest;
import com.example.phygen_java_2.model.Semester;
import com.example.phygen_java_2.model.Solution;
import com.example.phygen_java_2.model.UpdateProfileRequest;
import com.example.phygen_java_2.model.User;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;

public interface ApiService {
    @POST("api/signup")
    Call<GenericResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/login/google-login")
    Call<LoginResponse> googleLogin(@Body GoogleLoginRequest googleLoginRequest);

    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") int id, @Header("Authorization") String token);

    @GET("api/user/{id}")
    Call<User> getCurrentUser(@Header("Authorization") String token);

    @POST("api/exams/questions")
    Call<GenericResponse> assignQuestionToExam(@Body JsonObject body);

    @GET("api/chapters")
    Call<List<Chapter>> getChapters(@Header("Authorization") String token);

    @POST("api/chapters")
    Call<Chapter> createChapter(@Body Chapter chapter, @Header("Authorization") String token);
    @DELETE("api/chapters/{id}")
    Call<GenericResponse> deleteChapter(@Path("id") int id, @Header("Authorization") String token);

    @PUT("api/chapters/{id}")
    Call<GenericResponse> updateChapter(@Path("id") int id, @Body Chapter chapter, @Header("Authorization") String token);
    @GET("api/exams")
    Call<List<Exam>> getAllExams();

    @POST("api/exams")
    Call<GenericResponse> createExam(@Body Exam exam);

    @GET("api/exams/user/{userId}")
    Call<List<Exam>> getMyExams(@Path("userId") int userId);

    @GET("api/exams/recent")
    Call<List<Exam>> getRecentExams();

    @GET("api/questions")
    Call<QuestionResponse> getAllQuestions(@Header("Authorization") String token);

//    @GET("api/questions")
//    Call<QuestionResponse> getAllQuestions(
//            @Header("Authorization") String token,
//            @Query("lessonId") Integer lessonId
//    );

    @POST("api/questions")
    Call<Question> createQuestion(@Body Question question, @Header("Authorization") String token);

    @DELETE("api/questions/{id}")
    Call<Void> deleteQuestion(@Path("id") int id, @Header("Authorization") String token);

    @GET("api/questions/{id}")
    Call<Question> getQuestionById(@Path("id") int id);

    @GET("api/semesters")
    Call<List<Semester>> getAllSemesters();

    @GET("api/grades")
    Call<List<Grade>> getGrades();

    @POST("api/solutions")
    Call<Void> createSolution(@Body Solution solution);

    @GET("api/solutions")
    Call<List<Solution>> getSolutions();
    @GET("api/lessons")
    Call<LessonListResponse> getLessons(@Header("Authorization") String token);

    @GET("api/lessons/{id}")
    Call<Lesson> getLessonById(@Path("id") int id, @Header("Authorization") String token);

    @POST("api/lessons")
    Call<Lesson> createLesson(@Body Lesson lesson, @Header("Authorization") String token);

    @PUT("api/lessons/{id}")
    Call<Lesson> updateLesson(@Path("id") int id, @Body Lesson lesson, @Header("Authorization") String token);

    @DELETE("api/lessons/{id}")
    Call<Void> deleteLesson(@Path("id") int id, @Header("Authorization") String token);
    @GET("api/chapters/{userId}")
    Call<List<Chapter>> getChaptersByUserId(@Path("userId") int userId, @Header("Authorization") String token);

    @PUT("api/users/{id}/profile")
    Call<Void> updateUserProfile(
            @Header("Authorization") String token,
            @Path("id") int userId,
            @Body UpdateProfileRequest request
    );

    @GET("api/users/{id}")
    Call<User> getUserById(
            @Header("Authorization") String token,
            @Path("id") int userId
    );
    @GET("exams/user/{userId}")
    Call<List<ExamHistory>> getExamHistory(@Header("Authorization") String token, @Path("userId") String userId);
}

