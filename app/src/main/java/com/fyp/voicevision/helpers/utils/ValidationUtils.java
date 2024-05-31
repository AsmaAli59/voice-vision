package com.fyp.voicevision.helpers.utils;

import android.content.Context;
import android.util.Patterns;

import com.fyp.voicevision.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private final Context context;

    public ValidationUtils(Context context) {
        this.context = context;
    }

    public boolean isFieldEmpty(TextInputLayout layout, TextInputEditText editText) {
        String email_address = String.valueOf(editText.getText()).trim();
        if (email_address.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }

    public boolean isAgeValid(TextInputLayout layout, TextInputEditText editText) {
        String age = String.valueOf(editText.getText()).trim();
        int ageNumber = Integer.parseInt(age);
        if (age.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else if (ageNumber <= 14) {
            layout.setError(context.getResources().getString(R.string.age_limit_14));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }

    public boolean isEmailValid(TextInputLayout layout, TextInputEditText editText) {
        String email_address = String.valueOf(editText.getText()).trim();
        if (email_address.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            layout.setError(context.getResources().getString(R.string.invalid_input));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }

    public boolean isUsernameValid(TextInputLayout layout, TextInputEditText editText) {
        String username = String.valueOf(editText.getText()).trim();
        if (username.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else if (!username.matches("^[a-zA-Z\\s]+$")) {
            layout.setError(context.getResources().getString(R.string.invalid_input));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }

    public boolean isPasswordValid(TextInputLayout layout, TextInputEditText editText) {
        String password = String.valueOf(editText.getText()).trim();

        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        if (password.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else if (password.length() < 8) {
            layout.setError(context.getResources().getString(R.string.invalid_password_length));
        } else if (!matcher.matches()) {
            layout.setError(context.getResources().getString(R.string.invalid_password_length));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }

    public boolean isConfirmPasswordValid(TextInputLayout layout, TextInputEditText editText, TextInputEditText et_password) {
        String confirmPassword = String.valueOf(editText.getText()).trim();
        String password = String.valueOf(et_password.getText()).trim();
        if (confirmPassword.isEmpty()) {
            layout.setError(context.getResources().getString(R.string.field_cant_be_empty));
        } else if (confirmPassword.length() < 8) {
            layout.setError(context.getResources().getString(R.string.invalid_password_length));
        } else if (!password.equals(confirmPassword)) {
            layout.setError(context.getResources().getString(R.string.password_does_not_match));
        } else {
            layout.setError(null);
            return true;
        }
        return false;
    }
}
