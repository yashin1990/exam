package it.polito.po.utility;

import java.util.Optional;

public class MeterEntity implements Meter {
    private String id;
    private String serialNumber;
    private String brand;
    private String model;
    private String unit;
    private ServicePoint servicePoint;

    public MeterEntity(String id, String serialNumber, String brand, String model, String unit) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.brand = brand;
        this.model = model;
        this.unit = unit;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSN() {
        return serialNumber;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public Optional<ServicePoint> getServicePoint() {
        return Optional.ofNullable(servicePoint);
    }

    public void setServicePoint(ServicePoint servicePoint) {
        this.servicePoint = servicePoint;
    }
}
