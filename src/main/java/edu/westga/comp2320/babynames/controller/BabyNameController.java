package edu.westga.comp2320.babynames.controller;

import edu.westga.comp2320.babynames.model.NameManager;
import edu.westga.comp2320.babynames.model.NameRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.Scanner;

/**
 * Controller for the Baby Name Statistics application.
 * Handles loading data, user interaction, and UI updates.
 */
public class BabyNameController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField frequencyField;

    @FXML
    private Label yearErrorLabel;

    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;

    @FXML
    private ListView<String> listView;

    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private Button deleteButton;

    private NameManager manager = new NameManager();

    /**
     * Initializes the UI and loads data from file.
     */
    @FXML
    public void initialize() {
        this.genderGroup = new ToggleGroup();

        this.maleRadio.setToggleGroup(this.genderGroup);
        this.femaleRadio.setToggleGroup(this.genderGroup);

        this.deleteButton.setDisable(true);

        this.listView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) ->
                        this.deleteButton.setDisable(newVal == null));

        this.loadDataFile();
        this.updateList();
    }

    /**
     * Loads initial data from CSV file.
     */
    private void loadDataFile() {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/data.csv");

            if (inputStream == null) {
                this.showError("Could not find data.csv");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    this.manager.addRecord(new NameRecord(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            Integer.parseInt(parts[3].trim())
                    ));
                }
            }

            reader.close();

        } catch (Exception e) {
            this.showError("Could not load data file");
        }
    }

    /**
     * Validates and returns year input.
     */
    private Integer getValidYear() {
        String text = this.yearField.getText();

        if (text == null || text.isBlank()) {
            this.yearErrorLabel.setText("");
            return null;
        }

        try {
            int year = Integer.parseInt(text);
            this.yearErrorLabel.setText("");
            return year;
        } catch (NumberFormatException e) {
            this.yearErrorLabel.setText("Enter a valid Year");
            return null;
        }
    }

    /**
     * Adds a new record.
     */
    @FXML
    private void handleAdd() {
        String name = this.nameField.getText();

        if (name == null || name.isBlank()) {
            this.showError("Name is required");
            return;
        }

        if (!this.maleRadio.isSelected() && !this.femaleRadio.isSelected()) {
            this.showError("Select a gender");
            return;
        }

        String gender = this.maleRadio.isSelected() ? "M" : "F";

        Integer year = this.getValidYear();
        if (year == null) {
            return;
        }

        int freq;

        try {
            freq = Integer.parseInt(this.frequencyField.getText());
        } catch (NumberFormatException e) {
            this.showError("Frequency must be a number");
            return;
        }

        this.manager.addRecord(new NameRecord(name, gender, year, freq));
        this.updateList();
    }

    /**
     * Deletes selected record.
     */
    @FXML
    private void handleDelete() {
        int index = this.listView.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            NameRecord record = this.manager.getAllRecords().get(index);
            this.manager.deleteRecord(record);
            this.updateList();
        }
    }

    /**
     * Deletes all records.
     */
    @FXML
    private void handleDeleteAll() {
        this.manager.deleteAll();
        this.updateList();
    }

    /**
     * Searches records.
     */
    @FXML
    private void handleSearch() {
        try {
            String name = this.nameField.getText();

            String gender = null;
            if (this.maleRadio.isSelected()) {
                gender = "M";
            } else if (this.femaleRadio.isSelected()) {
                gender = "F";
            }

            Integer year = this.getValidYear();

            Integer freq = this.frequencyField.getText().isBlank()
                    ? null
                    : Integer.parseInt(this.frequencyField.getText());

            this.listView.getItems().clear();

            for (NameRecord r : this.manager.search(name, gender, year, freq)) {
                this.listView.getItems().add(r.toString());
            }

        } catch (Exception e) {
            this.showError("Invalid input");
        }
    }

    /**
     * Clears all input fields.
     */
    @FXML
    private void handleClear() {
        this.nameField.clear();
        this.yearField.clear();
        this.frequencyField.clear();
        this.genderGroup.selectToggle(null);
        this.yearErrorLabel.setText("");
        this.updateList();
    }

    /**
     * Shows top 3 male and female names.
     */
    @FXML
    private void handleTopNames() {
        Integer year = this.getValidYear();

        if (year == null) {
            return;
        }

        this.listView.getItems().clear();

        this.listView.getItems().add("Top Female Names:");
        for (NameRecord r : this.manager.getTop3ByGenderAndYear("F", year)) {
            this.listView.getItems().add(r.toString());
        }

        this.listView.getItems().add("");

        this.listView.getItems().add("Top Male Names:");
        for (NameRecord r : this.manager.getTop3ByGenderAndYear("M", year)) {
            this.listView.getItems().add(r.toString());
        }
    }

    /**
     * Saves records to file.
     */
    @FXML
    private void handleSave() {
        try {
            File file = new File("data.csv");
            FileWriter writer = new FileWriter(file);

            for (NameRecord r : this.manager.getAllRecords()) {
                writer.write(r.getName() + "," +
                        r.getGender() + "," +
                        r.getYear() + "," +
                        r.getFrequency() + "\n");
            }

            writer.close();
            this.showError("Saved successfully");

        } catch (Exception e) {
            this.showError("Error saving file");
        }
    }

    /**
     * Loads records from file.
     */
    @FXML
    private void handleOpen() {
        try {
            File file = new File("data.csv");

            if (!file.exists()) {
                this.showError("No saved file found");
                return;
            }

            this.manager.deleteAll();

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");

                if (parts.length == 4) {
                    this.manager.addRecord(new NameRecord(
                            parts[0],
                            parts[1],
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3])
                    ));
                }
            }

            scanner.close();
            this.updateList();

        } catch (Exception e) {
            this.showError("Error loading file");
        }
    }

    /**
     * Shows about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Baby Name Statistics");
        alert.setContentText("Created by Cason Robins");
        alert.showAndWait();
    }

    /**
     * Updates list view.
     */
    private void updateList() {
        this.listView.getItems().clear();

        for (NameRecord r : this.manager.getAllRecords()) {
            this.listView.getItems().add(r.toString());
        }
    }

    /**
     * Shows error message.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}