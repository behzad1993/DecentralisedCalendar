package de.htw.ai.decentralised_calendar.storage;

/**
 * @author Behzad Karimi on 2019-07-30.
 * @project decentralised_calendar
 */

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VAlarm;
import biweekly.component.VTimezone;
import biweekly.io.TimezoneInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


//TODO: assertj einsetzen
public class StorageManagerTest {


    private static final String PATH = "extern/testFiles/storageManagerTest/";
    private static StorageManager storageManager;


    @BeforeAll
    static void setUp() {
        storageManager = new StorageManager(PATH);
    }


    @BeforeEach
    void deleteFiles() {
        final List<String> fileNames = storageManager.getFilePaths();
        for (final String fileName : fileNames) {
            final File file = new File(fileName);
            file.delete();
        }
    }


    @Test
    void test_saveEntry_successfully() {
        final ICalendar toSave = ICalendarGenerator.createSampleICalendar();
        final String filename = storageManager.saveEntry(toSave);
        final String toRead = storageManager.getEntry(filename);

        final String toSaveAsString = toSave.write();
        assertThat(toSaveAsString).isEqualTo(toRead);
    }


    @Test
    void test_saveEntries_withLastModified() throws InterruptedException {
        final List<ICalendar> iCalendarList = ICalendarGenerator.createSampleICalendars(5);
        final List<String> iCalendarSaveListAsString = iCalendarList.stream()
                .map(ICalendar::write)
                .collect(Collectors.toList());

        Thread.sleep(2000);

        final List<String> filenameList = storageManager.saveEntries(iCalendarList);
        final List<String> iCalendarReadListAsString = filenameList.stream()
                .map(filename -> storageManager.getEntry(filename))
                .collect(Collectors.toList());

        assertThat(iCalendarReadListAsString).isEqualTo(iCalendarSaveListAsString);
    }


    @Test
    void test_getNumberOfAppointments_shouldBeEqualWithCreatedFiles() {
        final List<ICalendar> sampleICalendars = ICalendarGenerator.createSampleICalendars(5);
        storageManager.saveEntries(sampleICalendars);
        assertThat(storageManager.getNumberOfAppointments()).isEqualTo(sampleICalendars.size());
    }


    @Test
    void test_getAllCalendarEntriesAsList_shouldBeEqualToCreatedFiles() {
        final List<ICalendar> iCalendarList = ICalendarGenerator.createSampleICalendars(5);
        final List<String> iCalendarListAsString = iCalendarList.stream()
                .map(ICalendar::write)
                .collect(Collectors.toList());

        storageManager.saveEntries(iCalendarList);

        final List<String> allCalendarEntriesAsList = storageManager.getEntriesAsStrings();
        assertThat(allCalendarEntriesAsList).isEqualTo(iCalendarListAsString);
    }


    @Test
    void test_getAllCalendarEntriesAsOneString_shouldBeEqualToCreatedFiles() {
        final List<ICalendar> iCalendarList = ICalendarGenerator.createSampleICalendars(5);
        final List<String> iCalendarListAsString = iCalendarList.stream()
                .map(ICalendar::write)
                .collect(Collectors.toList());

        storageManager.saveEntries(iCalendarList);

        final String allCalendarEntriesAsOneString = storageManager.getEntriesAsOneString();
        final String iCalendarEntries = iCalendarListAsString.stream().collect(Collectors.joining());
        assertThat(allCalendarEntriesAsOneString).isEqualTo(iCalendarEntries);
    }


    @Test
    void test_splitEntriesToDifferentFiles_shouldSplitOneCalendarInto5() throws IOException {
        final List<ICalendar> iCalendarList = ICalendarGenerator.createSampleICalendars(5);
        Biweekly.write(iCalendarList).go(new File(PATH + "MultipleEntries"));

        final int appointmentsBeforeSplit = storageManager.getNumberOfAppointments();
        storageManager.splitEntriesToDifferentFiles();
        final int appointmentsAfterSplit = storageManager.getNumberOfAppointments();

        assertThat(appointmentsBeforeSplit).isEqualTo(1);
        assertThat(appointmentsAfterSplit).isEqualTo(5);
    }


    @Test
    void test_clearStorage_shouldClearTheDir() {
        final List<ICalendar> iCalendarList = ICalendarGenerator.createSampleICalendars(5);
        final List<String> filepathList = storageManager.saveEntries(iCalendarList);
        if (storageManager.getNumberOfAppointments() != 5) {
            return;
        }
        storageManager.clearStorage();

        final int numberOfAppointments = storageManager.getNumberOfAppointments();
        assertThat(numberOfAppointments).isEqualTo(0);
    }


    @Test
    void create() {
//        ICalendar sampleToDo = ICalendarGenerator.createSampleToDo();
//        System.out.println(sampleToDo.write());

//        ICalendar sampleToDo = ICalendarGenerator.createFreeBusz();
//        System.out.println(sampleToDo.write());

        ICalendar iCalendar = new ICalendar();
        iCalendar = ICalendarGenerator.createSampleICalendar();
        System.out.println(iCalendar.write());
    }


    @Test
    void testWrongDate() {
        try {
            ICalendarGenerator.createSampleICalendarWithWrongDates();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}