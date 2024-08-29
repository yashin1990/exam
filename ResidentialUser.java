package it.polito.po.utility;

public class ResidentialUser implements User {
    private String id;
    private String taxCode;
    private String name;
    private String surname;
    private String address;
    private String email;

    public ResidentialUser(String id, String taxCode, String name, String surname, String address, String email) {
        this.id = id;
        this.taxCode = taxCode;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
    }

    @Override
    public Type getType() {
        return Type.RESIDENTIAL;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCF() {
        return taxCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
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
