package com.fyp.voicevision.helpers.firebase;

import android.app.Activity;
import android.content.Intent;

import com.fyp.voicevision.helpers.keys.CourseQuizItem_Key;
import com.fyp.voicevision.helpers.keys.Course_Key;
import com.fyp.voicevision.helpers.keys.GeneralQuizItem_Key;
import com.fyp.voicevision.helpers.keys.Lesson_Key;
import com.fyp.voicevision.helpers.keys.QuizResult_Key;
import com.fyp.voicevision.helpers.keys.Quiz_Topic_Key;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.ui.activities.splash.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static DatabaseReference dr_users = FirebaseDatabase.getInstance().getReference(User_Key.OBJECT);
    public static DatabaseReference dr_lessons = FirebaseDatabase.getInstance().getReference(Lesson_Key.OBJECT);
    public static DatabaseReference dr_courses = FirebaseDatabase.getInstance().getReference(Course_Key.OBJECT);
    public static DatabaseReference dr_course_quiz = FirebaseDatabase.getInstance().getReference(CourseQuizItem_Key.OBJECT);

    public static DatabaseReference dr_quiz_topics = FirebaseDatabase.getInstance().getReference(Quiz_Topic_Key.OBJECT);
    public static DatabaseReference dr_general_quiz = FirebaseDatabase.getInstance().getReference(GeneralQuizItem_Key.OBJECT);
    public static DatabaseReference dr_quiz_result = FirebaseDatabase.getInstance().getReference(QuizResult_Key.OBJECT);

    public static void logout(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}