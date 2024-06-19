package org.group4;

record Address(String streetName, String stateAbbreviation, int zipCode) {
    @Override
    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", zipCode=" + zipCode +
                '}';
    }
}
