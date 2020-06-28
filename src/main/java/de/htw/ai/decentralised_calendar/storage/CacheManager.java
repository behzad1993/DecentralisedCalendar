package de.htw.ai.decentralised_calendar.storage;

import biweekly.Biweekly;
import biweekly.ICalendar;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class CacheManager extends StorageManager {

    // calendar storage from where the cache is build from
    private final ICalendarStorage storage;
    private final URI uriReceivedCache;


    public CacheManager(String path, ICalendarStorage storage) throws URISyntaxException {
        super(path);
        this.storage = storage;
        this.uriReceivedCache = new URI(this.storage.getPath() + "received/");
    }


    public synchronized String copyFileToCache(File source) throws IOException {
        String filename = source.getName();
        File target = new File(this.PATH + filename);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
        return target.getPath();
    }


    public synchronized boolean filenameExistsInCache(String filename) {
        List<String> fileNames = this.getFilePaths();
        return fileNames.contains(filename);
    }


    public synchronized boolean fileExistsInCache(File file) {
        if (! this.filenameExistsInCache(file.getPath())) {
            return false;
        }
        String toCheck = this.getEntry(file.getPath());
        String inCache = this.getEntry(this.PATH + file.getName());
        return toCheck.equals(inCache);
    }


    public synchronized void updateCache() {
        List<String> storageEntries = this.storage.getEntriesAsStrings();
        List<String> cacheEntries = this.getEntriesAsStrings();
        List<String> updatedEntries = Synchronizer.syncDevices(storageEntries, cacheEntries);
        this.splitEntriesToDifferentFiles();
        List<ICalendar> iCalendarList = updatedEntries.stream()
                .map(ical -> Biweekly.parse(ical).first())
                .collect(Collectors.toList());
        this.clearStorage();
        this.saveEntries(iCalendarList);
    }


    public List<String> getReceivedEntriesAsString() {
        URI uriOfReceivedCache = getUriOfReceivedCache();
        File[] files = new File(uriOfReceivedCache.toString()).listFiles();
        List<String> receivedEntries = this.filePathCreater(files);
        return receivedEntries;
    }


    @Override
    public ICalendarStorage getStorage() {
        return this.storage;
    }


    public URI getUriOfReceivedCache() {
        return this.uriReceivedCache;
    }
}
