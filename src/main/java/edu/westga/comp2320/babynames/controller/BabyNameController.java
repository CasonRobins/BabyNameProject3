package edu.westga.comp2320.babynames.controller;

import edu.westga.comp2320.babynames.model.NameManager;
import edu.westga.comp2320.babynames.model.NameRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BabyNameController {

    @FXML private TextField nameField;
    @FXML private TextField yearField;
    @FXML private TextField frequencyField;

    @FXML private Label yearErrorLabel;

    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;

    @FXML private ListView<String> listView;

    @FXML private ToggleGroup genderGroup;
    @FXML private Button deleteButton;

    private NameManager manager = new NameManager();

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        deleteButton.setDisable(true);

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteButton.setDisable(newVal == null);
        });

        loadDataFile();
        updateList();
    }

    // ---------------- FILE LOADING ----------------

    private void loadDataFile() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/data.csv");

            if (inputStream == null) {
                showError("Could not find data.csv");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String gender = parts[1].trim();
                    int year = Integer.parseInt(parts[2].trim());
                    int frequency = Integer.parseInt(parts[3].trim());

                    manager.addRecord(new NameRecord(name, gender, year, frequency));
                }
            }

            reader.close();

        } catch (Exception e) {
            showError("Could not load data file");
        }
    }

    // ---------------- VALIDATION ----------------

    private Integer getValidYear() {
        String text = yearField.getText();

        if (text == null || text.isBlank()) {
            yearErrorLabel.setText("");
            return null;
        }

        try {
            int year = Integer.parseInt(text);
            yearErrorLabel.setText("");
            return year;
        } catch (NumberFormatException e) {
            yearErrorLabel.setText("Enter a valid Year");
            return null;
        }
    }

    // ---------------- BUTTONS ----------------

    @FXML
    private void handleAdd() {
        String name = nameField.getText();

        if (name == null || name.isBlank()) {
            showError("Name is required");
            return;
        }

        if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
            showError("Select a gender");
            return;
        }

        String gender = maleRadio.isSelected() ? "M" : "F";

        Integer year = getValidYear();
        if (year == null) return;

        int freq;

        try {
            freq = Integer.parseInt(frequencyField.getText());
        } catch (NumberFormatException e) {
            showError("Frequency must be a number");
            return;
        }

        if (year < 0 || freq < 0) {
            showError("Year and Frequency must be positive");
            return;
        }

        manager.addRecord(new NameRecord(name, gender, year, freq));
        updateList();
    }

    @FXML
    private void handleDelete() {
        int index = listView.getSelectionModel().getSelectedIndex();

        if (index >= 0) {
            NameRecord record = manager.getAllRecords().get(index);
            manager.deleteRecord(record);
            updateList();
        }
    }

    @FXML
    private void handleDeleteAll() {
        manager.deleteAll();
        updateList();
    }

    @FXML
    private void handleSearch() {
        try {
            String name = nameField.getText();

            String gender = null;
            if (maleRadio.isSelected()) gender = "M";
            else if (femaleRadio.isSelected()) gender = "F";

            Integer year = getValidYear();
            Integer freq = frequencyField.getText().isBlank()
                    ? null
                    : Integer.parseInt(frequencyField.getText());

            listView.getItems().clear();

            for (NameRecord r : manager.search(name, gender, year, freq)) {
                listView.getItems().add(r.toString());
            }

        } catch (NumberFormatException e) {
            showError("Invalid number input");
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        yearField.clear();
        frequencyField.clear();
        genderGroup.selectToggle(null);
        yearErrorLabel.setText("");
        updateList();
    }

    @FXML
    private void handleTopNames() {
        try {
            Integer year = getValidYear();
            if (year == null) return;

            listView.getItems().clear();

            listView.getItems().add("Top Female Names:");
            for (NameRecord r : manager.getTop3ByGenderAndYear("F", year)) {
                listView.getItems().add(r.toString());
            }

            listView.getItems().add("");

            listView.getItems().add("Top Male Names:");
            for (NameRecord r : manager.getTop3ByGenderAndYear("M", year)) {
                listView.getItems().add(r.toString());
            }

        } catch (Exception e) {
            showError("Year must be valid");
        }
    }

    // ---------------- MENU METHODS (FIXED) ----------------

    @FXML
    private void handleOpen() {
        showError("Open not required for submission demo (or implement later)");
    }

    @FXML
    private void handleSave() {
        showError("Save not required for submission demo (or implement later)");
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Baby Name Statistics");
        alert.setContentText("Created by Cason Robins");
        alert.showAndWait();
    }

    // ---------------- HELPERS ----------------

    private void updateList() {
        listView.getItems().clear();
        for (NameRecord r : manager.getAllRecords()) {
            listView.getItems().add(r.toString());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}