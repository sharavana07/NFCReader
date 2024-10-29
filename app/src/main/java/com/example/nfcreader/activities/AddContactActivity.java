package com.example.nfcreader.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcreader.R;

public class AddContactActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private EditText nameInput, emailInput, phoneInput;
    private Button submitButton;
    private TextView tagIDTV;
    private String serialNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tagIDTV = findViewById(R.id.tag_ID_TV);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            if (serialNumber != null && !nameInput.getText().toString().isEmpty() &&
                !emailInput.getText().toString().isEmpty() && !phoneInput.getText().toString().isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("name", nameInput.getText().toString());
                resultIntent.putExtra("email", emailInput.getText().toString());
                resultIntent.putExtra("phone", phoneInput.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        resolveIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    private void resolveIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            serialNumber = (id != null) ? bytesToHex(id) : "Unknown";
            tagIDTV.setText("NFC Serial Number (UID): " + serialNumber);
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
