package io.pivotal.pal.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    @Autowired
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry ) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry){
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);

    }
    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id){
        TimeEntry timeEntry = timeEntryRepository.find(id);
        if(timeEntry != null){
            actionCounter.increment();
        return new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        }
        return new ResponseEntity<TimeEntry>( HttpStatus.NOT_FOUND);

    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        actionCounter.increment();
        return new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(),HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long id,@RequestBody TimeEntry timeEntry){
        TimeEntry timeEntryUpdate = timeEntryRepository.update(id, timeEntry);
        if(timeEntryUpdate != null) {
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(timeEntryUpdate, HttpStatus.OK);
        }
        return new ResponseEntity<TimeEntry>( HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        timeEntryRepository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
