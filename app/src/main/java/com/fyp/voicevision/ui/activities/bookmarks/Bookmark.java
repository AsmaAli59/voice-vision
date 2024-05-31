package com.fyp.voicevision.ui.activities.bookmarks;

import static com.fyp.voicevision.helpers.utils.TextSpeechUtils.textSpeech;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityBookmarkBinding;
import com.fyp.voicevision.helpers.adapters.AdapterBookmark;
import com.fyp.voicevision.helpers.dataProviders.DpVocabulary;
import com.fyp.voicevision.helpers.interfaces.OnBookmarkItemClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.VocabularyEachItem;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.roomDb.models.FavItem;
import com.fyp.voicevision.roomDb.viewModel.RoomViewModel;
import com.fyp.voicevision.ui.activities.home.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Bookmark extends AppCompatActivity implements OnBookmarkItemClickListener {

    private ActivityBookmarkBinding binding;

    private AdapterBookmark adapterBookmark;
    private RoomViewModel roomViewModel;
    private DpVocabulary dpVocabulary;
    private List<FavItem> favItems;

    private boolean isError = false;

    private void initializations() {
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        dpVocabulary = new DpVocabulary();
        favItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        initRecyclerView();
        initObservers();

        binding.toolbarBookmark.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        adapterBookmark = new AdapterBookmark(favItems, this);
        binding.rvListBookmark.setAdapter(adapterBookmark);
    }

    private void initObservers() {
        roomViewModel.getAllFav().observe(this, this::fillList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fillList(List<FavItem> list) {
        favItems.clear();
        favItems.addAll(list);
        adapterBookmark.notifyDataSetChanged();
        binding.progressBarBookmark.setVisibility(View.GONE);
        if (favItems.isEmpty())
            binding.mtvEmptyBookmark.setVisibility(View.VISIBLE);
        else
            binding.mtvEmptyBookmark.setVisibility(View.GONE);
        initVoiceMode();
    }

    @Override
    public void onItemClick(FavItem favItem) {
        VocabularyEachItem vocabularyEachItem = dpVocabulary.getEachVocabularyList(favItem.getVocabularyId()).get(favItem.getVocabularyEachId());
        textSpeech(vocabularyEachItem.getTitleEnglish());
    }

    @Override
    public void onDeleteClick(FavItem favItem) {
        roomViewModel.delete(favItem);
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
        Log.d("Magic", "initVoiceMode: called");
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }
        Log.d("Magic", "initVoiceMode: stopping");

        TextSpeechUtils.stopSpeech();
        if (!favItems.isEmpty()) {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To repeat, say repeat menu, ");
            text.append("Your bookmark items are:");

            for (int i = 0; i < favItems.size(); i++) {
                FavItem favItem = favItems.get(i);
                VocabularyEachItem vocabularyEachItem = dpVocabulary.getEachVocabularyList(favItem.getVocabularyId()).get(favItem.getVocabularyEachId());
                text.append(vocabularyEachItem.getTitleEnglish()).append("  ");
            }
            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
        }
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
        } else if (text.toLowerCase().contains("home")) {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }

    @Override
    public void onDestroy() {
        TextSpeechUtils.stopSpeech();
        super.onDestroy();
    }
}