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
     * Creates a NameRecord object.
     *
     * @param name the baby name
     * @param gender the gender (M or F)
     * @param year the year of the record
     * @param frequency how often the name occurred
     * @throws IllegalArgumentException if inputs are invalid
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
     *
     * @return the baby name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the gender.
     *
     * @return the gender (M or F)
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Gets the year.
     *
     * @return the year of the record
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Gets the frequency.
     *
     * @return how often the name occurred
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Returns a string representation of this record.
     *
     * @return formatted baby name record
     */
    @Override
    public String toString() {
        return this.name + " (" + this.gender + ", " + this.year + "): " + this.frequency;
    }

    /**
     * Checks equality based on name, gender, and year.
     *
     * @param obj object to compare
     * @return true if equal, false otherwise
     */
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

    /**
     * Generates hash code for this record.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return (this.name.toLowerCase()
                + this.gender.toLowerCase()
                + this.year).hashCode();
    }
}