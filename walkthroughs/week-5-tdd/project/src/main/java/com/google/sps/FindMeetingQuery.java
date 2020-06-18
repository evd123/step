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

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Set<TimeRange> newTimes = new HashSet<>();
    if (request.getDuration() <= TimeRange.END_OF_DAY - TimeRange.START_OF_DAY) {
        // before checking the event availability, the entire day is available
        newTimes.add(TimeRange.WHOLE_DAY);
        // iterate through the people in the message request
        for (String person : request.getAttendees()) {
            // iterate through the events already scheduled for that day
            for (Event mtg : events) {
                Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
                    List<TimeRange> times = new ArrayList<>();
                    times.addAll(newTimes);
                    // iterate through the available times
                    for (TimeRange time : times) {
                        if (time.contains(curWhen)) {
                            newTimes.remove(time);
                            newTimes.add(TimeRange.fromStartEnd(time.start(), curWhen.start(), false));
                            newTimes.add(TimeRange.fromStartEnd(curWhen.end(), time.end(), false));
                        } else if (time.overlaps(curWhen)) {
                            if (time.contains(curWhen.start())) {
                                newTimes.remove(time);
                                newTimes.add(TimeRange.fromStartEnd(time.start(), curWhen.start(), false));
                            } else if (curWhen.contains(time.start())) {
                                newTimes.remove(time);
                                newTimes.add(TimeRange.fromStartEnd(curWhen.end(), time.end(), false));
                            }
                        }
                        if (curWhen.contains(time)) {
                            newTimes.remove(time);
                        }
                    }
                } 
            }
        }  
    }
    List<TimeRange> finalTimes = new ArrayList<>();
    for (TimeRange time : newTimes) {
        if (time.duration() != 0 && request.getDuration() <= time.duration()) {
            if (finalTimes.size() == 0) {
                finalTimes.add(time);
            } else {
                for (int i = 0; i < finalTimes.size(); i++) {
                    if (time.start() < finalTimes.get(i).start()) {
                        finalTimes.add(i, time);
                    } else if (time.start() == finalTimes.get(i).start()){
                        if (time.end() < finalTimes.get(i).end()) {
                            finalTimes.add(i, time);
                        } else {
                            finalTimes.add(i+1, time);
                        }
                    } else {
                        finalTimes.add(time);
                    }
                    break;
                }
            }
        }
    }

    Set<TimeRange> optTimes = new ArrayList<>();
    // iterate through the optional attendees
    for (String opt : request.getOptionalAttendees()) {
        // iterate through the events of the day
        for (Event meet : events) {
            // check if the optional attendees is attending this (required) meeting
            if (meet.getAttendees().contains(opt)) {
                List<TimeRange> tempTimes = new ArrayList<>();
                tempTimes.addAll(optTimes); 
                for (TimeRange confirmed : finalTimes) {
                    if (confirmed.contains(meet.getWhen())) {
                        
                    }
                }
            }
        }
    }
    return finalTimes;
  }
}

