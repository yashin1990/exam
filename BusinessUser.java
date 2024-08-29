package it.polito.po.utility;

public class BusinessUser implements User {
    private String id;
    private String vatNumber;
    private String businessName;
    private String address;
    private String email;

    public BusinessUser(String id, String vatNumber, String businessName, String address, String email) {
        this.id = id;
        this.vatNumber = vatNumber;
        this.businessName = businessName;
        this.address = address;
        this.email = email;
    }

    @Override
    public Type getType() {
        return Type.BUSINESS;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCF() {
        return vatNumber;
    }

    @Override
    public String getName() {
        return businessName;
    }

    @Override
    public String getSurname() {
        return "";
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
