package com.fyp.voicevision.ui.fragments;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.FragmentModeBinding;
import com.fyp.voicevision.helpers.adapters.AdapterModes;
import com.fyp.voicevision.helpers.dataProviders.DpModes;
import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.interfaces.OnHomeItemClickListener;
import com.fyp.voicevision.helpers.keys.User_Key;
import com.fyp.voicevision.helpers.models.HomeItem;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.TextSpeechUtils;
import com.fyp.voicevision.ui.activities.home.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class FragmentMode extends Fragment implements OnHomeItemClickListener {

    private FragmentModeBinding binding;
    private DpModes dataProvider;
    private ProgressDialog progressDialog;

    private void initializations() {
        dataProvider = new DpModes();
        progressDialog = DialogUtils.buildProgressDialog(getContext(), "Please Wait!", "Processing...", false);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentModeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializations();
        initRecyclerView();
    }

    private void initRecyclerView() {
        AdapterModes adapter = new AdapterModes(dataProvider.getModeList(), this);
        binding.rvFeaturesModes.setAdapter(adapter);
    }

    @Override
    public void onItemClick(HomeItem homeItem) {
        progressDialog.show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            showToast(getContext(), "Something went Wrong!");
            FirebaseUtils.logout(getActivity());
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (homeItem.getId() == 0)
            hashMap.put(User_Key.MODE_TYPE, ModeType.touch);
        else
            hashMap.put(User_Key.MODE_TYPE, ModeType.voice);
        SharedPreferenceUtils.setMode(getContext(), String.valueOf(hashMap.get(User_Key.MODE_TYPE)));
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateScreen();
            } else {
                showToast(getContext(), String.valueOf(task.getException()));
            }
        });
    }

    private void navigateScreen() {
        progressDialog.dismiss();
        if (SharedPreferenceUtils.getMode(getContext()).equals(ModeType.voice.toString())) {
            TextSpeechUtils.textSpeech(getString(R.string.voice_mode_start), this::launchScreen);
        }
    }

    private void launchScreen() {
        Home home = (Home) getActivity();
        if (home != null) {
            home.loadFragment(new FragmentHome());
        }
    }
}