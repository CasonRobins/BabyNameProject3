package edu.westga.comp2320.babynames.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of NameRecord objects.
 */
public class NameManager {

    private List<NameRecord> records;

    /**
     * Creates a new NameManager.
     */
    public NameManager() {
        this.records = new ArrayList<>();
    }

    /**
     * Adds a record if it does not already exist.
     */
    public void addRecord(NameRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null");
        }

        if (!this.records.contains(record)) {
            this.records.add(record);
        }
    }

    /**
     * Deletes a specific record.
     */
    public void deleteRecord(NameRecord record) {
        this.records.remove(record);
    }

    /**
     * Deletes all records.
     */
    public void deleteAll() {
        this.records.clear();
    }

    /**
     * Returns all records sorted.
     */
    public List<NameRecord> getAllRecords() {
        return this.records.stream()
                .sorted(getComparator())
                .collect(Collectors.toList());
    }

    /**
     * Searches records based on optional fields.
     */
    public List<NameRecord> search(String name, String gender, Integer year, Integer frequency) {
        return this.records.stream()
                .filter(r -> name == null || name.isBlank() || r.getName().equalsIgnoreCase(name))
                .filter(r -> gender == null || gender.isBlank() || r.getGender().equalsIgnoreCase(gender))
                .filter(r -> year == null || r.getYear() == year)
                .filter(r -> frequency == null || r.getFrequency() == frequency)
                .sorted(getComparator())
                .collect(Collectors.toList());
    }

    /**
     * Sorting logic required by the project.
     */
    private Comparator<NameRecord> getComparator() {
        return Comparator
                .comparing(NameRecord::getYear).reversed()
                .thenComparing(r -> r.getGender().equalsIgnoreCase("F") ? 0 : 1)
                .thenComparing(NameRecord::getFrequency).reversed()
                .thenComparing(NameRecord::getName);
    }
}