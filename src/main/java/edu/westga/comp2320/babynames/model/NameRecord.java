package edu.westga.comp2320.babynames.model;

/**
 * Represents a single baby name record with name, gender, year, and frequency.
 */
public class NameRecord {

    private String name;
    private String gender;
    private int year;
    private int frequency;

    /**
     * Creates a NameRecord.
     *
     * @param name the baby name
     * @param gender the gender (M or F)
     * @param year the year
     * @param frequency how often the name occurred
     */
    public NameRecord(String name, String gender, int year, int frequency) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Invalid gender");
        }
        if (year < 0 || frequency < 0) {
            throw new IllegalArgumentException("Year and frequency must be non-negative");
        }

        this.name = name;
        this.gender = gender;
        this.year = year;
        this.frequency = frequency;
    }

    /**
     * Gets the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the gender.
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Gets the year.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Gets the frequency.
     */
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.gender + ", " + this.year + "): " + this.frequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NameRecord)) {
            return false;
        }
        NameRecord other = (NameRecord) obj;
        return this.name.equalsIgnoreCase(other.name)
                && this.gender.equalsIgnoreCase(other.gender)
                && this.year == other.year;
    }

    @Override
    public int hashCode() {
        return (this.name.toLowerCase() + this.gender.toLowerCase() + this.year).hashCode();
    }
}