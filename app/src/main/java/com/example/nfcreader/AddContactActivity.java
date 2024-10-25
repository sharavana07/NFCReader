package com.example.nfcreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, phoneInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);  // Ensure this layout contains EditTexts and Button

        // Initializing the views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        submitButton = findViewById(R.id.submitButton);

        // Handling submit button click event
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();

                // Checking if all fields are filled
                if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                    // Prepare the result to send back to MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("email", email);
                    resultIntent.putExtra("phone", phone);

                    // Send the result back to MainActivity
                    setResult(RESULT_OK, resultIntent);
                    finish();  // Close AddContactActivity and return to MainActivity
                } else {
                    // Show error if any input is missing
                    Toast.makeText(AddContactActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
