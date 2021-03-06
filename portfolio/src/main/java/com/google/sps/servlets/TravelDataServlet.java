// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/travel-data")
public class TravelDataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Continent");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> strContinents = new ArrayList<>();
    for (Entity entity: results.asIterable()) {
        String continent = (String) entity.getProperty("continent");
        strContinents.add(continent);
    }

    HashMap<String, Integer> voteCount = getVotes(strContinents);
    
    response.setContentType("application/json");
    Gson gson = new Gson();
    gson.toJson(gson.toJsonTree(voteCount), gson.newJsonWriter(response.getWriter()));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String continent = request.getParameter("continent");

    // creates Entity for continent vote
    Entity continentEntity = new Entity("Continent");
    continentEntity.setProperty("continent", continent);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(continentEntity);

    response.sendRedirect("/index.html");
  }

  private HashMap<String, Integer> getVotes(List<String> continents) {
      HashMap<String, Integer> voteStore = new HashMap<>();
      for (String continent : continents) {
          if (voteStore.containsKey(continent)) {
              voteStore.put(continent, voteStore.get(continent)+1);
          } else {
              voteStore.put(continent, 1);
          }
      }
      return voteStore;
  }
}
