package com.revelara.journal_app.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revelara.journal_app.entity.JournalEntry;
import com.revelara.journal_app.service.JournalEntryService;

/*
 * Rest Controller --> Service Layer --> Repository Layer
 * Entity --> Is a POJO class that represents a table in a database
 */

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/entries")
    public ResponseEntity<List<JournalEntry>> getJournalEntries() {
        try {
            List<JournalEntry> journalEntries = journalEntryService.getAllJournalEntries();
            if (journalEntries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/add-entry")
    public ResponseEntity<JournalEntry> addJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveJournalEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping("/entry/{id}")
    // public JournalEntry getEntryById(@PathVariable ObjectId id) {
    // return journalEntryService.getEntryById(id).orElse(null);
    // }

    @GetMapping("/entry/{id}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(id);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-entry/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id) {
        try {
            if (!journalEntryService.getEntryById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            journalEntryService.deleteEntryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/update-entry/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId id,
            @RequestBody JournalEntry journalEntry) {
        JournalEntry oldEntry = journalEntryService.getEntryById(id).orElse(null);
        if (oldEntry != null) {
            oldEntry.setContent(journalEntry.getContent() != null && journalEntry.getContent().length() > 0
                    ? journalEntry.getContent()
                    : oldEntry.getContent());
            oldEntry.setTitle(
                    journalEntry.getTitle() != null && journalEntry.getTitle().length() > 0 ? journalEntry.getTitle()
                            : oldEntry.getTitle());
            journalEntryService.saveJournalEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

/*
 * private Map<Long, JournalEntry> journalEntries = new HashMap<>();
 * 
 * @GetMapping("/entries")
 * public List<JournalEntry> getJournalEntries() {
 * return new ArrayList<>(journalEntries.values());
 * }
 * 
 * @GetMapping("/entry/{id}")
 * public JournalEntry getJournalEntry(@PathVariable long id) {
 * return journalEntries.get(id);
 * }
 * 
 * @PostMapping("/add-entry")
 * public boolean addJournalEntry(@RequestBody JournalEntry journalEntry) {
 * journalEntries.put(journalEntry.getId(), journalEntry);
 * return true;
 * }
 * 
 * @PutMapping("/update-entry/{id}")
 * public boolean updateJournalEntry(@PathVariable long id, @RequestBody
 * JournalEntry journalEntry) {
 * journalEntries.put(id, journalEntry);
 * return true;
 * }
 * 
 * @DeleteMapping("/delete-entry/{id}")
 * public boolean deleteJournalEntry(@PathVariable long id) {
 * journalEntries.remove(id);
 * return true;
 * }
 */