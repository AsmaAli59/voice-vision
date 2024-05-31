package com.fyp.voicevision.ui.activities.authentication;

import static com.fyp.voicevision.helpers.utils.GeneralUtils.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityForgetPasswordBinding;
import com.fyp.voicevision.helpers.utils.DialogUtils;
import com.fyp.voicevision.helpers.utils.ValidationUtils;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;
    private ValidationUtils validationUtils;
    private ProgressDialog progressDialog;

    private void initializations() {
        validationUtils = new ValidationUtils(this);
        progressDialog = DialogUtils.buildProgressDialog(this, "Please Wait!", "Processing...", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializations();

        binding.mcvBackForgetPassword.setOnClickListener(v -> finish());
        binding.mbSubmitForgetPassword.setOnClickListener(v -> onSubmitClick());
    }

    private void onSubmitClick() {
        if (checkValidations()) {
            submitForm();
        }
    }

    private void submitForm() {
        progressDialog.show();
        String email = String.valueOf(binding.etEmailForgetPassword.getText()).trim();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                DialogUtils.buildDialog(this, "Password Reset!", "We have sent you a password reset verification link on your email. Please check.", false, true);
            } else {
                Log.d(TAG, "submitForm: Exception: Forget_Password: " + task.getException());
                if (task.getException() != null)
                    DialogUtils.buildDialog(this, "Failed", task.getException().getMessage(), false, true);
            }
        });
    }

    private boolean checkValidations() {
        return validationUtils.isEmailValid(binding.ltEmailForgetPassword, binding.etEmailForgetPassword);
    }
}