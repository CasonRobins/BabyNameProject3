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
     *
     * @param record the record to add
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
     *
     * @param record the record to delete
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
     *
     * @return sorted list of NameRecord objects
     */
    public List<NameRecord> getAllRecords() {
        return this.records.stream()
                .sorted(this.getComparator())
                .collect(Collectors.toList());
    }

    /**
     * Searches records based on optional filters.
     *
     * @param name partial name filter
     * @param gender gender filter
     * @param year year filter
     * @param frequency frequency filter
     * @return filtered list of records
     */
    public List<NameRecord> search(String name, String gender, Integer year, Integer frequency) {
        return this.records.stream()

                .filter(r -> name == null || name.isBlank()
                        || r.getName().toLowerCase().contains(name.toLowerCase()))

                .filter(r -> gender == null || gender.isBlank()
                        || r.getGender().equalsIgnoreCase(gender))

                .filter(r -> year == null || r.getYear() == year)

                .filter(r -> frequency == null || r.getFrequency() == frequency)

                .sorted(this.getComparator())
                .collect(Collectors.toList());
    }

    /**
     * Returns top 3 names for a gender in a specific year.
     *
     * @param gender gender to filter
     * @param year year to filter
     * @return top 3 records
     */
    public List<NameRecord> getTop3ByGenderAndYear(String gender, int year) {
        return this.records.stream()
                .filter(r -> r.getGender().equalsIgnoreCase(gender))
                .filter(r -> r.getYear() == year)
                .sorted(Comparator.comparing(NameRecord::getFrequency).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Comparator used for sorting records.
     *
     * @return comparator for NameRecord
     */
    private Comparator<NameRecord> getComparator() {
        return Comparator
                .comparing(NameRecord::getYear).reversed()
                .thenComparing(r -> r.getGender().equalsIgnoreCase("F") ? 0 : 1)
                .thenComparing(NameRecord::getFrequency).reversed()
                .thenComparing(NameRecord::getName);
    }
}