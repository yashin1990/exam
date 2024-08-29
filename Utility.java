package it.polito.po.utility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Represents the facade class for the utility company.
 */
public class Utility {

    public TreeMap<String, ServicePoint> servicePoints = new TreeMap<>();
    public TreeMap<String, Meter> meters = new TreeMap<>();
    public TreeMap<String, User> users = new TreeMap<>();
    public TreeMap<String, Contract> contracts = new TreeMap<>();
    public TreeMap<String, Map<String, Double>> readings = new TreeMap<>();

    public int servicePointCounter = 0;
    public int meterCounter = 0;
    public int userCounter = 0;
    public int contractCounter = 0;

    /**
     * Defines a new service point.
     *
     * @param municipality the municipality of the service point
     * @param address      the address of the service point
     * @param lat          the latitude of the service point
     * @param lon          the longitude of the service point
     * @return the id of the service point
     */
    public String defineServicePoint(String municipality, String address, double lat, double lon) {
        String id = "SP" + (++servicePointCounter);
        ServicePoint sp = new ServicePointEntity(id, municipality, address, new Point(lon, lat));
        servicePoints.put(id, sp);
        return id;
    }

    /**
     * Returns the list of service points.
     *
     * @return the list of service points
     */
    public Collection<String> getServicePoints() {
        return servicePoints.keySet();
    }

    /**
     * Returns the service point with the given id.
     *
     * @param spId the id of the service point
     * @return the service point with the given id
     */
    public ServicePoint getServicePoint(String spId) {
        return servicePoints.get(spId);
    }

    /**
     * Adds a new meter to the utility company.
     *
     * @param sn    serial number of the meter
     * @param brand brand of the meter
     * @param model model of the meter
     * @param unit  unit of measure
     * @return the assigned unique id of the meter
     */
    public String addMeter(String sn, String brand, String model, String unit) {
        String id = "MT" + (++meterCounter);
        Meter meter = new MeterEntity(id, sn, brand, model, unit);
        meters.put(id, meter);
        return id;
    }

    /**
     * Connects a meter to a service point.
     *
     * @param spId    the id of the service point
     * @param meterId the id of the meter
     */
    public void installMeter(String spId, String meterId) {
        ServicePoint sp = servicePoints.get(spId);
        Meter meter = meters.get(meterId);
        ((ServicePointEntity) sp).setMeter(meter);
        ((MeterEntity) meter).setServicePoint(sp);
    }

    /**
     * Returns the meter with the given id.
     *
     * @param mid the id of the meter
     * @return the meter with the given id
     */
    public Meter getMeter(String mid) {
        return meters.get(mid);
    }

    //----
    // R2 User and contracts

    /**
     * Adds a new user to the utility company.
     *
     * @param ssn      the social security number of the user
     * @param name    the name of the user
     * @param surname the surname of the user
     * @param address the address of the user
     * @param email   the email of the user
     * @return the id of the user
     */
    public String addUser(String ssn, String name, String surname, String address, String email) {
        String id = "U" + (++userCounter);
        User user = new ResidentialUser(id, ssn, name, surname, address, email);
        users.put(id, user);
        return id;
    }

    /**
     * Adds a new business user to the utility company.
     *
     * @param ssn           the social security number or tax code of the user
     * @param businessName the name of the business
     * @param address      the address of the business
     * @param email        the email of the business
     * @return the id of the user
     */
    public String addUser(String ssn, String businessName, String address, String email) {
        String id = "U" + (++userCounter);
        User user = new BusinessUser(id, ssn, businessName, address, email);
        users.put(id, user);
        return id;
    }

    /**
     * Returns the user with the given id.
     *
     * @param uid the id of the user
     * @return the user with the given id
     */
    public User getUser(String uid) {
        return users.get(uid);
    }

    /**
     * Returns all users
     *
     * @return a collection of users' id
     */
    public Collection<String> getUsers() {
        return users.keySet();
    }

    /**
     * Signs a new contract with a user that is provided through a service point.
     *
     * @param user the id of the user
     * @param pdp  the id of the service point
     * @return the id of the contract
     */
    public String signContract(String user, String pdp) throws UtilityException {
        User u = users.get(user);
        ServicePoint sp = servicePoints.get(pdp);
        if (u == null || sp == null || sp.getMeter().isEmpty()) {
            throw new UtilityException("Invalid user or service point");
        }
        String id = "C" + (++contractCounter);
        Contract contract = new ContractEntity(id, u, sp);
        contracts.put(id, contract);
        return id;
    }

