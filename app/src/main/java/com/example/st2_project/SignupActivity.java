package com.example.st2_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText username = findViewById(R.id.signupUsername);
        final EditText password = findViewById(R.id.signupPassword);

        final RadioGroup genderGroup = findViewById(R.id.genderGroup);

        final Spinner countrySpinner = findViewById(R.id.countrySpinner);

        Button signupButton = findViewById(R.id.registerButton);

        signupButton.setOnClickListener(v -> {
            RadioButton selectedRadio = findViewById(genderGroup.getCheckedRadioButtonId());

            User user = new User(username.getText().toString(), password.getText().toString(),
                    selectedRadio.getText().toString(), countrySpinner.getSelectedItem().toString());

            try {
                DBHelper db = new DBHelper(getApplicationContext());

                db.addUser(user);

                Toast.makeText(SignupActivity.this, "Sign Up Successful!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(SignupActivity.this, "Insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
