package edu.westga.comp2320.babynames.model;

public class NameRecord {
    private String name;
    private String gender;
    private int year;
    private int frequency;

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

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public int getYear() {
        return this.year;
    }

    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.gender + ", " + this.year + "): " + this.frequency;
    }

    @Override
    public boolean equals(Object obj) {
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
        return this.name.toLowerCase().hashCode()
                + this.gender.toLowerCase().hashCode()
                + this.year;
    }
}