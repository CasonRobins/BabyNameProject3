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

    public NameManager() {
        this.records = new ArrayList<>();
    }

    public void addRecord(NameRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Record cannot be null");
        }

        if (!this.records.contains(record)) {
            this.records.add(record);
        }
    }

    public void deleteRecord(NameRecord record) {
        this.records.remove(record);
    }

    public void deleteAll() {
        this.records.clear();
    }

    public List<NameRecord> getAllRecords() {
        return this.records.stream()
                .sorted(getComparator())
                .collect(Collectors.toList());
    }

    public List<NameRecord> search(String name, String gender, Integer year, Integer frequency) {
        return this.records.stream()

                .filter(r -> name == null || name.isBlank()
                        || r.getName().toLowerCase().contains(name.toLowerCase()))

                .filter(r -> gender == null || gender.isBlank()
                        || r.getGender().equalsIgnoreCase(gender))

                .filter(r -> year == null || r.getYear() == year)

                .filter(r -> frequency == null || r.getFrequency() == frequency)

                .sorted(getComparator())
                .collect(Collectors.toList());
    }

    /**
     * PART 2 FEATURE:
     * Returns top 3 names for a gender in a specific year.
     */
    public List<NameRecord> getTop3ByGenderAndYear(String gender, int year) {
        return this.records.stream()
                .filter(r -> r.getGender().equalsIgnoreCase(gender))
                .filter(r -> r.getYear() == year)
                .sorted(Comparator.comparing(NameRecord::getFrequency).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    private Comparator<NameRecord> getComparator() {
        return Comparator
                .comparing(NameRecord::getYear).reversed()
                .thenComparing(r -> r.getGender().equalsIgnoreCase("F") ? 0 : 1)
                .thenComparing(NameRecord::getFrequency).reversed()
                .thenComparing(NameRecord::getName);
    }
}