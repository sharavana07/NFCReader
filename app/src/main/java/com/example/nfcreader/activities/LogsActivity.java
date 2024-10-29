package com.example.nfcreader.activities;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        // Clear existing rows if any
        logsTableLayout.removeAllViews();

        // Add table headers
        TableRow headerRow = new TableRow(this);
        TextView headerTagID = new TextView(this);
        headerTagID.setText("Tag ID");
        headerTagID.setPadding(8, 8, 8, 8);
        headerRow.addView(headerTagID);

        TextView headerTimeStamp = new TextView(this);
        headerTimeStamp.setText("Timestamp");
        headerTimeStamp.setPadding(8, 8, 8, 8);
        headerRow.addView(headerTimeStamp);

        logsTableLayout.addView(headerRow);

        // Add log entries
        for (NfcLogs log : logList) {
            TableRow row = new TableRow(this);
            TextView tagIDTextView = new TextView(this);
            tagIDTextView.setText(log.getTagID());
            tagIDTextView.setPadding(8, 8, 8, 8);
            row.addView(tagIDTextView);

            TextView timeStampTextView = new TextView(this);
            timeStampTextView.setText(log.getTimeStamp());
            timeStampTextView.setPadding(8, 8, 8, 8);
            row.addView(timeStampTextView);

            logsTableLayout.addView(row);
        }
    }
}
