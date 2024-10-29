package com.example.nfcreader;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddContactActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    private EditText nameInput, emailInput, phoneInput;
    private Button submitButton;

    private String serialNumber=null;
    private TextView tag_ID_TV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);  // Ensure this layout contains EditTexts and Button

        resolveIntent(getIntent());

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Initializing the views
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        submitButton = findViewById(R.id.submitButton);
        tag_ID_TV = findViewById(R.id.tag_ID_TV);

        // Handling submit button click event
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();


                // Checking if all fields are filled
                if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && serialNumber != null) {
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
    private void resolveIntent(Intent intent) {
        List<String> validActions = List.of(NfcAdapter.ACTION_TAG_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED, NfcAdapter.ACTION_NDEF_DISCOVERED);
        if (validActions.contains(intent.getAction())) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
             serialNumber = (id != null) ? bytesToHex(id) : "Unknown";

            // Display serial number (UID)
            tag_ID_TV.setText("NFC Serial Number (UID): " + serialNumber);


        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }
}
