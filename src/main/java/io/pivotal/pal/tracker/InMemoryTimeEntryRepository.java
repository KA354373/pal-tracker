package io.pivotal.pal.tracker;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private HashMap<Long,TimeEntry> timeEntryHashMap = new HashMap<Long,TimeEntry>();

    private  Long id = 0l;

    public TimeEntry create(TimeEntry timeEntry){
      ++id;
        TimeEntry newTimeEntry = new TimeEntry(
                id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );
        System.out.println("id"+ id);
        timeEntryHashMap.put(id, newTimeEntry);
        return newTimeEntry;
    }

    public TimeEntry  find(Long timeEntryId){
        return  timeEntryHashMap.get(timeEntryId);
    }

    public List<TimeEntry> list(){
        return new ArrayList<TimeEntry>(timeEntryHashMap.values());
    }

    public TimeEntry update(Long timeEntryId, TimeEntry timeEntry){
        TimeEntry updatedEntry = null;
        if(timeEntryHashMap.get(timeEntryId) != null ){

            updatedEntry =  new TimeEntry(
                    id,
                    timeEntry.getProjectId(),
                    timeEntry.getUserId(),
                    timeEntry.getDate(),
                    timeEntry.getHours()
            );

            timeEntryHashMap.replace(id, updatedEntry);
        }
        return updatedEntry;
    }

    public void delete(Long timeEntryId){
        timeEntryHashMap.remove(timeEntryId);
    }
}