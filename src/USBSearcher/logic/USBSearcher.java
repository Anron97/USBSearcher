package USBSearcher.logic;

import USBSearcher.USBDevice.USBDevice;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class USBSearcher extends Thread {

    private ObservableList<USBDevice> listDevices;
    private ArrayList<USBDevice> tempListDevices = new ArrayList<>();
    private boolean work = true;

    public USBSearcher(ObservableList<USBDevice> listDevices) {
        this.listDevices = listDevices;
    }

    @Override
    public void run() {
        while (work) {

            try {
                getUSBDevices();
                getMTPDevices();
                sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getUSBDevices() throws IOException {
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
    }

    public void getMTPDevices() throws IOException {
        Process process = Runtime.getRuntime().exec("mtp-detect");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("@")) {
                String systemName = "/run/user/1000/gvfs/mtp:host=%5Busb%3A00" +
                        line.substring(line.indexOf('@') + 2).split(", ")[0].split(" ")[1] + "%2C0" +
                        line.substring(line.indexOf('@') + 2).split(", ")[1].split(" ")[1] + "%5D";
                USBDevice device = new USBDevice(systemName, "", "", "", "");
                String parseDeviceLine;
                while ((parseDeviceLine = reader.readLine()) != null) {
                    if (parseDeviceLine.contains("Model")) device.setName(parseDeviceLine.split(": ")[1]);
                    if (parseDeviceLine.contains("MaxCapacity")) {
                        double allSize = Double.parseDouble(parseDeviceLine.split(": ")[1]) / 1024 / 1024 / 1024;
                        double freeSize = Double.parseDouble(reader.readLine().split(": ")[1]) / 1024 / 1024 / 1024;
                        double busySize = allSize - freeSize;
                        device.setSize(String.format("%.2f", allSize) + " GB");
                        device.setFreeSize(String.format("%.2f", freeSize) + " GB");
                        device.setBusySize(String.format("%.2f", busySize) + " GB");
                        break;
                    }
                }
                if (device.isCorrect()) tempListDevices.add(device);
                if (!listDevices.contains(device) && device.isCorrect()) listDevices.add(device);

            }
        }
        listDevices.retainAll(tempListDevices);
    }


    private USBDevice parseUsbDevice(String parceLine) {
        String name = parceLine.substring(parceLine.lastIndexOf("/") + 1);
        String[] attributes = parceLine.replaceAll("\\s+", " ").split(" ");
        return new USBDevice(attributes[0], name, attributes[1], attributes[3], attributes[2]);
    }

    public void setWork(boolean work) {
        this.work = work;
    }

}