    /**
     * Returns the contract with the given id.
     *
     * @param contractId the id of the contract
     * @return the contract with the given id
     */
    public Contract getContract(String contractId) {
        return contracts.get(contractId);
    }

    //----
    // R3 Reading

    /**
     * Adds a new reading for a given meter.
     *
     * @param contractId
     * @param meterId
     * @param date
     * @param value
     * @throws UtilityException if the contract and meter do not match
     */
    public void addReading(String contractId, String meterId, String date, double value) throws UtilityException {
        Contract contract = contracts.get(contractId);
        if (contract == null || !contract.getServicePoint().getMeter().get().getId().equals(meterId)) {
            throw new UtilityException("Invalid contract or meter");
        }
        readings.computeIfAbsent(contractId, k -> new TreeMap<>()).put(date, value);    }

    /**
     * Adds a new reading for a given meter.
     *
     * @param contractId id of the contract
     * @return a map that links dates and metering values
     */      
    public Map<String,Double> getReadings(String contractId) {
       return readings.getOrDefault(contractId, Collections.emptyMap());
    }

    /**
     * Read latest reading
     * 
     * @param contractId id of the contract
     * @return a metering value 
     */
    public double getLatestReading(String contractId) {
        Map<String, Double> readingMap = readings.get(contractId);
        if (readingMap == null || readingMap.isEmpty()) {
            return Double.NaN;
        }
        return ((NavigableMap<String, Double>) readingMap).lastEntry().getValue();
    }

    //----
    // R4 Tariffe

    /**
     * Computes the estimated reading for a given contract and date.
     * The estimated reading is computed as the linear interpolation of the latest two readings.
     *
     * @param contractId the id of the contract
     * @param date       the date for which the reading is estimated
     * @return the estimated reading
     * @throws UtilityException if estimation cannot be computed
     */
    public double getEstimatedReading(String contractId, String date) throws UtilityException {
        Map<String, Double> readingMap = readings.get(contractId);
        if (readingMap == null || readingMap.size() < 2) {
            throw new UtilityException("Not enough readings for estimation");
        }

        String previousDate = null;
        String nextDate = null;
        for (String d : readingMap.keySet()) {
            if (d.compareTo(date) <= 0) {
                previousDate = d;
            }
            if (d.compareTo(date) > 0) {
                nextDate = d;
                break;
            }
        }

        if (previousDate == null || nextDate == null) {
            throw new UtilityException("Cannot estimate reading for the given date");
        }

        double y1 = readingMap.get(previousDate);
        double y2 = readingMap.get(nextDate);
        long t = LocalDate.parse(date).toEpochDay();
        long t1 = LocalDate.parse(previousDate).toEpochDay();
        long t2 = LocalDate.parse(nextDate).toEpochDay();

        return y1 + (t - t1) * (y2 - y1) / (double) (t2 - t1);
    }

    /**
     * Computes the consumption between two dates
     * 
     * @param contractId    the id of the contract
     * @param dateInitial   the initial date
     * @param dateFinal     the final date
     * @return  the total consumption between the two dates
     * @throws UtilityException if the contract id is not valid or a reading cannot be estimated for the dates
     */
    public double getConsumption(String contractId, String dateInitial, String dateFinal) throws UtilityException {
        double initialReading = getEstimatedReading(contractId, dateInitial);
        double finalReading = getEstimatedReading(contractId, dateFinal);
        return finalReading - initialReading;
    }

        /**
     * Returns the consumption breakdown (month by month) 
     * 
     * @param contractId    id of the contrac
     * @param monthStart    initial month
     * @param monthEnd      final month
     * @param year          year of reference
     * @return the breakdown
     * @throws UtilityException in case contract is not valid, or it is not possible to get reading estimates
     */
    public List<String> getBillBreakdown(String contractId, int monthStart, int monthEnd, int year) throws UtilityException {
        List<String> breakdown = new LinkedList<>();
        for (int month = monthStart; month <= monthEnd; month++) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.plusMonths(1);

            double startReading = getEstimatedReading(contractId, start.toString());
            double endReading = getEstimatedReading(contractId, end.toString());

            String bill = String.format("%s..%s: %.2f -> %.2f = %.2f", start, end, startReading, endReading, endReading - startReading);
            breakdown.add(bill);
        }
        return breakdown;
    }

}
