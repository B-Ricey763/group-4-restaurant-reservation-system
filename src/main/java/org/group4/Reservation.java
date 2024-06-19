package org.group4;

import java.time.LocalDateTime;
import java.util.Objects;

class Reservation {
    private final Customer customer;
    private final LocalDateTime dateTime;
    private final int credits;
    private final int partySize;

    public static String generateKey(Customer _customer, LocalDateTime _dateTime) {
        return String.format("Customer=%s-DateTime=%s", _customer.getId(), _dateTime);
    }

    public Reservation(Customer customer, int partySize, LocalDateTime dateTime, int credits) {
        this.customer = customer;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.credits = credits;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCredits() {
        return credits;
    }

    public int getPartySize() {
        return partySize;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", dateTime=" + dateTime +
                ", credits=" + credits +
                ", partySize=" + partySize +
                '}';
    }

    /**
     * @return The unique key used to identify this reservation
     */
    public String getKey() {
        return generateKey(this.customer,this.dateTime);
    }

}
