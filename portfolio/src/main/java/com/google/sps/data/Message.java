package com.google.sps.data;

public final class Message {
    private final long id;
    private final String comment;
    private final Boolean yes;
    private final String email;
    private final long timestamp;

    /** A comment within a list of accumulated comments */
    public Message(long id, String comment, Boolean yes, String email, long timestamp) {
        this.id = id;
        this.comment = comment;
        this.yes = yes;
        this.email = email;
        this.timestamp = timestamp;
    }
}
