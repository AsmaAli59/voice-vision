package com.fyp.voicevision.ui.activities.authentication;

import static android.content.ContentValues.TAG;
import static com.fyp.voicevision.helpers.utils.GeneralUtils.hideKeyboard;
import static com.fyp.voicevision.helpers.utils.GeneralUtils.showToast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityLoginBinding;
import com.fyp.voicevision.helpers.enums.ModeType;
import com.fyp.voicevision.helpers.firebase.FirebaseUtils;
import com.fyp.voicevision.helpers.models.User;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.SharedPreferenceUtils;
import com.fyp.voicevision.helpers.utils.ValidationUtils;
import com.fyp.voicevision.ui.activities.home.Home;
import com.fyp.voicevision.ui.activities.modes.Modes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ValidationUtils validationUtils;
    private ProgressDialog progressDialog;
    private String email, password;

    private void initializations() {
        validationUtils = new ValidationUtils(this);
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Logging in...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();

        binding.mtvForgetPasswordLogin.setOnClickListener(v -> moveScreen(0));
        binding.mtvSignupLogin.setOnClickListener(v -> moveScreen(1));
        binding.mbSubmitLogin.setOnClickListener(v -> onLoginClick());
    }

    /**
     * @param caseType 0 -> Forget Password
     *                 1 -> Modes
     *                 2 -> SignUp
     */

    private void moveScreen(int caseType) {
        Intent intent = null;
        switch (caseType) {
            case 0: {
                intent = new Intent(this, ForgetPassword.class);
                break;
            }
            case 1: {
                binding.etEmailLogin.setText(null);
                binding.etPasswordLogin.setText(null);
                binding.getRoot().requestFocus();
                intent = new Intent(this, SignUp.class);
                break;
            }
        }
        startActivity(intent);
    }

    private void onLoginClick() {
        if (checkValidations()) {
            progressDialog.show();
            getValues();
            submitForm();
        }
    }

    private boolean checkValidations() {
        return validationUtils.isEmailValid(binding.ltEmailLogin, binding.etEmailLogin)
                && validationUtils.isFieldEmpty(binding.ltPasswordLogin, binding.etPasswordLogin);
    }

    private void getValues() {
        email = String.valueOf(binding.etEmailLogin.getText()).trim();
        password = String.valueOf(binding.etPasswordLogin.getText()).trim();
    }

    private void submitForm() {
        hideKeyboard(this.getCurrentFocus());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    fetchValue(firebaseUser);
                }
            } else {
                progressDialog.dismiss();
                Log.d(TAG, "submitForm: Exception: " + task.getException());
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    showToast(this, "Invalid password");
                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    showToast(this, "Incorrect email address");
                } else if (task.getException() != null) {
                    showToast(this, task.getException().getMessage());
                }
            }
        });
    }

    private void fetchValue(FirebaseUser firebaseUser) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    navigateScreen(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(Login.this, error.getMessage());
            }
        };
        FirebaseUtils.dr_users.child(firebaseUser.getUid()).addListenerForSingleValueEvent(valueEventListener);
    }

    private void navigateScreen(User user) {
        progressDialog.dismiss();
        SharedPreferenceUtils.setMode(this, user.getModeType().toString());
        SharedPreferenceUtils.setLevel(this, user.getLevelType().toString());
        SharedPreferenceUtils.setQuizLevel(this, user.getQuizLevelType().toString());
        if (user.getModeType() == ModeType.none)
            startActivity(new Intent(this, Modes.class));
        else
            startActivity(new Intent(this, Home.class));
        finish();
    }
}