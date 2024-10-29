package com.example.nfcreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private TextView nfcTextView;
    private ImageView create_contact_IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resolveIntent(getIntent());
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            showNoNfcDialog();
            return;
        }
        create_contact_IV = findViewById(R.id.create_contact_IV);
        create_contact_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            openNfcSettings();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE // Use FLAG_MUTABLE for API 31+
        );
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void showNoNfcDialog() {
//        new MaterialAlertDialogBuilder(this)
//                .setMessage(R.string.no_nfc)
//                .setNeutralButton(R.string.close_app, (dialog, which) -> finish())
//                .setCancelable(false)
//                .show();
    }

    private void openNfcSettings() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent = new Intent(Settings.Panel.ACTION_NFC);
        } else {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        startActivity(intent);
    }

    private void resolveIntent(Intent intent) {
        List<String> validActions = List.of(NfcAdapter.ACTION_TAG_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED, NfcAdapter.ACTION_NDEF_DISCOVERED);
        if (validActions.contains(intent.getAction())) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String serialNumber = (id != null) ? bytesToHex(id) : "Unknown";

            // Display serial number (UID)
            TextView serialTextView = new TextView(this);
            serialTextView.setText("NFC Serial Number (UID): " + serialNumber);

            // Get current timestamp
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            // Insert NFC data into the database
            NfcDataClass nfcData = new NfcDataClass(serialNumber, timeStamp);

// Use an Executor to run the insert operation in a background thread
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                NfcDatabase db = NfcDatabase.getDatabase(getApplicationContext());
                db.nfcDataDao().insertNfcData(nfcData);
            });

        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

}