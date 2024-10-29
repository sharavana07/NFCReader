package com.example.nfcreader.activities;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nfcreader.R;
import com.example.nfcreader.db.NfcDatabase;
import com.example.nfcreader.model.NfcLogs;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogsActivity extends AppCompatActivity {

    private TableLayout logsTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        logsTableLayout = findViewById(R.id.logsTableLayout);

        loadLogs();
    }

    private void loadLogs() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            NfcDatabase db = NfcDatabase.getDatabase(getApplicationContext());
            List<NfcLogs> logList = db.nfcDataDao().getAllLogs();

            runOnUiThread(() -> populateTable(logList));
        });
    }

    private void populateTable(List<NfcLogs> logList) {
        logsTableLayout.removeAllViews();

        // Add table headers
        TableRow headerRow = new TableRow(this);

        TextView headerPKey = new TextView(this);
        headerPKey.setText("PKey");
        headerPKey.setPadding(16, 16, 16, 16);
        headerRow.addView(headerPKey);

        TextView headerTagID = new TextView(this);
        headerTagID.setText("Tag ID");
        headerTagID.setPadding(16, 16, 16, 16);
        headerRow.addView(headerTagID);

        TextView headerTimeStamp = new TextView(this);
        headerTimeStamp.setText("Timestamp");
        headerTimeStamp.setPadding(16, 16, 16, 16);
        headerRow.addView(headerTimeStamp);

        logsTableLayout.addView(headerRow);

        // Add log entries
        for (NfcLogs log : logList) {
            TableRow row = getTableRow(log);

            logsTableLayout.addView(row);
        }
    }

    private @NonNull TableRow getTableRow(NfcLogs log) {
        TableRow row = new TableRow(this);

        TextView pKeyTextView = new TextView(this);
        pKeyTextView.setText(log.getpKey());
        pKeyTextView.setPadding(16, 16, 16, 16);
        row.addView(pKeyTextView);

        TextView tagIDTextView = new TextView(this);
        tagIDTextView.setText(log.getTagID());
        tagIDTextView.setPadding(16, 16, 16, 16);
        row.addView(tagIDTextView);

        TextView timeStampTextView = new TextView(this);
        timeStampTextView.setText(log.getTimeStamp());
        timeStampTextView.setPadding(16, 16, 16, 16);
        row.addView(timeStampTextView);
        return row;
    }
}
