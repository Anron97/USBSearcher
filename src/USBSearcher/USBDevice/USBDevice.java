package USBSearcher.USBDevice;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class USBDevice {
    private StringProperty systemName;
    private StringProperty name;
    private StringProperty size;
    private StringProperty freeSize;
    private StringProperty busySize;

    public USBDevice(String systemName, String name, String size, String freeSize, String busySize) {
        this.systemName = new SimpleStringProperty(systemName);
        this.name = new SimpleStringProperty(name);
        this.size = new SimpleStringProperty(size);
        this.freeSize = new SimpleStringProperty(freeSize);
        this.busySize = new SimpleStringProperty(busySize);
    }

    public String getSystemName() {
        return systemName.get();
    }

    public StringProperty systemNameProperty() {
        return systemName;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public String getFreeSize() {
        return freeSize.get();
    }

    public StringProperty freeSizeProperty() {
        return freeSize;
    }

    public String getBusySize() {
        return busySize.get();
    }

    public StringProperty busySizeProperty() {
        return busySize;
    }

    public void setSystemName(String systemName) {
        this.systemName.set(systemName);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public void setFreeSize(String freeSize) {
        this.freeSize.set(freeSize);
    }

    public void setBusySize(String busySize) {
        this.busySize.set(busySize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        USBDevice usbDevice = (USBDevice) o;

        return name.toString().equals(usbDevice.name.toString());
    }

    @Override
    public int hashCode() {
        return name.toString().hashCode();
    }
}
