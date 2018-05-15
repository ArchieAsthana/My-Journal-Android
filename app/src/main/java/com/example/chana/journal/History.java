package com.example.chana.journal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.chana.journal.helpers.JournalDatabaseHelper;
import com.example.chana.journal.models.JournalEntry;
import com.example.chana.journal.models.JournalHistory;
import com.example.chana.journal.models.JournalUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chana.journal.R.id.toolbar;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar menuToolbar;
    private ListView listView;
    private JournalHistory journalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        menuToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(menuToolbar);

        listView = (ListView) findViewById(R.id.ListView);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        journalHistory = getJournalHistory();

        List<String> data = new ArrayList<>();
        for (JournalEntry entry : journalHistory.getHistory()) {
            data.add(entry.getText());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.journal_history_menu, menu); // set your file name
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_entry_history:
                Intent journalEntryIntent = new Intent(getApplicationContext(), JournalEntryActivity.class);
                journalEntryIntent.putExtra("JOURNAL_USER", getJournalUser());
                startActivity(journalEntryIntent);
                return true;

            case R.id.logout_history:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
        }

        return false;
    }

    protected JournalUser getJournalUser() {
        return (JournalUser) getIntent().getSerializableExtra("JOURNAL_USER");
    }

    private JournalHistory getJournalHistory() {
        JournalDatabaseHelper dbHelper = JournalDatabaseHelper.getInstance(History.this);
        return dbHelper.getJournalHistory(getJournalUser());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JournalEntry journalEntry = journalHistory.getHistory().get(position);

        Intent journalEntryIntent = new Intent(getApplicationContext(), JournalEntryActivity.class);
        journalEntryIntent.putExtra("JOURNAL_USER", getJournalUser());
        journalEntryIntent.putExtra("JOURNAL_ENTRY", journalEntry);
        startActivity(journalEntryIntent);
    }
}