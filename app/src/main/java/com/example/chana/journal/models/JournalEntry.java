package com.example.chana.journal.models;

import java.io.Serializable;
import java.util.Date;

public class JournalEntry implements Serializable {
    private Integer id;
    private Integer userId;
    private String text;
    private Date date;

    public JournalEntry(Integer id, Integer userId, String text, Date date) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }
}
