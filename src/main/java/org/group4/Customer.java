package org.group4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.group4.Reservation.RESERVATION_DURATION;

class Customer {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final Address address;
    private double funds;
    private int credits;
    private int missedReservations;
    private final List<Reservation> reservations = new ArrayList<>();

    public Customer(String uniqueId, String firstName, String lastName, Address address, double funds) {
        this.id = (uniqueId == null) ? UUID.randomUUID().toString() : uniqueId;
        this.firstName = firstName;
        // NOTE: We must consider the case when this is optional, like with Beyonce
        this.lastName = lastName;
        this.address = address;
        this.funds = funds;
        this.missedReservations = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getMissedReservations() {
        return missedReservations;
    }

    public void setMissedReservations(int missedReservations) {
        this.missedReservations = missedReservations;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public String getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * @param reservationTime time at which you want to create another reservation
     * @return true if the customer has no reservations within 2 hours of the reservationTime
     */
    public boolean isReservationConflict(LocalDateTime reservationTime) {
        for (Reservation r : reservations) {
            long timeDifferenceInSeconds = Duration.between(r.getDateTime(), reservationTime).abs().toSeconds();
            // Within two hours
            if (timeDifferenceInSeconds <= Duration.ofHours(RESERVATION_DURATION).toSeconds()) {
                return true;
            }
        }
        return false;
    }

    public void addRes(Reservation reservation) {
        reservations.add(reservation);
    }
}
