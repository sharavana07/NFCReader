package com.example.nfcreader.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcreader.model.NFCUserData;
import com.example.nfcreader.model.NfcLogs;
import com.example.nfcreader.db.NfcDatabase;
import com.example.nfcreader.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    private ImageView addUserIV;
    private Button logs_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
//            showNoNfcDialog();
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        addUserIV = findViewById(R.id.add_user_IV);
        addUserIV.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });

        logs_BTN = findViewById(R.id.logs_BTN);
        logs_BTN.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogsActivity.class);
            startActivity(intent);
        });

        resolveIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            openNfcSettings();
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
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
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.no_nfc)
                .setNeutralButton(R.string.close_app, (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void openNfcSettings() {
        Intent intent = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        ? new Intent(Settings.Panel.ACTION_NFC)
                        : new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    private void resolveIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String tagId = (id != null) ? bytesToHex(id) : "Unknown";
            getUserbyTagId(tagId);
        }
    }

    private void getUserbyTagId(String tagId) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            NfcDatabase db = NfcDatabase.getDatabase(getApplicationContext());
            NFCUserData nfcUserData = db.nfcDataDao().getUserDataByTagId(tagId);

            if (nfcUserData != null) {
                addLogToDB(nfcUserData);

            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, "Not a registered User", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addLogToDB(NFCUserData nfcUserData) {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        NfcLogs nfcData = new NfcLogs(UUID.randomUUID().toString(),nfcUserData.getTagID(), nfcUserData.getName(), nfcUserData.getEmail(), nfcUserData.getPhone(), timeStamp);

        NfcDatabase db = NfcDatabase.getDatabase(getApplicationContext());
        db.nfcDataDao().insertLog(nfcData);

    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
