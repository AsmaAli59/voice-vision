package com.fyp.voicevision.ui.activities.home.classrooms;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityClassroomBinding;
import com.fyp.voicevision.helpers.adapters.AdapterLesson;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnLessonItemClickListener;
import com.fyp.voicevision.helpers.keys.Lesson_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.CourseItem;
import com.fyp.voicevision.helpers.models.Lesson;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Classroom extends AppCompatActivity implements OnLessonItemClickListener {

    private ActivityClassroomBinding binding;

    private AdapterLesson adapterLesson;

    private CourseItem courseItem;
    private List<Lesson> lessonList;

    private boolean isError = false;
    private boolean isFetched = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            courseItem = getIntent().getSerializableExtra("course", CourseItem.class);
        } else {
            courseItem = (CourseItem) getIntent().getSerializableExtra("course");
        }
        lessonList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        initRecyclerView();
        fetchContent();

        binding.toolbarClassroom.setNavigationOnClickListener(v -> finish());
        binding.toolbarClassroom.setTitle(courseItem.getTitle());
        binding.mbQuizClassroom.setOnClickListener(v -> onQuizClick());
    }

    private void initRecyclerView() {
        adapterLesson = new AdapterLesson(lessonList, this);
        binding.rvFeaturesClassroom.setAdapter(adapterLesson);
    }

    private void fetchContent() {
        if (VoiceManager.isVoiceMode(this)) {
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_fetching_data));
        }
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fillList(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(Classroom.this, error.getMessage());
            }
        };
        Query query = FirebaseUtils.dr_lessons.orderByChild(Lesson_Key.CID).equalTo(courseItem.getCid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        isFetched = true;
        lessonList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Lesson lesson = dataSnapshot.getValue(Lesson.class);
            if (lesson != null) {
                lessonList.add(lesson);
            }
        }
        adapterLesson.notifyDataSetChanged();
        binding.progressBarClassroom.setVisibility(View.GONE);
        if (lessonList.isEmpty())
            binding.mtvEmptyClassroom.setVisibility(View.VISIBLE);
        else
            binding.mtvEmptyClassroom.setVisibility(View.GONE);

        startSpeakingList();
    }

    private void startSpeakingList() {
        if (!isFetched) return;
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (lessonList.isEmpty()) {
            TextSpeechUtils.textSpeech("No Data Found");
        } else {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To start quiz, say Start Quiz, ");
            text.append("To repeat, say repeat menu, ");


            for (int i = 0; i < lessonList.size(); i++) {
                Lesson lesson = lessonList.get(i);
                text.append("Speak Select ").append(i + 1).append(" for ").append(lesson.getTitle()).append("  ");
            }
            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
        }
    }

    private void onQuizClick() {
        Intent intent = new Intent(this, ClassroomQuiz.class);
        intent.putExtra("course", courseItem);
        startActivity(intent);
    }

    @Override
    public void onItemClick(Lesson lesson) {
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isError) {
            initVoiceMode();
            startSpeakingList();
        } else {
            isError = false;
        }
    }

    /* ------------------------------------------- Voice Mode ------------------------------------------- */

    private void initVoiceMode() {
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }
        binding.incVoice.getRoot().setVisibility(View.VISIBLE);

        binding.incVoice.getRoot().setOnClickListener(v -> launchGoogleAssistant());
        binding.incVoice.mbExitVoiceMode.setOnClickListener(v -> VoiceManager.exitVoiceMode(this));
    }

    private void launchGoogleAssistant() {
        TextSpeechUtils.stopSpeech();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter Command");
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
                ArrayList<String> resultList = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = resultList.get(0);
                boolean isNormalCommand = VoiceManager.checkDefaultCommand(this, text);
                if (isNormalCommand) {
                    navigateScreen(text);
                }
            }
        }
    });

    private void navigateScreen(String text) {
        if (text.toLowerCase().contains("repeat")) {
            initVoiceMode();
        } else if (text.toLowerCase().contains("back")) {
            finish();
        } else if (text.toLowerCase().contains("quiz")) {
            onQuizClick();
        } else if (text.toLowerCase().contains("one") || text.toLowerCase().contains("1")) {
            if (0 < lessonList.size()) {
                onItemClick(lessonList.get(0));
            }
        } else if (text.toLowerCase().contains("two") || text.toLowerCase().contains("2")) {
            if (1 < lessonList.size()) onItemClick(lessonList.get(1));
        } else if (text.toLowerCase().contains("three") || text.toLowerCase().contains("3")) {
            if (2 < lessonList.size()) onItemClick(lessonList.get(2));
        } else if (text.toLowerCase().contains("four") || text.toLowerCase().contains("4")) {
            if (3 < lessonList.size()) onItemClick(lessonList.get(3));
        } else if (text.toLowerCase().contains("five") || text.toLowerCase().contains("5")) {
            if (4 < lessonList.size()) onItemClick(lessonList.get(4));
        } else if (text.toLowerCase().contains("six") || text.toLowerCase().contains("6")) {
            if (5 < lessonList.size()) onItemClick(lessonList.get(5));
        } else if (text.toLowerCase().contains("seven") || text.toLowerCase().contains("7")) {
            if (6 < lessonList.size()) onItemClick(lessonList.get(6));
        } else if (text.toLowerCase().contains("eight") || text.toLowerCase().contains("8")) {
            if (7 < lessonList.size()) onItemClick(lessonList.get(7));
        } else if (text.toLowerCase().contains("nine") || text.toLowerCase().contains("9")) {
            if (8 < lessonList.size()) onItemClick(lessonList.get(8));
        } else if (text.toLowerCase().contains("ten") || text.toLowerCase().contains("10")) {
            if (9 < lessonList.size()) onItemClick(lessonList.get(9));
        } else if (text.toLowerCase().contains("eleven") || text.toLowerCase().contains("11")) {
            if (10 < lessonList.size()) onItemClick(lessonList.get(10));
        } else if (text.toLowerCase().contains("twelve") || text.toLowerCase().contains("12")) {
            if (11 < lessonList.size()) onItemClick(lessonList.get(11));
        } else if (text.toLowerCase().contains("thirteen") || text.toLowerCase().contains("13")) {
            if (12 < lessonList.size()) onItemClick(lessonList.get(12));
        } else if (text.toLowerCase().contains("fourteen") || text.toLowerCase().contains("14")) {
            if (13 < lessonList.size()) onItemClick(lessonList.get(13));
        } else if (text.toLowerCase().contains("fifteen") || text.toLowerCase().contains("15")) {
            if (14 < lessonList.size()) onItemClick(lessonList.get(14));
        } else if (text.toLowerCase().contains("sixteen") || text.toLowerCase().contains("16")) {
            if (15 < lessonList.size()) onItemClick(lessonList.get(15));
        } else if (text.toLowerCase().contains("seventeen") || text.toLowerCase().contains("17")) {
            if (16 < lessonList.size()) onItemClick(lessonList.get(16));
        } else if (text.toLowerCase().contains("eighteen") || text.toLowerCase().contains("18")) {
            if (17 < lessonList.size()) onItemClick(lessonList.get(17));
        } else if (text.toLowerCase().contains("nineteen") || text.toLowerCase().contains("19")) {
            if (18 < lessonList.size()) onItemClick(lessonList.get(18));
        } else if (text.toLowerCase().contains("twenty") || text.toLowerCase().contains("20")) {
            if (19 < lessonList.size()) onItemClick(lessonList.get(19));
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
}