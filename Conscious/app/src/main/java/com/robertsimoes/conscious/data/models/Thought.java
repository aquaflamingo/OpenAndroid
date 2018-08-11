/*
 * Copyright (c) 2017. Pressure Labs. All Rights Reserved.
 */

package com.robertsimoes.conscious.data.models;

/**
 * Copyright (c) 2017 Pressure Labs.
 *
 */

public class Thought {
    public String getHash() {
        return hash;
    }

    private String hash;
    private long id;
    private String title;
    private String timeStamp;
    private String body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Thought(long id, String timeStamp, String title, String body) {
        this.id = id;
        this.title = title;
        this.timeStamp = timeStamp;
        this.body = body;
    }

    public Thought(String timeStamp, String title, String body) {
        this.title = title;
        this.timeStamp = timeStamp;
        this.body = body;
    }

    public Thought(long id, String timeStamp, String title, String body, String hash) {
        this.id = id;
        this.title = title;
        this.timeStamp = timeStamp;
        this.body = body;
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", body='" + body + '\'' +
                ", hashPBK=" + hash +
                '}';
    }
}
