package com.revelara.journal_app.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revelara.journal_app.entity.JournalEntry;
import com.revelara.journal_app.repository.JournalEntryRepository;

@Component
public class JournalEntryService {
    
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveJournalEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        String idString = id.toString();
        return journalEntryRepository.findById(idString);
    }

    public void deleteEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id.toString());
    }


}
