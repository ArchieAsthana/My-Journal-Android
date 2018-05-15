package com.example.chana.journal.models;

import java.io.Serializable;

public class JournalUser implements Serializable {
    private Integer id;
    private String username;

    public JournalUser(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}