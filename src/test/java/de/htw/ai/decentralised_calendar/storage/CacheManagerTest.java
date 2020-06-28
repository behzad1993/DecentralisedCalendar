package de.htw.ai.decentralised_calendar.storage;

import biweekly.Biweekly;
import biweekly.ICalendar;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


class CacheManagerTest {

    private static final String PATH_TO_CACHE = "extern/testFiles/chacheManagerTest/sender/cache/";
    private static final String PATH_TO_STORAGE = "extern/testFiles/chacheManagerTest/sender/";
    private static StorageManager storageManager;
    private static CacheManager cacheManager;


    @BeforeAll
    static void setUp() throws URISyntaxException {
        storageManager = new StorageManager(PATH_TO_STORAGE);
        cacheManager = new CacheManager(PATH_TO_CACHE, storageManager);
    }


    @BeforeEach
    void deleteFiles() {
        List<String> allFiles = storageManager.getFilePaths();
        List<String> chacheFiles = cacheManager.getFilePaths();
        allFiles.addAll(chacheFiles);
        allFiles = allFiles.stream()
                .filter(filename -> filename.endsWith(".ics"))
                .collect(Collectors.toList());
        for (final String fileName : allFiles) {
            final File file = new File(fileName);
            file.delete();
        }
    }


    @Test
    void test_copyFileToCache_shouldBeSameFile() throws IOException {
        ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        String filename = storageManager.saveEntry(sampleICalendar);
        File toCopy = new File(filename);
        String targetPath = cacheManager.copyFileToCache(toCopy);
        File resultFile = new File(targetPath);

        String stringOfCopiedFile = storageManager.getEntry(toCopy.getPath());
        String stringOfResultFile = storageManager.getEntry(resultFile.getPath());

        assertThat(stringOfCopiedFile).isEqualTo(stringOfResultFile);
    }


    @Test
    void test_filenameExistsInCache_shouldExist() {
        ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        String filename = cacheManager.saveEntry(sampleICalendar);
        boolean exists = cacheManager.filenameExistsInCache(filename);

        assertThat(exists).isTrue();
    }


    @Test
    void test_filenameExistsInCache_shouldNotExist() {
        ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        String filename = cacheManager.saveEntry(sampleICalendar);
        boolean exists = cacheManager.filenameExistsInCache(filename + "1");

        assertThat(exists).isFalse();
    }


    @Test
    void test_fileExistsInCache_shouldExist() {
        ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        String filename = cacheManager.saveEntry(sampleICalendar);
        File file = new File(filename);
        boolean exists = cacheManager.fileExistsInCache(file);

        assertThat(exists).isTrue();
    }


    @Test
    void test_fileExistsInCache_shouldNotExist() {
        ICalendar sampleICalendar = ICalendarGenerator.createSampleICalendar();
        String filename = cacheManager.saveEntry(sampleICalendar);
        File file = new File(filename + "1");
        boolean exists = cacheManager.fileExistsInCache(file);

        assertThat(exists).isFalse();
    }


    @Test
    void test_UpdateCache_shouldContainExactlyEntriesOfBothLists() {
        // Setup
        List<ICalendar> sampleICalendars = ICalendarGenerator.createSampleICalendars(6);
        List<ICalendar> storageEntries = new ArrayList<>(sampleICalendars);
        List<ICalendar> cacheEntries = new ArrayList<>();
        cacheEntries.add(sampleICalendars.get(0));
        cacheEntries.add(sampleICalendars.get(2));
        cacheEntries.add(sampleICalendars.get(4));

        storageManager.saveEntries(storageEntries);
        cacheManager.saveEntries(cacheEntries);

        // Run the test
        cacheManager.updateCache();

        // Verify the results
        List<String> sampleICalendarsAsString = sampleICalendars.stream()
                .map(ical -> ical.write())
                .collect(Collectors.toList());

        List<String> cacheEntriesAsString = cacheManager.getEntriesAsStrings();
        String[] entries = (String[]) sampleICalendarsAsString.toArray();
        assertThat(cacheEntriesAsString).containsExactlyInAnyOrder(entries);
    }
}