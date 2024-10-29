package com.example.nfcreader.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcreader.R;
import com.example.nfcreader.db.NfcDatabase;
import com.example.nfcreader.model.NFCUserData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddUserActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private EditText nameInput, emailInput, phoneInput, tagId_ET;
    private Button submitButton;
    private String serialNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tagId_ET = findViewById(R.id.tagId_ET);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        submitButton = findViewById(R.id.submitButton);

        resolveIntent(getIntent());

        submitButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (serialNumber == null) {
                Toast.makeText(this, "Please scan an NFC tag", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || name.length() < 3) {
                Toast.makeText(this, "Name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (serialNumber.equalsIgnoreCase("unknown")) {
                Toast.makeText(this, "NFC id data Invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            NFCUserData nfcUserData = new NFCUserData(serialNumber, name, email, phone, timeStamp);

            addUserToDB(nfcUserData);

        });

    }

    private void addUserToDB(NFCUserData nfcUserData) {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            NfcDatabase db = NfcDatabase.getDatabase(getApplicationContext());
            long result = db.nfcDataDao().insertUserData(nfcUserData);
            if (result == -1) {
                // The insert was ignored due to a conflict
                Log.d("NfcDatabase", "Insert ignored: A user with the same primary key already exists.");
                Toast.makeText(this, "User creation failed. NFC tag is already added to a user", Toast.LENGTH_SHORT).show();
            } else {
                // The insert was successful
                Log.d("NfcDatabase", "User data inserted with row ID: " + result);
            }
        });

    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        // Checks for 10-15 digit phone numbers
        return android.util.Patterns.PHONE.matcher(phone).matches() && phone.length() >= 10 && phone.length() <= 15;
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
            tagId_ET.setText(serialNumber);
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
