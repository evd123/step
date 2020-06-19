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
        Set<TimeRange> optTimes = new HashSet<>();
        if (request.getDuration() <= TimeRange.END_OF_DAY - TimeRange.START_OF_DAY) {
            // before checking the event availability, the entire day is available
            newTimes.add(TimeRange.WHOLE_DAY);
            optTimes.add(TimeRange.WHOLE_DAY);
            // return timeslots required attendees are available for
            newTimes = helper(events, request, request.getAttendees(), newTimes);
            // return timeslots optional attendees are available for
            optTimes = helper(events, request, request.getOptionalAttendees(), optTimes);
        }
        // sort and filter times that required attendees are available for
        List <TimeRange> finalTimes = new ArrayList<>();
        finalTimes = sorter(finalTimes, request, newTimes);
        // sort and filter times that optional attendees are available for
        List <TimeRange> finalOptTimes = new ArrayList<>();
        finalOptTimes = sorter(finalOptTimes, request, optTimes);

        // check to see if the lists need to be compared; if there are no optional attendees, can
        // return times that work for required attendees, and vice versa
        if (request.getAttendees().size() == 0) {
            return finalOptTimes;
        } else if (request.getOptionalAttendees().size() == 0) {
            return finalTimes;
        }

        // iterate through the optional attendees
        List<TimeRange> possibleFinalTimes = new ArrayList<>();
        possibleFinalTimes = comparer(finalOptTimes, finalTimes, request);

        if (possibleFinalTimes.size() != 0) {
            return possibleFinalTimes;
        }
        return finalTimes;
    }

    public Set<TimeRange> helper(Collection<Event> events, MeetingRequest request, Collection<String> attendees, Set<TimeRange> newTimes) {
        for (String person: attendees) {
            // iterate through the events already scheduled for that day
            for (Event mtg: events) {
                Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
                    List<TimeRange> times = new ArrayList<>();
                    times.addAll(newTimes);
                    // iterate through the available times
                    for (TimeRange time: times) {
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
        return newTimes;
    }

    public List<TimeRange> sorter(List<TimeRange> finalTimes, MeetingRequest request, Set<TimeRange> semiFilteredTimes) {
        for (TimeRange time: semiFilteredTimes) {
            if (time.duration() != 0 && request.getDuration() <= time.duration()) {
                if (finalTimes.size() == 0) {
                    finalTimes.add(time);
                } else {
                    for (int i = 0; i < finalTimes.size(); i++) {
                        if (time.start() < finalTimes.get(i).start()) {
                            finalTimes.add(i, time);
                        } else if (time.start() == finalTimes.get(i).start()) {
                            if (time.end() < finalTimes.get(i).end()) {
                                finalTimes.add(i, time);
                            } else {
                                finalTimes.add(i + 1, time);
                            }
                        } else {
                            finalTimes.add(time);
                        }
                        break;
                    }
                }
            }
        }
        return finalTimes;
    }

    public List<TimeRange> comparer(List<TimeRange> optTimes, List<TimeRange> newTimes, MeetingRequest request) {
        Set<TimeRange> tempTimes = new HashSet<>();
        for (TimeRange time1 : optTimes) {
            for (TimeRange time2 : newTimes) {
                if (time2.contains(time1)) {
                    tempTimes.add(time1);
                } else if (time1.contains(time2)) {
                    tempTimes.add(time2);
                } else if (time2.overlaps(time1)) {
                    if (time1.contains(time2.start())) {
                        TimeRange tempTime = TimeRange.fromStartEnd(time2.start(), time1.end(), false);
                        if (tempTime.duration() >= request.getDuration()) {
                            tempTimes.add(tempTime);
                        }
                    } else if (time2.contains(time1.start())) {
                        TimeRange tempTime = TimeRange.fromStartEnd(time1.start(), time2.end(), false);
                        if (tempTime.duration() >= request.getDuration()) {
                            tempTimes.add(tempTime);
                        }
                    }
                }
            }
        }
        List<TimeRange> compared = new ArrayList<>();
        compared = sorter(compared, request, tempTimes);
        return compared;
    }
}
