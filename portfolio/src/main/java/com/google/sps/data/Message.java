package com.google.sps.data;

public final class Message {
<<<<<<< HEAD
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
=======
  private final long id;
  private final String comment;
  private final String yes;
  private final String email;
  private final long timestamp;

  /** Add the new message to the list of all messages */
  public Message(long id, String comment, String yes, String email, long timestamp) {
    this.id = id;
    this.comment = comment;
    this.yes = yes;
    this.email = email;
    this.timestamp = timestamp;
  }
>>>>>>> a78d3eb7fa6469b91e5678e376eecf4af04e8840
}
