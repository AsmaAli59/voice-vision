package com.fyp.voicevision.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.FragmentHomeBinding;
import com.fyp.voicevision.helpers.adapters.AdapterHome;
import com.fyp.voicevision.helpers.dataProviders.DpHome;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.HomeItem;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.bookmarks.Bookmark;
import com.fyp.voicevision.ui.activities.home.Home;
import com.fyp.voicevision.ui.activities.home.classrooms.EnlistClassrooms;
import com.fyp.voicevision.ui.activities.home.quiz.EnlistQuiz;
import com.fyp.voicevision.ui.activities.home.vocabulary.EnlistVocabulary;
import com.fyp.voicevision.ui.activities.levels.Levels;
import com.fyp.voicevision.ui.activities.levels.QuizLevels;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentHome extends Fragment implements OnHomeItemClickListener {

    private FragmentHomeBinding binding;
    private DpHome dataProvider;
    private boolean isError = false;

    private void initializations() {
        dataProvider = new DpHome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializations();
        initRecyclerView();
    }

    private void initRecyclerView() {
        AdapterHome adapter = new AdapterHome(dataProvider.getHomeFeatureList(), this);
        binding.rvFeaturesHome.setAdapter(adapter);
    }

    @Override
    public void onItemClick(HomeItem homeItem) {
        Intent intent = null;
        switch (homeItem.getId()) {
            case 0: {
                if (SharedPreferenceUtils.getLevel(getContext()).equals(LevelType.none.toString()))
                    intent = new Intent(getContext(), Levels.class);
                else {
                    intent = new Intent(getContext(), EnlistClassrooms.class);
                }
                break;
            }
            case 1: {
                intent = new Intent(getContext(), EnlistVocabulary.class);
                break;
            }
            case 2: {
                if (SharedPreferenceUtils.getQuizLevel(getContext()).equals(LevelType.none.toString()))
                    intent = new Intent(getContext(), QuizLevels.class);
                else {
                    intent = new Intent(getContext(), EnlistQuiz.class);
                }
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isError) {
            initVoiceMode();
        } else {
            isError = false;
        }
    }

    /* ------------------------------------------- Voice Mode ------------------------------------------- */

    private void initVoiceMode() {
        if (!VoiceManager.isVoiceMode(getContext())) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }
        binding.incVoice.getRoot().setVisibility(View.VISIBLE);

        TextSpeechUtils.textSpeech(getString(R.string.voice_mode_home), this::launchGoogleAssistant);

        binding.incVoice.getRoot().setOnClickListener(v -> launchGoogleAssistant());
        binding.incVoice.mbExitVoiceMode.setOnClickListener(v -> VoiceManager.exitVoiceMode(getActivity()));
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
                boolean isNormalCommand = VoiceManager.checkDefaultCommand(getActivity(), text);
                if (isNormalCommand) {
                    navigateScreen(text);
                }
            }
        }
    });

    private void navigateScreen(String text) {
        if (text.toLowerCase().contains("repeat")) {
            initVoiceMode();
        } else if (text.toLowerCase().contains("level")) {
            onLevelClick();
        } else if (text.toLowerCase().contains("bookmark")) {
            onBookmarkClick();
        } else if (text.toLowerCase().contains("mode")) {
            ((Home) getActivity()).loadFragment(new FragmentMode());
        } else if (text.toLowerCase().contains("logout")) {
            FirebaseUtils.logout(getActivity());
        } else if (text.toLowerCase().contains("profile")) {
            ((Home) getActivity()).loadFragment(new FragmentProfile());
        } else if (text.toLowerCase().contains("classroom")) {
            onItemClick(dataProvider.getHomeFeatureList().get(0));
        } else if (text.toLowerCase().contains("phrase")) {
            onItemClick(dataProvider.getHomeFeatureList().get(1));
        } else if (text.toLowerCase().contains("quiz")) {
            onItemClick(dataProvider.getHomeFeatureList().get(2));
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }

    private void onLevelClick() {
        Intent intent = new Intent(getContext(), Levels.class);
        intent.putExtra("isUpdate", true);
        startActivity(intent);
    }

    private void onBookmarkClick() {
        startActivity(new Intent(getContext(), Bookmark.class));
    }

    @Override
    public void onDestroy() {
        TextSpeechUtils.stopSpeech();
        super.onDestroy();
    }
}