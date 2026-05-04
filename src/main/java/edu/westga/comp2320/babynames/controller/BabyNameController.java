package edu.westga.comp2320.babynames.controller;

import edu.westga.comp2320.babynames.model.NameManager;
import edu.westga.comp2320.babynames.model.NameRecord;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BabyNameController {

    @FXML private TextField nameField;
    @FXML private TextField yearField;
    @FXML private TextField frequencyField;

    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;

    @FXML private ListView<String> listView;

    @FXML private ToggleGroup genderGroup;

    private NameManager manager = new NameManager();

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        updateList();
    }

    @FXML
    private void handleAdd() {
        try {
            String name = nameField.getText();
            String gender = maleRadio.isSelected() ? "M" : "F";
            int year = Integer.parseInt(yearField.getText());
            int freq = Integer.parseInt(frequencyField.getText());

            manager.addRecord(new NameRecord(name, gender, year, freq));
            updateList();
        } catch (Exception e) {
            showError("Invalid input");
        }
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
            String gender = maleRadio.isSelected() ? "M" : "F";
            Integer year = yearField.getText().isBlank() ? null : Integer.parseInt(yearField.getText());
            Integer freq = frequencyField.getText().isBlank() ? null : Integer.parseInt(frequencyField.getText());

            listView.getItems().clear();
            for (NameRecord r : manager.search(name, gender, year, freq)) {
                listView.getItems().add(r.toString());
            }
        } catch (Exception e) {
            showError("Invalid search input");
        }
    }

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