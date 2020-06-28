package de.htw.ai.decentralised_calendar;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.property.Uid;
import de.htw.ai.decentralised_calendar.storage.ICalendarGenerator;
import de.htw.ai.decentralised_calendar.storage.Synchronizer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


class SynchronizerTest {


    @Test
    void test_syncDevices_sameEntriesInBothLists_shouldBeAllEntriesOnce() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("0");
        list1.add("1");
        list1.add("2");
        list1.add("3");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("1");
        list2.add("2");
        list2.add("3");

        List<String> strings = Synchronizer.syncDevices(list1, list2);
        String[] expectedStrings = {"0", "1", "2", "3"};
        assertThat(strings).containsExactlyInAnyOrder(expectedStrings);
    }


    @Test
    void test_syncDevices_OneEmptyList_shouldBeAllEntriesFromListWithEntries() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("0");
        list1.add("1");
        list1.add("2");
        list1.add("3");

        ArrayList<String> list2 = new ArrayList<>();

        List<String> strings = Synchronizer.syncDevices(list1, list2);
        String[] expectedStrings = {"0", "1", "2", "3"};
        assertThat(expectedStrings).containsExactly(expectedStrings);

        strings = Synchronizer.syncDevices(list2, list1);
        assertThat(strings).containsExactlyInAnyOrder(expectedStrings);
    }


    @Test
    void test_syncDevices_differentListsWithoutSameEntries_shouldHaveEveryEntryOnce() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("0");
        list1.add("3");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("1");
        list2.add("2");

        List<String> strings = Synchronizer.syncDevices(list1, list2);
        String[] expectedStrings = {"0", "1", "2", "3"};
        System.out.println(strings);
        assertThat(strings).containsExactlyInAnyOrder(expectedStrings);
    }


    @Test
    void test_syncDevices_differentListsWithEqualEntries_shouldHaveEveryEntryOnce() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("0");
        list1.add("2");
        list1.add("3");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("1");
        list2.add("3");

        List<String> strings = Synchronizer.syncDevices(list1, list2);
        String[] expectedStrings = {"0", "1", "2", "3"};
        assertThat(strings).containsExactlyInAnyOrder(expectedStrings);
    }


    @Test
    void test_ical_withSampleFolder() throws IOException {
        File alone = new File("extern/testFiles/sampleICS/alone_2019-08-10T17:41:34.070722.ics");
        File new1 = new File("extern/testFiles/sampleICS/new1_2019-08-10T17:41:34.068853.ics");
        File new2 = new File("extern/testFiles/sampleICS/new2_2019-08-10T17:41:34.068853.ics");
        File renewed1 = new File("extern/testFiles/sampleICS/renewed1_2019-08-10T17:41:34.068853.ics");
        File renewed2 = new File("extern/testFiles/sampleICS/renewed2_2019-08-10T17:41:34.068853.ics");
        File same1 = new File("extern/testFiles/sampleICS/same1_2019-08-10T17:41:34.068853.ics");
        File same2 = new File("extern/testFiles/sampleICS/same2_2019-08-10T17:41:34.068853.ics");

        ArrayList<File> files = new ArrayList<>();
        files.add(alone);
        files.add(new1);
        files.add(new2);
        files.add(renewed1);
        files.add(renewed2);
        files.add(same1);
        files.add(same2);

        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> expectingList = new ArrayList<>();
        expectingList.add(Files.readString(alone.toPath()));
        expectingList.add(Files.readString(new1.toPath()));
        expectingList.add(Files.readString(renewed2.toPath()));
        expectingList.add(Files.readString(same1.toPath()));

        list1.add(Files.readString(alone.toPath()));
        list1.add(Files.readString(new1.toPath()));
        list1.add(Files.readString(renewed2.toPath()));
        list1.add(Files.readString(same1.toPath()));

        list2.add(Files.readString(new2.toPath()));
        list2.add(Files.readString(renewed1.toPath()));
        list2.add(Files.readString(same2.toPath()));

        List<String> strings = Synchronizer.syncDevices(list1, list2);

        Collections.sort(strings);
        Collections.sort(expectingList);

        assertEquals(expectingList, strings);

//        assertThat(strings).containsExactlyInAnyOrder(expectingArray);
    }
}