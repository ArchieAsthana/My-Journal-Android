package com.example.chana.journal.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chana.journal.models.JournalEntry;
import com.example.chana.journal.models.JournalHistory;
import com.example.chana.journal.models.JournalUser;

import java.util.Date;

public class JournalDatabaseHelper extends SQLiteOpenHelper {

    // Database information.
    private static String DATABASE_NAME = "Journal";
    private static Integer DATABASE_VERSION = 2;

    // Database table information.
    private static String ENTRIES_TABLE_NAME = "entries";
    private static String USERS_TABLE_NAME = "users";

    // Entries table information.
    private static String ENTRIES_ID_KEY = "id";
    private static String ENTRIES_TEXT_KEY = "text";
    private static String ENTRIES_DATE_KEY = "date";
    private static String ENTRIES_USER_ID_KEY = "user_id";

    // Users table information.
    private static String USERS_ID_KEY = "id";
    private static String USERS_USERNAME_KEY = "username";

    private static JournalDatabaseHelper journalDatabaseHelper;

    public static synchronized JournalDatabaseHelper getInstance(Context context) {
        if (journalDatabaseHelper == null) {
            journalDatabaseHelper = new JournalDatabaseHelper(context);
        }

        return journalDatabaseHelper;
    }

    private JournalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(journalEntryTableSQL());
        db.execSQL(journalUserTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ENTRIES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            onCreate(db);
        }
    }

    public JournalUser getJournalUser(String username) {
        JournalUser journalUser = null;

        String sqlStatement = String.format("SELECT * FROM %s WHERE %s = '%s'",
                USERS_TABLE_NAME, USERS_USERNAME_KEY, username);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        if (cursor.moveToFirst()) {
            Integer db_id = cursor.getInt(cursor.getColumnIndex(USERS_ID_KEY));
            String db_username = cursor.getString(cursor.getColumnIndex(USERS_USERNAME_KEY));
            journalUser = new JournalUser(db_id, db_username);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return journalUser;
    }

    public JournalEntry getJournalEntry(Integer id) {
        JournalEntry journalEntry = null;

        String sqlStatement = String.format("SELECT * FROM %s WHERE %s = %s",
                ENTRIES_TABLE_NAME, ENTRIES_ID_KEY, id);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        if (cursor.moveToFirst()) {
            Integer db_id = cursor.getInt(cursor.getColumnIndex(ENTRIES_ID_KEY));
            Integer db_userId = cursor.getInt(cursor.getColumnIndex(ENTRIES_USER_ID_KEY));
            String db_text = cursor.getString(cursor.getColumnIndex(ENTRIES_TEXT_KEY));
            String db_date_string = cursor.getString(cursor.getColumnIndex(ENTRIES_DATE_KEY));
            Date db_date = new Date(db_date_string);

            journalEntry = new JournalEntry(db_id, db_userId, db_text, db_date);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return journalEntry;
    }

    public JournalHistory getJournalHistory(JournalUser journalUser) {
        JournalHistory journalHistory = new JournalHistory();

        String sqlStatement = String.format("SELECT * FROM %s WHERE %s = %s",
                ENTRIES_TABLE_NAME, ENTRIES_USER_ID_KEY, journalUser.getId());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlStatement, null);

        if (cursor.moveToFirst()) {
            do {
                Integer db_id = cursor.getInt(cursor.getColumnIndex(ENTRIES_ID_KEY));
                Integer db_userId = cursor.getInt(cursor.getColumnIndex(ENTRIES_USER_ID_KEY));
                String db_text = cursor.getString(cursor.getColumnIndex(ENTRIES_TEXT_KEY));
                String db_date_string = cursor.getString(cursor.getColumnIndex(ENTRIES_DATE_KEY));
                Date db_date = new Date(db_date_string);

                JournalEntry journalEntry = new JournalEntry(db_id, db_userId, db_text, db_date);
                journalHistory.addJournalEntry(journalEntry);
            } while (cursor.moveToNext());
        }

        return journalHistory;
    }

    public void addJournalUser(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(USERS_USERNAME_KEY, username);

        try {
            db.insertOrThrow(USERS_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(USERS_TABLE_NAME, "Error inserting " + values, e);
        } finally {
            db.endTransaction();
        }
    }

    public void addJournalEntry(JournalEntry journalEntry) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(ENTRIES_TEXT_KEY, journalEntry.getText());
        values.put(ENTRIES_DATE_KEY, String.valueOf(journalEntry.getDate()));
        values.put(ENTRIES_USER_ID_KEY, journalEntry.getUserId());

        try {
            db.insertOrThrow(ENTRIES_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(ENTRIES_TABLE_NAME, "Error inserting " + values, e);
        } finally {
            db.endTransaction();
        }
    }

    public void updateJournalEntry(JournalEntry journalEntry) {
        String whereClause = ENTRIES_ID_KEY + "=" + journalEntry.getId().toString();

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(ENTRIES_TEXT_KEY, journalEntry.getText());

        db.update(ENTRIES_TABLE_NAME, values, whereClause, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteJournalEntry(JournalEntry journalEntry) {
        String whereClause = ENTRIES_ID_KEY + "=" + journalEntry.getId().toString();

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(ENTRIES_TABLE_NAME, whereClause, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private String journalEntryTableSQL() {
        return "CREATE TABLE " + ENTRIES_TABLE_NAME +
                "(" +
                ENTRIES_ID_KEY + " INTEGER PRIMARY KEY," +
                ENTRIES_USER_ID_KEY + " INTEGER," +
                ENTRIES_TEXT_KEY + " TEXT," +
                ENTRIES_DATE_KEY + " TEXT," +
                " FOREIGN KEY (" + ENTRIES_USER_ID_KEY + ") REFERENCES " +
                USERS_TABLE_NAME + "(" + USERS_ID_KEY + ")" +
                ")";
    }

    private String journalUserTableSQL() {
        return "CREATE TABLE " + USERS_TABLE_NAME +
                "(" +
                USERS_ID_KEY + " INTEGER PRIMARY KEY," +
                USERS_USERNAME_KEY + " TEXT" +
                ")";
    }
}