package com.example.chana.journal.models;

import java.util.ArrayList;

public class JournalHistory {
    private ArrayList<JournalEntry> history;

    public JournalHistory() {
        history = new ArrayList<JournalEntry>();
    }

    public ArrayList<JournalEntry> getHistory() {
        return history;
    }

    public void addJournalEntry(JournalEntry journalEntry) {
        history.add(journalEntry);
    }
}