package org.group4;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

class Restaurant {
    private final String id;
    private final String name;
    private final Address address;
    private int rating;
    private boolean top10;
    private final int seatingCapacity;
    private final Map<String, Reservation> reservations = new HashMap<>();

    public Restaurant(String uniqueId, String name, Address address, int rating, boolean top10, int seatingCapacity) {
        this.id = (uniqueId == null) ? UUID.randomUUID().toString() : uniqueId;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.top10 = top10;
        this.seatingCapacity = seatingCapacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public Address getAddress() {
        return address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isTop10() {
        return top10;
    }

    public void setTop10(boolean top10) {
        this.top10 = top10;
    }

    // TODO: handle the case when the date is within 2 hours or other edge cases, like when customer has no funds
    //  Also remember since we are using a unique key for each reservation, the customer can absolutely NOT book a reservation
    //  for the same time.
    public Reservation makeReservation(Customer customer, int partySize, LocalDateTime reservationDateTime, int credits) {
        if (partySize < 1 || reservationDateTime == null || customer == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        // What is this doing?
        if (LocalDateTime.now().isAfter(reservationDateTime.minusHours(2))) {
            return null;
        }

        if (checkSpace(reservationDateTime) < partySize) {
            return null;
        }
        if (customer.isReservationConflict(reservationDateTime)) {
            return null;
        }

        Reservation reservation = new Reservation(customer, partySize, reservationDateTime, credits);
        reservations.put(reservation.getKey(), reservation);
        customer.addRes(reservation);
        return reservation;
    }

    public ArrivalStatus customerArrives(Customer customer, LocalDateTime reservationDateTime, LocalTime arrivalTime) {
        Reservation reservation = reservations.get(Reservation.generateKey(customer, reservationDateTime));
        System.out.println("Customer arrived with " + reservation);
        // TODO: Handle cases, look at canvas assignment for details, use Arrival status enum:
        //  - 15 minutes late: mark as missed, handle cases
        //  - on time: seat and reward
        //  - arrive early: tell them to come back later
        //  - walk in

        return ArrivalStatus.ON_TIME;
    }

    public int checkSpace(LocalDateTime arrivalTime) {
        // TODO: calculate the number of seats available based on the reservations at the time.
        //  Remember, a walk in party is treated like a reservation, so we can just use that list
        return seatingCapacity;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", rating=" + rating +
                ", top10=" + top10 +
                ", seatingCapacity=" + seatingCapacity +
                '}';
    }

}
