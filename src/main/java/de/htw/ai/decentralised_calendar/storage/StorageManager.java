package de.htw.ai.decentralised_calendar.storage;

import biweekly.Biweekly;
import biweekly.ICalendar;
import de.htw.ai.decentralised_calendar.request.RequestLog;
import de.htw.ai.decentralised_calendar.request.RequestManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * @author Behzad Karimi on 2019-06-24.
 * @project decentralised_calendar
 */

// TODO: Filenamechecker

public class StorageManager implements ICalendarStorage {

    private static final Logger LOG = Logger.getLogger(StorageManager.class.getName());

    protected final String PATH;
    protected final URI URI;
    protected final int id;
    protected RequestManager requestManager;
    protected RequestLog requestLog;


    public StorageManager(final String pathToStorage, final int id) {
        this.PATH = pathToStorage;
        this.URI = null;
        this.id = id;
        this.requestLog = RequestLog.getInstance();
    }


    public StorageManager(final String pathToStorage) {
        this.PATH = pathToStorage;
        this.URI = null;
        final Random random = new Random();
        this.id = random.nextInt(10000);
        this.requestLog = RequestLog.getInstance();
    }


    public StorageManager(final URI uriForStorage) {
        this.PATH = uriForStorage.toString();
        this.URI = uriForStorage;
        final Random random = new Random();
        this.id = random.nextInt(10000);
        this.requestLog = RequestLog.getInstance();
    }


    public RequestLog getRequestLog() {
        return this.requestLog;
    }


    public RequestManager getRequestManager() {
        return this.requestManager;
    }


    public void setRequestManager(final RequestManager requestManager) {
        this.requestManager = requestManager;
    }


    @Override
    public synchronized List<String> getEntriesAsStrings() {
        final List<String> fileNames = this.getFilePaths();
        return this.iCalStringsToList(fileNames);
    }


    public List<String> iCalStringsToList(final List<String> fileNames) {
        final ArrayList<String> iCalList = new ArrayList<>();
        for (final String fileName : fileNames) {
            if (fileName.endsWith(".ics")) {
                final String entry = this.getEntry(fileName);
                iCalList.add(entry);
            }
        }
        return iCalList;
    }


    @Override
    public synchronized List<File> getEntriesAsFiles() {
        final List<File> fileList = this.getFilePaths().stream()
                .map(File::new)
                .filter(file -> ! file.isDirectory())
                .collect(Collectors.toList());
        return fileList;
    }


    @Override
    public synchronized String getEntriesAsOneString() {
//        final List<String> fileNames = this.getFilePaths();
        final List<String> fileNames = this.getFileNames();
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String fileName : fileNames) {
            if (fileName.endsWith(".ics")) {
                final String entry = this.getEntry(fileName);
                stringBuilder.append(entry);
            }
        }
        return stringBuilder.toString();
    }


    @Override
    public synchronized String saveEntry(final ICalendar iCalendar) {
//        String name = iCalendar.getNames().get(0).getValue();
//        name = name.replaceAll(" ", "_");
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        final LocalDateTime now = LocalDateTime.now();
        final String time = now.format(dateTimeFormatter);
//        final StringBuilder stringBuilder = new StringBuilder(this.PATH);
//        stringBuilder.append(name)
//                .append("_")
//                .append(time)
//                .append(".ics");
//        final String filename = stringBuilder.toString();
        final String filename = this.PATH + iCalendar.getUid().getValue() + ".ics";
        final File file = new File(filename);
//        if (! file.exists()) {
//            final String entryString = Biweekly.write(iCalendar).go();
//            final CreateFile createOperation = new CreateFile(entryString);
//            final Request request = new Request(this.id, filename, null, createOperation);
//            this.requestLog.addRequest(request);
//        }
        try {
            final Date modified = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            if (iCalendar.getLastModified() == null) {
                iCalendar.setLastModified(modified);
            }
            Biweekly.write(iCalendar).go(file);
            LOG.log(Level.FINE, "Saving new ICalendar File.");
            return file.getName();
        } catch (final IOException e) {
            LOG.log(Level.SEVERE, "Could not save.");
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public synchronized List<String> saveEntries(final Collection<ICalendar> entries) {
        final ArrayList<String> filenameList = new ArrayList<>();
        for (final ICalendar entry : entries) {
            final String filename = this.saveEntry(entry);
            filenameList.add(filename);
        }
        return filenameList;
    }


    @Override
    public synchronized String getEntry(final String filename) {
//        final File fileToRead = new File(filename);
        final File fileToRead = new File(this.PATH + filename);
        final List<ICalendar> all;
        try {
            all = Biweekly.parse(fileToRead).all();
        } catch (final IOException e) {
            LOG.log(Level.SEVERE, "Could not read .ics file {0}", filename);
            e.printStackTrace();
            return null;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (final ICalendar iCalendar : all) {
            stringBuilder.append(iCalendar.write());
        }
        return stringBuilder.toString();
    }


    //    TODO: maybe it is optimizable
    @Override
    public synchronized int getNumberOfAppointments() {
        final List<String> fileNames = this.getFilePaths();
        return fileNames.size();
    }


    @Override
    public synchronized void splitEntriesToDifferentFiles() {
        final List<String> fileNames = this.getFilePaths();
        for (final String fileName : fileNames) {
            final File fileToRead = new File(fileName);
            List<ICalendar> allEntries = null;
            try {
                allEntries = Biweekly.parse(fileToRead).all();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            if (allEntries.size() > 1) {
                for (final ICalendar iCal : allEntries) {
                    this.saveEntry(iCal);
                }
                this.deleteEntry(fileName);
            }
        }
    }


    @Override
    public synchronized void deleteEntry(final String filename) {
        final File file = new File(filename);
        if (file.isFile()) {
            file.delete();
        }
    }


    @Override
    public synchronized List<String> getFilePaths() {
        final File[] files = new File(this.PATH).listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        return this.filePathCreater(files);
    }


    protected List<String> filePathCreater(final File[] files) {
        final List<String> filepaths = Arrays.stream(files)
                .map(File::getPath)
                .collect(Collectors.toList());

        return filepaths;
    }


    @Override
    public synchronized List<String> getFileNames() {
        final File[] files = new File(this.PATH).listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        return this.fileNameCreater(files);
    }


    protected List<String> fileNameCreater(final File[] files) {
        final List<String> filenames = Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());

        return filenames;
    }


    @Override
    public void clearStorage() {
        final List<String> fileNames = this.getFilePaths();
        for (final String fileName : fileNames) {
            this.deleteEntry(fileName);
        }
    }


    @Override
    public String getPath() {
        return this.PATH;
    }


    @Override
    public java.net.URI getURI() {
        return this.URI;
    }


    @Override
    public ICalendarStorage getStorage() {
        return this;
    }


    public int getId() {
        return this.id;
    }


}
