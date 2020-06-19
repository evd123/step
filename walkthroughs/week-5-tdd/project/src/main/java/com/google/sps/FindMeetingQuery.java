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
        Set<TimeRange> requiredTimes = new HashSet<>();
        Set<TimeRange> optionalTimes = new HashSet<>();
        if (request.getDuration() <= TimeRange.END_OF_DAY - TimeRange.START_OF_DAY) {
            // before checking the event availability, the entire day is available
            // return timeslots required attendees are available for
            requiredTimes = determineAvailableTimes(events, request, request.getAttendees());
            // return timeslots optional attendees are available for
            optionalTimes = determineAvailableTimes(events, request, request.getOptionalAttendees());
        }
        // sort times that required attendees are available for
        List<TimeRange> finalRequiredTimes = new ArrayList<>();
        finalRequiredTimes.addAll(requiredTimes);
        Collections.sort(finalRequiredTimes, TimeRange.ORDER_BY_START);
        // sort times that optional attendees are available for
        List<TimeRange> finalOptionalTimes = new ArrayList<>();
        finalOptionalTimes.addAll(optionalTimes);
        Collections.sort(finalOptionalTimes, TimeRange.ORDER_BY_START);

        // check to see if the lists need to be compared; if there are no attendees at all, the entire 
        // day is available. Otherwise, if there are no optional attendees, we can
        // return times that work for required attendees, and vice versa.
        if (request.getAttendees().size() == 0 && request.getOptionalAttendees().size() == 0) {
            return Arrays.asList(TimeRange.WHOLE_DAY);
        } else if (request.getAttendees().size() == 0) {
            return finalOptionalTimes;
        } else if (request.getOptionalAttendees().size() == 0) {
            return finalRequiredTimes;
        }

        // iterate through the optional attendees
        List<TimeRange> possibleFinalTimes = new ArrayList<>();
        possibleFinalTimes = crossCheck(finalOptionalTimes, finalRequiredTimes, request);

        if (possibleFinalTimes.size() != 0) {
            return possibleFinalTimes;
        }
        return finalRequiredTimes;
    }

    /**
    * Determines which times throughout the day are available for a meeting, given events already 
    * happening, a request, a list of attendees (comes from the request, but is inputted separately 
    * to allow functionality for both required and optional attendees
    */
    public Set<TimeRange> determineAvailableTimes(Collection<Event> events, MeetingRequest request, Collection<String> attendees) {
        Set<TimeRange> availableTimes = new HashSet<>();
        availableTimes.add(TimeRange.WHOLE_DAY);
        for (String person : attendees) {
            // iterate through the events already scheduled for that day
            for (Event mtg : events) {
                Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
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
        return availableTimes;
    }

    /**
    * Checks for times in which required and optional attendees are both available 
    */
    public List<TimeRange> crossCheck(List<TimeRange> optionalTimes, List<TimeRange> requiredTimes, MeetingRequest request) {
        Set<TimeRange> tempTimes = new HashSet<>();
        for (TimeRange optTime : optionalTimes) {
            for (TimeRange reqTime : requiredTimes) {
                if (reqTime.contains(optTime)) {
                    tempTimes.add(optTime);
                } else if (optTime.contains(reqTime)) {
                    tempTimes.add(reqTime);
                } else if (reqTime.overlaps(optTime)) {
                    if (optTime.contains(reqTime.start())) {
                        TimeRange tempTime = TimeRange.fromStartEnd(reqTime.start(), optTime.end(), false);
                        if (tempTime.duration() >= request.getDuration()) {
                            tempTimes.add(tempTime);
                        }
                    } else if (reqTime.contains(optTime.start())) {
                        TimeRange tempTime = TimeRange.fromStartEnd(optTime.start(), reqTime.end(), false);
                        if (tempTime.duration() >= request.getDuration()) {
                            tempTimes.add(tempTime);
                        }
                    }
                }
            }
        }
        List<TimeRange> compared = new ArrayList<>();
        compared.addAll(tempTimes);
        return compared;
    }
}
