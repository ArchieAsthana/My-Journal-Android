package com.example.chana.journal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chana.journal.helpers.JournalDatabaseHelper;
import com.example.chana.journal.models.JournalEntry;
import com.example.chana.journal.models.JournalUser;

import java.util.Date;

import static com.example.chana.journal.R.id.toolbar;

public class JournalEntryActivity extends AppCompatActivity {
    private EditText journalText;
    private Button saveButton;
    private Toolbar menuToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menuToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(menuToolbar);

        journalText = (EditText) findViewById(R.id.JournalText);
        if (!isNewEntry()) {
            JournalEntry entry = existingEntry();
            journalText.setText(entry.getText(), TextView.BufferType.EDITABLE);
        }

        journalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (saveButton != null) {
                    saveButton.setEnabled(isValidJournalText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); // set your file name

        saveButton = (Button) findViewById( R.id.menu_Save_Entry);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        JournalDatabaseHelper dbHelper;
        switch (item.getItemId()) {
            case R.id.menu_Save_Entry:
                dbHelper = JournalDatabaseHelper.getInstance(getApplicationContext());
                if (isNewEntry()) {
                    dbHelper.addJournalEntry(getJournalEntry());
                } else {
                    JournalEntry entry = getJournalEntry();
                    Log.d("JOURNAL_ENTRY", entry.getText());
                    dbHelper.updateJournalEntry(getJournalEntry());
                }
                SimpleToast.toastWithMessage(getApplicationContext(), "Saved");
                return true;

            case R.id.menu_New_Entry:
                showJournalEntry();
                return true;

            case R.id.menu_History:
                Intent intent = new Intent(getApplicationContext(), History.class);
                intent.putExtra("JOURNAL_USER", getJournalUser());
                startActivity(intent);
                return true;

            case R.id.menu_Delete_Entry:
                dbHelper = JournalDatabaseHelper.getInstance(getApplicationContext());
                dbHelper.deleteJournalEntry(getJournalEntry());

                SimpleToast.toastWithMessage(getApplicationContext(), "Deleted");

                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                historyIntent.putExtra("JOURNAL_USER", getJournalUser());
                startActivity(historyIntent);
                return true;

            case R.id.menu_Logout:
                SimpleToast.toastWithMessage(getApplicationContext(), "Logout");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
        }

        return false;
    }

    protected JournalUser getJournalUser() {
        return (JournalUser) getIntent().getSerializableExtra("JOURNAL_USER");
    }

    protected JournalEntry getJournalEntry() {
        if (existingEntry() == null) {
            Integer userId = getJournalUser().getId();
            String text = getJournalText();
            Date date = new Date();
            return new JournalEntry(null, userId, text, date);
        } else {
            JournalEntry entry = existingEntry();
            String text = getJournalText();
            Date date = new Date();
            return new JournalEntry(entry.getId(), entry.getUserId(), text, date);
        }
    }

    private boolean isValidJournalText() {
        return !getJournalText().isEmpty();
    }

    private String getJournalText() {
        return journalText.getText().toString();
    }

    private void showJournalEntry() {
        Intent journalEntryIntent = new Intent(getApplicationContext(), JournalEntryActivity.class);
        journalEntryIntent.putExtra("JOURNAL_USER", getJournalUser());
        startActivity(journalEntryIntent);
    }

    private boolean isNewEntry() {
        return existingEntry() == null;
    }

    private JournalEntry existingEntry() {
        return (JournalEntry) getIntent().getSerializableExtra("JOURNAL_ENTRY");
    }
}