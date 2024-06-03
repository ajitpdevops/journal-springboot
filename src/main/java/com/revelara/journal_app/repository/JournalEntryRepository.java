package com.revelara.journal_app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.revelara.journal_app.entity.JournalEntry;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {

}
