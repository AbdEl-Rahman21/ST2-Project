package com.example.st2_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageAccountActivity extends AppCompatActivity {
    Button deleteButton;
    Button editButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        App app = (App) getApplicationContext();
        user = app.getCurrentUser();

        EditText username = findViewById(R.id.manageUsername);
        username.setText(user.getUsername());

        EditText password = findViewById(R.id.managePassword);
        password.setText(user.getPassword());

        Spinner countrySpinner = findViewById(R.id.manageCountrySpinner);
        for (int i = 0; i < countrySpinner.getCount(); i++) {
            if (countrySpinner.getItemAtPosition(i).toString().equals(user.getCountry())) {
                countrySpinner.setSelection(i);

                break;
            }
        }

        deleteButton = findViewById(R.id.deleteButton);
        registerForContextMenu(deleteButton);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(this::showPopupMenu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_account) {
            try {
                DBHelper db = new DBHelper(getApplicationContext());

                db.deleteUser(user.getId());

                Toast.makeText(ManageAccountActivity.this, "Deletion Successful!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ManageAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(ManageAccountActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(ManageAccountActivity.this, view);
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit_account) {
                EditText username = findViewById(R.id.manageUsername);
                EditText password = findViewById(R.id.managePassword);
                Spinner countrySpinner = findViewById(R.id.manageCountrySpinner);

                User newUser = new User(username.getText().toString(), password.getText().toString(),
                        "", countrySpinner.getSelectedItem().toString());

                try {
                    DBHelper db = new DBHelper(getApplicationContext());

                    db.editUser(user.getId(), newUser);

                    Toast.makeText(ManageAccountActivity.this, "Edit Successful!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ManageAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(ManageAccountActivity.this, "Edit failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            return true;
        });

        popupMenu.show();
    }
}
