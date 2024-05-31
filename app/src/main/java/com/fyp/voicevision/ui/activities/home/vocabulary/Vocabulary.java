package com.fyp.voicevision.ui.activities.home.vocabulary;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityVocabularyBinding;
import com.fyp.voicevision.helpers.adapters.AdapterEachVocabulary;
import com.fyp.voicevision.helpers.dataProviders.DpVocabulary;
import com.fyp.voicevision.helpers.interfaces.OnVocabularyEachItemClickListener;
import com.fyp.voicevision.helpers.manager.VoiceManager;
import com.fyp.voicevision.helpers.models.VocabularyEachItem;
import com.fyp.voicevision.helpers.models.VocabularyItem;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.roomDb.models.FavItem;
import com.fyp.voicevision.roomDb.viewModel.RoomViewModel;
import com.fyp.voicevision.ui.activities.home.Home;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressLint("NotifyDataSetChanged")
public class Vocabulary extends AppCompatActivity implements OnVocabularyEachItemClickListener {

    private ActivityVocabularyBinding binding;
    private RoomViewModel roomViewModel;

    private AdapterEachVocabulary adapter;
    private DpVocabulary dataProvider;
    private VocabularyItem vocabularyItem;
    private List<FavItem> favItemList;
    private List<VocabularyEachItem> vocabularyItemList;
    private boolean isError = false;

    private void initializations() {
        TextSpeechUtils.stopSpeech();
        dataProvider = new DpVocabulary();
        favItemList = new ArrayList<>();
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        vocabularyItem = (VocabularyItem) getIntent().getSerializableExtra("vocabularyItem");
        vocabularyItemList = dataProvider.getEachVocabularyList(vocabularyItem.getVid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVocabularyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();
        initObservers();
        initRecyclerView();

        binding.toolbarVocabulary.setNavigationOnClickListener(v -> finish());
    }

    private void initObservers() {
        roomViewModel.getAllFav().observe(this, list -> {
            favItemList.clear();
            favItemList.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    private void initRecyclerView() {
        if (vocabularyItem != null) {
            adapter = new AdapterEachVocabulary(favItemList, dataProvider.getEachVocabularyList(vocabularyItem.getVid()), this);
            binding.rvListVocabulary.setAdapter(adapter);
        } else {
            showToast(this, "Exception Found");
        }
    }

    @Override
    public void onItemClick(VocabularyEachItem vocabularyEachItem) {
        String text = vocabularyEachItem.getTitleEnglish() + " " + vocabularyEachItem.getTitleUrdu();
        TextSpeechUtils.textSpeech(text, this::startSpeakingList);
    }

    @Override
    public void onFavClick(VocabularyEachItem vocabularyEachItem) {
        FavItem favItem = new FavItem(0, vocabularyEachItem.getVid(), vocabularyEachItem.getId());
        roomViewModel.insert(favItem);
        adapter.notifyDataSetChanged();
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


    private void startSpeakingList() {
        if (!VoiceManager.isVoiceMode(this)) {
            binding.incVoice.getRoot().setVisibility(View.GONE);
            return;
        }

        TextSpeechUtils.stopSpeech();
        if (vocabularyItemList.isEmpty()) {
            TextSpeechUtils.textSpeech("No Data Found");
        } else {
            StringBuilder text = new StringBuilder();

            text.append("To go back app, say Go Back, ");
            text.append("To exit voice mode, say Exit Voice Mode, ");
            text.append("To go on home screen, say Home Screen, ");
            text.append("To repeat, say repeat sentences, ");
            text.append("To bookmark, say bookmark number one and so, ");

            for (int i = 0; i < vocabularyItemList.size(); i++) {
                VocabularyEachItem item = vocabularyItemList.get(i);
                text.append(item.getTitleEnglish()).append("  ");
                //text.append("Speak Select ").append(i + 1).append(" for ").append(item.getTitleEnglish()).append("  ");
            }
            TextSpeechUtils.textSpeech(text.toString(), this::launchGoogleAssistant);
        }
    }

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
        } else if (text.toLowerCase().contains("home")) {
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (text.toLowerCase().contains("one") || text.toLowerCase().contains("1")) {
            if (0 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(0));
        } else if (text.toLowerCase().contains("two") || text.toLowerCase().contains("2")) {
            if (1 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(1));
        } else if (text.toLowerCase().contains("three") || text.toLowerCase().contains("3")) {
            if (2 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(2));
        } else if (text.toLowerCase().contains("four") || text.toLowerCase().contains("4")) {
            if (3 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(3));
        } else if (text.toLowerCase().contains("five") || text.toLowerCase().contains("5")) {
            if (4 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(4));
        } else if (text.toLowerCase().contains("six") || text.toLowerCase().contains("6")) {
            if (5 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(5));
        } else if (text.toLowerCase().contains("seven") || text.toLowerCase().contains("7")) {
            if (6 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(6));
        } else if (text.toLowerCase().contains("eight") || text.toLowerCase().contains("8")) {
            if (7 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(7));
        } else if (text.toLowerCase().contains("nine") || text.toLowerCase().contains("9")) {
            if (8 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(8));
        } else if (text.toLowerCase().contains("ten") || text.toLowerCase().contains("10")) {
            if (9 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(9));
        } else if (text.toLowerCase().contains("eleven") || text.toLowerCase().contains("11")) {
            if (10 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(10));
        } else if (text.toLowerCase().contains("twelve") || text.toLowerCase().contains("12")) {
            if (11 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(11));
        } else if (text.toLowerCase().contains("thirteen") || text.toLowerCase().contains("13")) {
            if (12 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(12));
        } else if (text.toLowerCase().contains("fourteen") || text.toLowerCase().contains("14")) {
            if (13 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(13));
        } else if (text.toLowerCase().contains("fifteen") || text.toLowerCase().contains("15")) {
            if (14 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(14));
        } else if (text.toLowerCase().contains("sixteen") || text.toLowerCase().contains("16")) {
            if (15 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(15));
        } else if (text.toLowerCase().contains("seventeen") || text.toLowerCase().contains("17")) {
            if (16 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(16));
        } else if (text.toLowerCase().contains("eighteen") || text.toLowerCase().contains("18")) {
            if (17 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(17));
        } else if (text.toLowerCase().contains("nineteen") || text.toLowerCase().contains("19")) {
            if (18 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(18));
        } else if (text.toLowerCase().contains("twenty") || text.toLowerCase().contains("20")) {
            if (19 < vocabularyItemList.size()) onFavClick(vocabularyItemList.get(19));
        } else {
            isError = true;
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_failed), this::launchGoogleAssistant);
        }
    }
    @Override
    protected void onDestroy() {
        TextSpeechUtils.stopSpeech();
        super.onDestroy();
    }

}