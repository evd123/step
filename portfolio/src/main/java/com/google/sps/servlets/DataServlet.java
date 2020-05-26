// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//git 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.sps.data.Message;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public final class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List < Message > allMessages = new ArrayList < > ();
    for (Entity entity: results.asIterable()) {
      long id = entity.getKey().getId();
      String comment = (String) entity.getProperty("comment");
      String yes = (String) entity.getProperty("need response");
      String email = (String) entity.getProperty("email");
      long timestamp = (long) entity.getProperty("timestamp");

      Message curMessage = new Message(id, comment, yes, email, timestamp);
      allMessages.add(curMessage);
    }
    // Send the JSON as the response
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(allMessages);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String msg = request.getParameter("text-input");
    String yes = getYes(request);
    String email = request.getParameter("email-input");
    // Record time submitted.
    long timestamp = System.currentTimeMillis();

    // Intended to send user a message; don't think this actually does anything though
    response.setContentType("text/html");
    response.getWriter().println("Thanks for reaching out!");

    if (yes == "true") {
      response.setContentType("text/html");
      response.getWriter().println("I'll be in touch soon!");
    }

    // creates Entity for messages
    Entity messageEntity = new Entity("Message");
    messageEntity.setProperty("comment", msg);
    messageEntity.setProperty("need response", yes);
    messageEntity.setProperty("email", email);
    messageEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(messageEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /*
  Function to check if email is submitted if the user wants a response. Don't think this function actually does anything.
  */
  private String getYes(HttpServletRequest request) {
    String yes = request.getParameter("yes-response");
    String emailString = request.getParameter("email-input");
    if (emailString == null) {
      System.err.println("It looks like you're interested in a response! Please enter your email address so that I can get in touch.");
      return "false";
    }

    return yes;
  }
}
