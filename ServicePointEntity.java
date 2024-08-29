package it.polito.po.utility;

import java.util.Optional;

public class ServicePointEntity implements ServicePoint {
    private String id;
    private String municipality;
    private String address;
    private Point position;
    private Meter meter;

    public ServicePointEntity(String id, String municipality, String address, Point position) {
        this.id = id;
        this.municipality = municipality;
        this.address = address;
        this.position = position;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getMunicipality() {
        return municipality;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public Optional<Meter> getMeter() {
        return Optional.ofNullable(meter);
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }
}
