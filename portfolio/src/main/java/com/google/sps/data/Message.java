package com.google.sps.data;

public final class Message {
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
}
