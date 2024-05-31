package com.fyp.voicevision.ui.activities.authentication;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.TAG;
import static com.fyp.voicevision.helpers.utils.GeneralUtils.hideKeyboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivitySignUpBinding;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.models.User;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.ValidationUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private ValidationUtils validationUtils;
    private ProgressDialog progressDialog;
    private String username, email, password, age;

    private void initializations() {
        validationUtils = new ValidationUtils(this);
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Creating Account...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();

        binding.mbSubmitSignUp.setOnClickListener(v -> onSignUpClick());
        binding.mtvLoginSignUp.setOnClickListener(v -> finish());
    }


    private void onSignUpClick() {
        if (checkValidations()) {
            progressDialog.show();
            getValues();
            submitForm();
        }
    }

    private boolean checkValidations() {
        return validationUtils.isEmailValid(binding.ltEmailSignUp, binding.etEmailSignUp)
                && validationUtils.isUsernameValid(binding.ltFullNameSignUp, binding.etFullNameSignUp)
                && validationUtils.isPasswordValid(binding.ltPasswordSignUp, binding.etPasswordSignUp)
                && validationUtils.isConfirmPasswordValid(binding.ltConfirmPasswordSignUp, binding.etConfirmPasswordSignUp, binding.etPasswordSignUp)
                && validationUtils.isAgeValid(binding.ltAgeSignUp, binding.etAgeSignUp);
    }

    private void getValues() {
        username = String.valueOf(binding.etFullNameSignUp.getText()).trim();
        email = String.valueOf(binding.etEmailSignUp.getText()).trim();
        password = String.valueOf(binding.etPasswordSignUp.getText()).trim();
        age = String.valueOf(binding.etAgeSignUp.getText()).trim();
    }

    private void submitForm() {
        hideKeyboard(this.getCurrentFocus());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    setValues(firebaseUser);
                }
            } else {
                progressDialog.dismiss();
                Log.d(TAG, "submitForm: Exception: " + task.getException());
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    notifyUser("Email Address has already been registered.");
                } else if (task.getException() != null) {
                    notifyUser(task.getException().getMessage());
                }
            }
        });
    }

    private void notifyUser(String message) {
        DialogUtils.buildDialog(this, "Failed", message, false, false);
    }

    private void setValues(FirebaseUser firebaseUser) {
        User user = new User(
                firebaseUser.getUid(),
                username,
                email,
                age,
                ModeType.none,
                LevelType.none,
                LevelType.none,
                1,
                System.currentTimeMillis()
        );

        FirebaseUtils.dr_users.child(user.getUid()).setValue(user).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                FirebaseAuth.getInstance().signOut();
                MaterialAlertDialogBuilder builder = DialogUtils.buildDialogWithListener(this, "Successful", "Account Created Successfully. Login to proceed.", false);
                builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                });
                builder.show();
            } else {
                DialogUtils.buildDialog(this, "Failed", "Failed to create account", false, false);
            }
        });
    }
}