package de.htw.ai.decentralised_calendar.storage;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.property.Uid;
import org.apache.commons.collections4.ListUtils;

import java.util.*;
import java.util.stream.Collectors;


public class Synchronizer {

    // TODO: zeit kann falsch gehen
//    TODO: warum nach zeit?
//    TODO: einladungen von Personen wie haendeln
//    TODO: routing tabelle verteiltes system
//    TODO:
//    TODO:
    public synchronized static List<String> syncDevices(List<String> list1, List<String> list2) {

        List<String> mergedList = ListUtils.union(list1, list2);
        mergedList = mergedList.stream().distinct().collect(Collectors.toList());

        List<ICalendar> mergedICals = mergedList.stream()
                .map(icalString -> Biweekly.parse(icalString).first())
                .collect(Collectors.toList());


        Map<Uid, List<ICalendar>> mergedMap = mergedICals.stream()
                .collect(Collectors.groupingBy(ICalendar::getUid));

        ArrayList<String> resultList = new ArrayList<>();
        for (List<ICalendar> iCalendarList : mergedMap.values()) {
            ICalendar iCalendar;
            if (iCalendarList.size() > 1) {
                iCalendar = iCalendarList.stream()
                        .max(Comparator.comparing(ical -> ical.getLastModified().getValue()))
                        .orElseThrow(NoSuchElementException::new);
            } else {
                iCalendar = iCalendarList.get(0);
            }
            resultList.add(iCalendar.write());
        }

        return resultList;
    }
}
