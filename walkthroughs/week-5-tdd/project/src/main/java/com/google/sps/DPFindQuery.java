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

public final class DPFindQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        List<TimeRange> requiredTimes = determineAvailableTimes(events, request, request.getAttendees());
        List<String> optionalAttendees = request.getOptionalAttendees();
        List<String>[][] matrix = new List<String>[optionalAttendees.size()][optionalAttendees.size()];

        for (int i = 0; i < optionalAttendees.size(); i++) {
            for (int j = 0; j < optionalAttendees.size(); j++) {
                if (i == 0) {
                    List<TimeRange> newAvailableTimes = timeChecker(events, optionalAttendees.get(j), requiredTimes, request)
                    if (newAvailableTimes.size() != 0) {
                        matrix[0][j] = (matrix[0][j]).add(optionalAttendees.get(j));
                    }
                } else if (j == 0) {
                    continue;
                } else {
                    
                }
            }
        }
    }

    public List<TimeRange> determineAvailableTimes(Collection<Event> events, MeetingRequest request, Collection<String> attendees) {
        Set<TimeRange> availableTimes = new HashSet<>();
        // at the beginning, the entire day is available
        availableTimes.add(TimeRange.WHOLE_DAY);
        for (String person : attendees) {
            // iterate through the events already scheduled for that day
            for (Event mtg : events) {
                Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
                    // create a temporary list of times to iterate through; this is a copy of the list that
                    // gets narrowed down each time. a copy must be made each time because otherwise the list
                    // gets mutated while being iterated through.
                    List<TimeRange> times = new ArrayList<>();
                    times.addAll(availableTimes);
                    // iterate through the available times
                    for (TimeRange time : times) {
                        if (time.contains(curWhen)) {
                            availableTimes.remove(time);
                            TimeRange tempTime1 = TimeRange.fromStartEnd(time.start(), curWhen.start(), false);
                            TimeRange tempTime2 = TimeRange.fromStartEnd(curWhen.end(), time.end(), false);
                            if (tempTime1.duration() >= request.getDuration()) {
                                availableTimes.add(tempTime1);
                            }
                            if (tempTime2.duration() >= request.getDuration()) {
                                availableTimes.add(tempTime2);
                            }
                        } else if (time.overlaps(curWhen)) {
                            if (time.contains(curWhen.start())) {
                                availableTimes.remove(time);
                                TimeRange tempTime = TimeRange.fromStartEnd(time.start(), curWhen.start(), false);
                                if (tempTime.duration() > request.getDuration()) {
                                    availableTimes.add(tempTime);
                                }
                            } else if (curWhen.contains(time.start())) {
                                availableTimes.remove(time);
                                TimeRange tempTime = TimeRange.fromStartEnd(curWhen.end(), time.end(), false);
                                if (tempTime.duration() >= request.getDuration()) {
                                    availableTimes.add(tempTime);
                                }
                            }
                        }
                        if (curWhen.contains(time)) {
                            availableTimes.remove(time);
                        }
                    }
                }
            }
        }
        List<TimeRange> finalTimes = new ArrayList<>();
        finalTimes.addAll(availableTimes);
        Collections.sort(finalTimes, TimeRange.ORDER_BY_START);
        return finalTimes;
    }

    public List<TimeRange> timeChecker(Collection<Event> events, String person, List<TimeRange> availableTimes, MeetingRequest request) {
        for (Event mtg : events) {
            Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
                    // create a temporary list of times to iterate through; this is a copy of the list that
                    // gets narrowed down each time. a copy must be made each time because otherwise the list
                    // gets mutated while being iterated through.
                    List<TimeRange> times = new ArrayList<>();
                    times.addAll(availableTimes);
                    // iterate through the available times
                    for (TimeRange time : times) {
                        if (time.contains(curWhen)) {
                            availableTimes.remove(time);
                            TimeRange tempTime1 = TimeRange.fromStartEnd(time.start(), curWhen.start(), false);
                            TimeRange tempTime2 = TimeRange.fromStartEnd(curWhen.end(), time.end(), false);
                            if (tempTime1.duration() >= request.getDuration()) {
                                availableTimes.add(tempTime1);
                            }
                            if (tempTime2.duration() >= request.getDuration()) {
                                availableTimes.add(tempTime2);
                            }
                        } else if (time.overlaps(curWhen)) {
                            if (time.contains(curWhen.start())) {
                                availableTimes.remove(time);
                                TimeRange tempTime = TimeRange.fromStartEnd(time.start(), curWhen.start(), false);
                                if (tempTime.duration() > request.getDuration()) {
                                    availableTimes.add(tempTime);
                                }
                            } else if (curWhen.contains(time.start())) {
                                availableTimes.remove(time);
                                TimeRange tempTime = TimeRange.fromStartEnd(curWhen.end(), time.end(), false);
                                if (tempTime.duration() >= request.getDuration()) {
                                    availableTimes.add(tempTime);
                                }
                            }
                        }
                        if (curWhen.contains(time)) {
                            availableTimes.remove(time);
                        }
                    }
                }
            }
            Collections.sort(availableTimes, TimeRange.ORDER_BY_START);
            return availableTimes;
    }
}