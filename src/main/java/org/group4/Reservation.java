package org.group4;

import java.time.LocalDateTime;
import java.util.Objects;

class Reservation {
    private final Customer customer;
    private LocalDateTime dateTime;
    private LocalDateTime endTime;
    private final int credits;
    private int partySize;

    public static final int RESERVATION_DURATION = 2;

    public static String generateKey(Customer _customer, LocalDateTime _dateTime) {
        return String.format("Customer=%s-DateTime=%s", _customer.getId(), _dateTime);
    }

    public Reservation(Customer customer, int partySize, LocalDateTime dateTime, int credits) {
        this.customer = customer;
        this.partySize = partySize;
        this.dateTime = dateTime;
        this.endTime = dateTime.plusHours(2);
        this.credits = credits;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.endTime = dateTime.plusHours(2);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getCredits() {
        return credits;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
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
                ", endTime=" + endTime +
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
