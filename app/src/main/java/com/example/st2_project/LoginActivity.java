package com.example.st2_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final DBHelper db = new DBHelper(getApplicationContext());

        final EditText username = findViewById(R.id.loginUsername);
        final EditText password = findViewById(R.id.loginPassword);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            User user = db.getUser(
                    username.getText().toString().trim(),
                    password.getText().toString().trim()
            );

            if (user == null) {
                Toast.makeText(LoginActivity.this, "Invalid User Info!", Toast.LENGTH_LONG).show();
            } else {
                /*Intent intent = new Intent(LoginActivity.this, MoviesListActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();*/
            }
        });

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> {
            /*Intent intent = new Intent(LoginActivity.this, MoviesListActivity.class);
            startActivity(intent);*/
        });
    }
}
