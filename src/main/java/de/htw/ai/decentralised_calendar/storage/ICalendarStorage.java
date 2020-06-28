package de.htw.ai.decentralised_calendar.storage;

import biweekly.ICalendar;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;


/**
 * @author Behzad Karimi on 2019-06-24.
 * @project decentralised_calendar
 */
public interface ICalendarStorage {

    /**
     * @return a list of ICalendar data
     */
    List<String> getEntriesAsStrings();

    /**
     * @return a list of files from the storage
     */
    List<File> getEntriesAsFiles();


    /**
     * @return a list of ICalendar data as an ICalendar string
     */
    String getEntriesAsOneString();

    /**
     * @param iCalendar the data to save
     * @return relative path of saved .ics file
     */
    String saveEntry(ICalendar iCalendar);

    /**
     * @param entries of ICalendar objects to save
     * @return a list of filepath
     */
    List<String> saveEntries(Collection<ICalendar> entries);

    /**
     * @param filename of the .ics file that should be parsed to an ICalendar object
     * @return returning the ICalendar object as String
     */
    String getEntry(String filename);

    /**
     * @return number of appointments as integer
     */
    int getNumberOfAppointments();


    /**
     * splitting .ics files if there are more than one VCALENDAR entries
     */
    void splitEntriesToDifferentFiles();


    /**
     * deleting entry by the given filename
     *
     * @param filename of file to delete
     */
    void deleteEntry(String filename);

    /**
     * @return a list of all filepaths in the directory
     */
    List<String> getFilePaths();


    /**
     * @return a list of all filename in the directory
     */
    List<String> getFileNames();


    /**
     * deleting all entries in storage
     */
    void clearStorage();

    /**
     * @return the path as a string
     */
    String getPath();

    /**
     * @return the URI
     */
    URI getURI();

    /**
     * @return the parent storage in case of this ist the CacheManager. If its the IStorageManager, than it returns himself.
     */
    ICalendarStorage getStorage();

//    /**
//     * Default behaviour of ASAPEngine: Each peer / device
//     * gets its own chunk storage. That storage is filled during asap
//     * synchronization. That storage can be retrieved with this command.
//     *
//     * @param sender
//     * @return
//     */
//    public ASAPChunkStorage getIncomingChunkStorage(CharSequence sender);
}
