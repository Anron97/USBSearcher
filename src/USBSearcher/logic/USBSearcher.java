package USBSearcher.logic;

import USBSearcher.USBDevice.USBDevice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class USBSearcher extends Thread{

    private ObservableList<USBDevice> listDevices;
    private ObservableList<USBDevice> tempListDevices = FXCollections.observableArrayList();
    private boolean work = true;

    public USBSearcher(ObservableList<USBDevice> listDevices) {
        this.listDevices = listDevices;
    }

    @Override
    public void run() {
        while (work) {
            getUSBDevices();
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUSBDevices() {
        try {
            ProcessBuilder bash = new ProcessBuilder("bash", "-c", "df -h | grep '/media'");
            Process process = bash.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String parseLine;
            tempListDevices.clear();
            while ((parseLine = reader.readLine()) != null) {
                USBDevice device = parseUsbDevice(parseLine);
                tempListDevices.add(device);
                if (!listDevices.contains(device)) listDevices.add(device);
            }
            listDevices.retainAll(tempListDevices);
            System.out.println(listDevices.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private USBDevice parseUsbDevice(String parceLine) {
        String name = parceLine.substring(parceLine.lastIndexOf("/")+1);
        String[] attributes = parceLine.replaceAll("\\s+", " ").split(" ");
        return new USBDevice(attributes[0], name, attributes[1], attributes[3], attributes[2]);
    }

    public void setWork(boolean work) {
        this.work = work;
    }

}
