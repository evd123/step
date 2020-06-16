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
    List<TimeRange> times = new ArrayList<>();
    List<TimeRange> newTimes = new ArrayList<>();
    if (request.getDuration() <= TimeRange.END_OF_DAY - TimeRange.START_OF_DAY) {
        times.add(TimeRange.WHOLE_DAY);
        newTimes.add(TimeRange.WHOLE_DAY);
        for (String person : request.getAttendees()) {
            for (Event mtg : events) {
                Set<String> curAttending = mtg.getAttendees();
                TimeRange curWhen = mtg.getWhen();
                if (curAttending.contains(person)) {
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
    Set<TimeRange> finalTimes = new HashSet<>();
    finalTimes.addAll(newTimes);
    for (TimeRange time : newTimes) {
        if (time.duration() == 0) {
            finalTimes.remove(time);
        }
    }
    return finalTimes;
  }

  public helper(List<TimeRange> times, TimeRange curWhen) {
      List<TimeRange> temp = times;
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
