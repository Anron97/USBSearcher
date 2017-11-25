package USBSearcher.controller;

import USBSearcher.USBDevice.USBDevice;
import USBSearcher.logic.AlertWindow;
import USBSearcher.logic.USBSearcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class UsbSearcherController {
    @FXML
    private TableView<USBDevice> tableView;
    @FXML
    private TableColumn<USBDevice, String> systemNameColumn;
    @FXML
    private TableColumn<USBDevice, String> nameColumn;
    @FXML
    private TableColumn<USBDevice, String> sizeColumn;
    @FXML
    private TableColumn<USBDevice, String> freeSizeColumn;
    @FXML
    private TableColumn<USBDevice, String> busySizeColumn;
    @FXML
    private Button unmount;
    private USBSearcher searcher;

    private ObservableList<USBDevice> listDevices = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        systemNameColumn.setCellValueFactory(cellData -> cellData.getValue().systemNameProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        freeSizeColumn.setCellValueFactory(cellData -> cellData.getValue().freeSizeProperty());
        busySizeColumn.setCellValueFactory(cellData -> cellData.getValue().busySizeProperty());
        tableView.setItems(listDevices);
        unmount.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> {
                    if(newValue.getSystemName().contains("dev")) unmount.setDisable(false);
                    else unmount.setDisable(true);
                }));
    }

    public UsbSearcherController() {
        searcher = new USBSearcher(listDevices);
        searcher.start();
    }

    public void unmountDevice() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "echo "
                    + System.getenv("PASSWORD") + " | sudo -S eject " + tableView.getSelectionModel().getSelectedItem().getSystemName()});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            if (reader.readLine().contains("target is busy")) AlertWindow.showErrorAlert("Device is busy");
            else unmount.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        searcher.setWork(false);
    }
}
