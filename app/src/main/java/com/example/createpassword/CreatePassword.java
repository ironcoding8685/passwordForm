package com.example.createpassword;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CreatePassword extends AppCompatActivity {

    private TextView textViewPasswordStrength;
    private TextView textView8Character;
    private TextView textViewUpperCase;
    private TextView textViewSpecialCharacter;
    private ProgressBar passwordStrengthProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_password);

        textViewPasswordStrength = findViewById(R.id.textViewPasswordStrength);
        passwordStrengthProgressBar = findViewById(R.id.passwordStrengthProgressBar);
        textView8Character = findViewById(R.id.textView8Character);
        textViewUpperCase = findViewById(R.id.textViewUpperCase);
        textViewSpecialCharacter = findViewById(R.id.textViewSpecialCharacter);


        TextInputEditText editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String password = s.toString();
                if (password.isEmpty()) {
                    // If password is empty, set progress to 0 and use zero_drawable.xml
                    passwordStrengthProgressBar.setProgress(0);
                    passwordStrengthProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.zero));
                    // Update other UI elements accordingly
                    textViewPasswordStrength.setText("");
                    updateTextViewDrawable(textViewUpperCase, false);
                    updateTextViewDrawable(textView8Character, false);
                    updateTextViewDrawable(textViewSpecialCharacter, false);
                } else {
                    // If password is not empty, calculate strength and update UI
                    int strength = calculatePasswordStrength(password);
                    updatePasswordStrengthUI(strength);
                    updateTextViewDrawable(textViewUpperCase, containsUpperCase(password));
                    updateTextViewDrawable(textView8Character, password.length() >= 8);
                    updateTextViewDrawable(textViewSpecialCharacter, containsSpecialCharacter(password));
                }
            }

        });
    }

    private int calculatePasswordStrength(String password) {
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;
        boolean hasLength = password.length() >= 8;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (isSpecialCharacter(c)) {
                hasSpecialChar = true;
            }
        }

        if (hasUpperCase && hasSpecialChar && hasLength) {
            return 100;
        } else {
            // Calculate the strength percentage based on the number of conditions met
            int conditionsMet = (hasUpperCase ? 1 : 0) + (hasSpecialChar ? 1 : 0) + (hasLength ? 1 : 0);
            return (int) ((conditionsMet / 3.0) * 100);
        }
    }

    private boolean isSpecialCharacter(char c) {

        String specialCharacters = "!@#$%^&*()-+";
        return specialCharacters.indexOf(c) != -1;
    }

    private void updatePasswordStrengthUI(int strengthPercent) {


        if (strengthPercent >= 75) {
            textViewPasswordStrength.setText("Strong");
            passwordStrengthProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.strong));
        } else if (strengthPercent >= 50) {
            textViewPasswordStrength.setText("Medium");
            passwordStrengthProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.medium));
        } else {
            textViewPasswordStrength.setText("Weak");
            passwordStrengthProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.weak));
        }
        passwordStrengthProgressBar.setProgress(strengthPercent);
    }
    private boolean containsUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean containsSpecialCharacter(String str) {
        // Define the set of special characters you want to check for
        String specialCharacters = "!@#$%^&*()-+";
        for (char c : str.toCharArray()) {
            if (specialCharacters.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
    private void updateTextViewDrawable(TextView textView, boolean contains) {
        Drawable drawable = getResources().getDrawable(contains ? R.drawable.baseline_check_241 : R.drawable.baseline_check_24);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }
}
