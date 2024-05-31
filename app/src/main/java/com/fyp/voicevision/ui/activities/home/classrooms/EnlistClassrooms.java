package com.fyp.voicevision.ui.activities.home.classrooms;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityEnlistClassroomsBinding;
import com.fyp.voicevision.helpers.adapters.AdapterClassroom;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnCourseItemClickListener;
import com.fyp.voicevision.helpers.keys.Lesson_Key;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.CourseItem;
import com.fyp.voicevision.helpers.models.User;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.levels.Levels;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnlistClassrooms extends AppCompatActivity implements OnCourseItemClickListener {

    private ActivityEnlistClassroomsBinding binding;
    private FirebaseUser firebaseUser;
    private AdapterClassroom adapter;
    private List<CourseItem> courseItemList;
    private User user;
    private boolean isError = false;
    private boolean isFetched = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        courseItemList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnlistClassroomsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();

        initRecyclerView();
        fetchMe();

        binding.ifvBackEnlistClassrooms.setOnClickListener(v -> finish());
        binding.mbQuizEnlistClassroom.setOnClickListener(v -> onQuizLevelClick());
    }

    private void fetchMe() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    fetchCourses();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(EnlistClassrooms.this, error.getMessage());
            }
        };
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).addValueEventListener(valueEventListener);
    }

    private void updateUI() {
        String text = "Level: " + SharedPreferenceUtils.getLevel(this);
        binding.mbQuizEnlistClassroom.setText(text);
    }

    private void initRecyclerView() {
        adapter = new AdapterClassroom(courseItemList, this);
        binding.rvFeaturesEnlistClassrooms.setAdapter(adapter);
    }

    private void fetchCourses() {
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
                showToast(EnlistClassrooms.this, error.getMessage());
            }
        };
        String level = SharedPreferenceUtils.getLevel(this);
        Query query = FirebaseUtils.dr_courses.orderByChild(Lesson_Key.LEVEL_TYPE).equalTo(level);
        query.addValueEventListener(valueEventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(@NonNull DataSnapshot snapshot) {
        isFetched = true;
        courseItemList.clear();
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            CourseItem courseItem = dataSnapshot.getValue(CourseItem.class);
            if (courseItem != null) {
                if (user.getCurrentCourse() == 0)
                    user.setCurrentCourse(1);
                courseItem.setLocked(courseItemList.size() > user.getCurrentCourse() - 1);
                courseItemList.add(courseItem);
            }
        }
        adapter.notifyDataSetChanged();
        binding.progressBarEnlistClassrooms.setVisibility(View.GONE);
        if (courseItemList.isEmpty())
            binding.mtvEmptyEnlistClassrooms.setVisibility(View.VISIBLE);
        else
            binding.mtvEmptyEnlistClassrooms.setVisibility(View.GONE);

        startSpeakingList();
    }

    private void startSpeakingList() {
        if (!isFetched) return;
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (courseItemList.isEmpty()) {
            TextSpeechUtils.textSpeech("No Data Found");
        } else {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To repeat, say repeat menu, ");

            for (int i = 0; i < courseItemList.size(); i++) {
                CourseItem courseItem = courseItemList.get(i);
                text.append("Speak Select ").append(i + 1).append(" for ").append(courseItem.getTitle()).append("  ");
            }
            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
        }
    }

    @Override
    public void onItemClick(CourseItem courseItem) {
        String text = "This course is locked. First clear previous one";
        if (courseItem.isLocked()) {
            if (VoiceManager.isVoiceMode(this))
                TextSpeechUtils.textSpeech(text, this::launchGoogleAssistant);
            else
                showToast(this, text);
            return;
        }
        Intent intent = new Intent(this, Classroom.class);
        intent.putExtra("course", courseItem);
        startActivity(intent);
    }

    private void onQuizLevelClick() {
        Intent intent = new Intent(this, Levels.class);
        intent.putExtra("isUpdate", true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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
        } else if (text.toLowerCase().contains("one") || text.toLowerCase().contains("1")) {
            if (0 < courseItemList.size()) {
                if(courseItemList.get(0).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(0));
            }
        } else if (text.toLowerCase().contains("two") || text.toLowerCase().contains("2")) {
            if (1 < courseItemList.size()) {
                if(courseItemList.get(1).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(1));
            }
        } else if (text.toLowerCase().contains("three") || text.toLowerCase().contains("3")) {
            if (2 < courseItemList.size()) {
                if(courseItemList.get(2).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(2));
            }
        } else if (text.toLowerCase().contains("four") || text.toLowerCase().contains("4")) {
            if (3 < courseItemList.size()) {
                if(courseItemList.get(3).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(3));
            }
        } else if (text.toLowerCase().contains("five") || text.toLowerCase().contains("5")) {
            if (4 < courseItemList.size()) {
                if(courseItemList.get(4).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(4));
            }
        } else if (text.toLowerCase().contains("six") || text.toLowerCase().contains("6")) {
            if (5 < courseItemList.size()) {
                if(courseItemList.get(5).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(5));
            }
        } else if (text.toLowerCase().contains("seven") || text.toLowerCase().contains("7")) {
            if (6 < courseItemList.size()) {
                if(courseItemList.get(6).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(6));
            }
        } else if (text.toLowerCase().contains("eight") || text.toLowerCase().contains("8")) {
            if (7 < courseItemList.size()) {
                if(courseItemList.get(7).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(7));
            }
        } else if (text.toLowerCase().contains("nine") || text.toLowerCase().contains("9")) {
            if (8 < courseItemList.size()) {
                if(courseItemList.get(8).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(8));
            }
        } else if (text.toLowerCase().contains("ten") || text.toLowerCase().contains("10")) {
            if (9 < courseItemList.size()) {
                if(courseItemList.get(9).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(9));
            }
        } else if (text.toLowerCase().contains("eleven") || text.toLowerCase().contains("11")) {
            if (10 < courseItemList.size()) {
                if(courseItemList.get(10).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(10));
            }
        } else if (text.toLowerCase().contains("twelve") || text.toLowerCase().contains("12")) {
            if (11 < courseItemList.size()) {
                if(courseItemList.get(11).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(11));
            }
        } else if (text.toLowerCase().contains("thirteen") || text.toLowerCase().contains("13")) {
            if (12 < courseItemList.size()) {
                if(courseItemList.get(12).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(12));
            }
        } else if (text.toLowerCase().contains("fourteen") || text.toLowerCase().contains("14")) {
            if (13 < courseItemList.size()) {
                if(courseItemList.get(13).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(13));
            }
        } else if (text.toLowerCase().contains("fifteen") || text.toLowerCase().contains("15")) {
            if (14 < courseItemList.size()) {
                if(courseItemList.get(14).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(14));
            }
        } else if (text.toLowerCase().contains("sixteen") || text.toLowerCase().contains("16")) {
            if (15 < courseItemList.size()) {
                if(courseItemList.get(15).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(15));
            }
        } else if (text.toLowerCase().contains("seventeen") || text.toLowerCase().contains("17")) {
            if (16 < courseItemList.size()) {
                if(courseItemList.get(16).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(16));
            }
        } else if (text.toLowerCase().contains("eighteen") || text.toLowerCase().contains("18")) {
            if (17 < courseItemList.size()) {
                if(courseItemList.get(17).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(17));
            }
        } else if (text.toLowerCase().contains("nineteen") || text.toLowerCase().contains("19")) {
            if (18 < courseItemList.size()) {
                if(courseItemList.get(18).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(18));
            }
        } else if (text.toLowerCase().contains("twenty") || text.toLowerCase().contains("20")) {
            if (19 < courseItemList.size()) {
                if(courseItemList.get(19).isLocked()) {
                    isError = true;
                }
                onItemClick(courseItemList.get(19));
            }
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
}