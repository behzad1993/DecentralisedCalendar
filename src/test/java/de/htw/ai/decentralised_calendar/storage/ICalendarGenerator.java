package de.htw.ai.decentralised_calendar.storage;

import biweekly.ICalVersion;
import biweekly.ICalendar;
import biweekly.ValidationWarnings;
import biweekly.component.*;
import biweekly.io.scribe.component.VFreeBusyScribe;
import biweekly.parameter.FreeBusyType;
import biweekly.parameter.Role;
import biweekly.property.*;
import biweekly.util.Duration;
import de.htw.ai.decentralised_calendar.request.*;

import javax.swing.plaf.ColorUIResource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class ICalendarGenerator {

    public static List<ICalendar> createSampleICalendars(int number) {
        ArrayList<ICalendar> iCalendars = new ArrayList<>();
        for (; number > 0; number--) {
            iCalendars.add(createSampleICalendar());
        }
        return iCalendars;
    }


    public static ICalendar createSampleICalendar() {
        final ICalendar iCalendar = new ICalendar();
        Uid uid = Uid.random();
        iCalendar.setUid(uid);
        iCalendar.addName("Test name");
        iCalendar.setVersion(ICalVersion.V2_0);
        iCalendar.setLastModified(Date.from(Instant.now()));
        final LocalDateTime now = LocalDateTime.now();
        final Date modified = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        iCalendar.setLastModified(modified);
        final VEvent event = new VEvent();
        final Summary summary = event.setSummary("Meeting with Team A");
        summary.setLanguage("en-us");
        final LocalDate localDate = LocalDate.now();
        final Date start = convertToDateViaInstant(localDate);
        event.setDateStart(start);
        final Duration duration = new Duration.Builder().hours(1).build();
        event.setDuration(duration);
        event.addAttendee(createAttendeeWithRole(Role.ATTENDEE));
        event.addAttendee(createAttendeeWithRole(Role.CHAIR));
        event.addAttendee(createAttendeeWithRole(Role.DELEGATE));
        event.addAttendee(createAttendeeWithRole(Role.ORGANIZER));
        event.addAttendee(createAttendeeWithRole(Role.OWNER));
        event.setLastModified(modified);
        iCalendar.addEvent(event);
        return iCalendar;
    }

    public static Attendee createAttendeeWithRole(Role role) {
        Attendee attendee = new Attendee("user", "user@mail.com", "uri");
        attendee.setRole(role);
        return attendee;
    }


    public static ICalendar createSampleICalendarWithoutLastModified() {
        final ICalendar iCalendar = new ICalendar();
        iCalendar.addName("Test name");
        iCalendar.setVersion(ICalVersion.V2_0);
        final VEvent event = new VEvent();
        final Summary summary = event.setSummary("Meeting with Team A");
        summary.setLanguage("en-us");
        final LocalDate localDate = LocalDate.now();
        final Date start = convertToDateViaInstant(localDate);
        event.setDateStart(start);
        final Duration duration = new Duration.Builder().hours(1).build();
        event.setDuration(duration);
        iCalendar.addEvent(event);
        return iCalendar;
    }


    private static Date convertToDateViaInstant(final LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static ICalendar createSampleToDo() {
        ICalendar iCalendar = new ICalendar();
        iCalendar.setVersion(ICalVersion.V2_0);
        VTodo todo = new VTodo();
        final LocalDate localDate = LocalDate.now();
        final Date due = convertToDateViaInstant(localDate);
        todo.setDateDue(due);
        todo.setLocation("Berlin");
        todo.setOrganizer("mustermail@mail.com");
        todo.setPriority(1);
        todo.addRelatedTo("b91092bc-bb8b-11e9-9cb5-2a2ae2dbcce4");
        todo.addAttendee("mustermail@mail.com");
        todo.addAttendee("teilnehmer1@mail.com");
        todo.addAttendee("teilnehmer2@mail.com");
        iCalendar.addTodo(todo);

        return iCalendar;
    }


    public static ICalendar createFreeBusz() {
        ICalendar iCalendar = new ICalendar();
        iCalendar.setVersion(ICalVersion.V2_0);
        VFreeBusy fb = new VFreeBusy();
        fb.setOrganizer("mustermail@mail.com");
        fb.addAttendee(new Attendee("user","user@mail.com", "uri"));
        FreeBusyType free = FreeBusyType.get("FREE");
        FreeBusyType busy = FreeBusyType.get("BUSY");
        final LocalDate localDate = LocalDate.now();
        final LocalDate localDate2 = LocalDate.of(2019, 03,10);
        final Date start = convertToDateViaInstant(localDate);
        final Date start2 = convertToDateViaInstant(localDate2);
        Duration duration = new Duration.Builder().hours(1).build();
        fb.addFreeBusy(busy, start, duration);
        duration = new Duration.Builder().hours(2).minutes(40).build();
        fb.addFreeBusy(free, start2, duration);
        iCalendar.addFreeBusy(fb);

        return iCalendar;
    }


    public static ICalendar createJournal() throws IOException {
        ICalendar iCalendar = new ICalendar();
        iCalendar.setVersion(ICalVersion.V2_0);
        VJournal journal = new VJournal();
        journal.addDescription("Dies hier ist ein Beispieltext über fünfzig Wörter. Es dient zur Anschauung von Textlängen. Denn nicht jeder Shopbetreiber oder Webdesginer hat eine genaue Vorstellung davon, wie ein Textumfang auf einer Webseite wirkt. Textlängen mit bis zu fünfzig Wörtern diesen meist als Kurzbeschreibung oder als Teaser-Text, der neugierig auf mehr macht.");
        journal.addAttachment(new Attachment("Beispielseite", new File("/Users/admin/Documents/HTW/DecentralisedCalendar/src/test/java/de/htw/ai/decentralised_calendar/storage/ICalendarGenerator.java")));
        journal.addAttendee("mustermail@mail.com");
        journal.addRelatedTo("b91092bc-bb8b-11e9-9cb5-2a2ae2dbcce4");
        journal.addContact("Max");
        final LocalDate localDate = LocalDate.now();
        final Date start = convertToDateViaInstant(localDate);
        journal.setDateStart(start);
        iCalendar.addJournal(journal);
        return iCalendar;
    }


    public static VTimezone createTimeZone() {
        VTimezone t = new VTimezone("Europe/Berlin");
        t.addStandardTime(new StandardTime());
        t.addDaylightSavingsTime(new DaylightSavingsTime());
        return t;
    }


    public static VAlarm createAlarm() {
        final LocalDate localDate = LocalDate.now();
        final Date trigger = convertToDateViaInstant(localDate);
        VAlarm vAlarm = new VAlarm(Action.audio(), new Trigger(trigger));
        vAlarm.setRepeat(4);
        final Duration duration = new Duration.Builder().minutes(10).build();
        vAlarm.setDuration(duration);
        return vAlarm;
    }


    public static void createSampleICalendarWithWrongDates() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ICalendar ical = new ICalendar();

        VEvent event = new VEvent();
        event.setDateStart(df.parse("2019-07-15 18:00"));
        event.setDateEnd(df.parse("2013-07-15 12:00"));
        ical.addEvent(event);

        ValidationWarnings warnings = ical.validate(ICalVersion.V2_0);
        System.out.println(warnings.toString());

        // Ausgabe der Konsole:
        // [ICalendar > VEvent]: (16) The start date must come before the end date.
    }

    public static LinkedList<Request> storageInit(final StorageManager storageManager) throws Exception {
        final ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        final String filename = sampleICalendar.getUid().getValue() + ".ics";
        final RequestManager requestManager = storageManager.getRequestManager();

        final CreateFile createFile = new CreateFile(sampleICalendar.write());
        final Delete delete = new Delete(13);
        final Update update = new Update(10, "SUMMARY;LANGUAGE=en-us:Meeting with Team A", "SUMMARY;LANGUAGE=en-us:Meeting with Team B");
        final Insert insert = new Insert(17, "ATTENDEE;ROLE=ATTENDEE;CN=user;EMAIL=NEWUSER@mail.com:uri");

//        System.out.println("Storage initialisation CREATION:");
//        System.out.println(delete);
//        System.out.println(update);
//        System.out.println(insert);
//        System.out.println("\n");

        final RequestLog instance = RequestLog.getInstance();
        final LinkedList<Request> requestListToClear = instance.getRequestList();
        requestListToClear.clear();
        instance.setRequestList(requestListToClear);

        LinkedList<Request> cbfList = new LinkedList<>();

        cbfList.add(requestManager.generatingRequest(createFile, filename));
        cbfList.add(requestManager.generatingRequest(delete, filename));
        cbfList.add(requestManager.generatingRequest(update, filename));
        cbfList.add(requestManager.generatingRequest(insert, filename));

        final LinkedList<Request> requestListCanonized = requestManager.getLog().getRequestList();

//        System.out.println("Storage initialisation CANONIZED:");
//        for (Request request : requestListCanonized) {
//            System.out.println(request.getOperation());
//        }
//        System.out.println("\n");

//        System.out.println("Storage initialisation CBF:");
//        for (Request request : cbfList) {
//            System.out.println(request.getOperation());
//        }
//        System.out.println("\n");

        return cbfList;
    }
}