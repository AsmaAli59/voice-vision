package com.fyp.voicevision.ui.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fyp.voicevision.R;
import com.fyp.voicevision.databinding.ActivityHomeBinding;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.ui.activities.bookmarks.Bookmark;
import com.fyp.voicevision.ui.activities.levels.Levels;
import com.fyp.voicevision.ui.activities.modes.Help;
import com.fyp.voicevision.ui.activities.modes.Modes;
import com.fyp.voicevision.ui.fragments.FragmentHome;
import com.fyp.voicevision.ui.fragments.FragmentMode;
import com.fyp.voicevision.ui.fragments.FragmentProfile;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadFragment(new FragmentHome());

        binding.ifvOptionsHome.setOnClickListener(v -> binding.drawerLayoutHome.openDrawer(binding.navHome));
        binding.navHome.setNavigationItemSelectedListener(this::onMoreClick);
        binding.bnvContainerHome.setOnItemSelectedListener(this::onItemClick);
        binding.ifvHelpModes.setOnClickListener(v -> onHelpClick());
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fcvContainerHome.getId(), fragment);
        fragmentTransaction.commit();
    }

    private boolean onItemClick(MenuItem menuItem) {
        binding.ifvHelpModes.setVisibility(View.GONE);
        if (menuItem.getItemId() == R.id.menu_item_home) {
            loadFragment(new FragmentHome());
            return true;
        } else if (menuItem.getItemId() == R.id.menu_item_modes) {
            binding.ifvHelpModes.setVisibility(View.VISIBLE);
            loadFragment(new FragmentMode());
            return true;
        } else if (menuItem.getItemId() == R.id.menu_item_profile) {
            loadFragment(new FragmentProfile());
            return true;
        }
        return false;
    }

    private boolean onMoreClick(MenuItem menuItem) {
        binding.drawerLayoutHome.closeDrawer(binding.navHome);
        if (menuItem.getItemId() == R.id.menu_item_bookmarks) {
            startActivity(new Intent(this, Bookmark.class));
            return true;
        } else if (menuItem.getItemId() == R.id.menu_item_modes) {
            startActivity(new Intent(this, Modes.class));
            return true;
        } else if (menuItem.getItemId() == R.id.menu_item_levels) {
            onLevelClick();
            return true;
        } else if (menuItem.getItemId() == R.id.menu_item_logout) {
            showLogoutDialog();
            return true;
        }
        return false;
    }

    private void onLevelClick() {
        Intent intent = new Intent(this, Levels.class);
        intent.putExtra("isUpdate", true);
        startActivity(intent);
    }

    private void showLogoutDialog() {
        MaterialAlertDialogBuilder builder = DialogUtils.buildDialogWithListener(this, "Logout", "Are you sure to Logout?", true);
        builder.setPositiveButton("Confirm", ((dialog, which) -> {
            dialog.dismiss();
            FirebaseUtils.logout(this);
        }));
        builder.show();
    }


    private void onHelpClick() {
        startActivity(new Intent(this, Help.class));
    }
}