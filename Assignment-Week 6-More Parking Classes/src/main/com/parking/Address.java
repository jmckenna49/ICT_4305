package com.parking;

public class Address {
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zipcode;

    public Address(String streetAddress1, String streetAddress2,
                   String city, String state, String zipcode) {
        this.streetAddress1 = streetAddress1;
        this.streetAddress2 = streetAddress2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }
    public String getStreetAddress1() {
        return streetAddress1;
    }
    public String getStreetAddress2() {
        return streetAddress2;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZipcode() {
        return zipcode;
    }
    public String getAddressInfo() {
        String result = streetAddress1;

        if (streetAddress2 != null && !streetAddress2.isEmpty()) {
            result += "\n" + streetAddress2;
        }

        result += "\n" + city + ", " + state + " " + zipcode;

        return result;
    }
}
