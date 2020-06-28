package de.htw.ai.decentralised_calendar.request;

import biweekly.property.Uid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Behzad Karimi 2019-09-09
 * @project decentralised_calendar
 */
class InsertTest {

    private String sample = "BEGIN:VCALENDAR\n" +
            "VERSION:2.0\n" +
            "PRODID:-//Michael Angstadt//biweekly 0.6.3//EN\n" +
            "UID:b91098f2-bb8b-11e9-9cb5-2a2ae2dbcce4\n" +
            "NAME:same\n" +
            "LAST-MODIFIED:20190810T152454Z\n" +
            "BEGIN:VEVENT\n" +
            "DTSTAMP:20190810T152454Z\n" +
            "SUMMARY;LANGUAGE=en-us:Meeting with Team A\n" +
            "DTSTART:20190809T220000Z\n" +
            "DURATION:PT1H\n" +
            "END:VEVENT\n" +
            "END:VCALENDAR\n";

    @Test
    void test_doOperation_insertNewLine() {
        System.out.println(sample);
        String newAttribute = "BEGIN:VCALENDARx";
        Insert insert = new Insert(1, newAttribute);
        String result = insert.doOperation(sample);
        System.out.println(result);
    }

}