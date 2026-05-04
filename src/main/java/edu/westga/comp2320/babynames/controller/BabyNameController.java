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

        int year;
        int freq;

        try {
            year = Integer.parseInt(yearField.getText());
            freq = Integer.parseInt(frequencyField.getText());
        } catch (NumberFormatException e) {
            showError("Year and Frequency must be numbers");
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

            if (maleRadio.isSelected()) {
                gender = "M";
            } else if (femaleRadio.isSelected()) {
                gender = "F";
            }

            Integer year = yearField.getText().isBlank() ? null : Integer.parseInt(yearField.getText());
            Integer freq = frequencyField.getText().isBlank() ? null : Integer.parseInt(frequencyField.getText());

            listView.getItems().clear();

            for (NameRecord record : manager.search(name, gender, year, freq)) {
                listView.getItems().add(record.toString());
            }

        } catch (NumberFormatException e) {
            showError("Year and Frequency must be numbers");
        }
    }

    @FXML
    private void handleClear() {
        nameField.clear();
        yearField.clear();
        frequencyField.clear();
        genderGroup.selectToggle(null);
        updateList();
    }

    private void updateList() {
        listView.getItems().clear();
        for (NameRecord record : manager.getAllRecords()) {
            listView.getItems().add(record.toString());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}