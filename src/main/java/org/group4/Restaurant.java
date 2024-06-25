package org.group4;

import java.time.LocalDate;
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

    public void removeReservation(Customer customer, LocalDateTime reservationDateTime) {
        reservations.remove(Reservation.generateKey(customer, reservationDateTime));
    }

    // TODO: handle the case when the date is within 2 hours or other edge cases, like when customer has no funds
    //  Also remember since we are using a unique key for each reservation, the customer can absolutely NOT book a reservation
    //  for the same time.
    public Reservation makeReservation(Customer customer, int partySize, LocalDateTime reservationDateTime, int credits) {
        if (partySize < 1 || reservationDateTime == null || customer == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        if (checkSpace(reservationDateTime) < partySize) {
            throw new NoSpaceException();
        }
        if (customer.isReservationConflict(reservationDateTime)) {
            throw new ReservationConflictException();
        }
        Reservation reservation = new Reservation(customer, partySize, reservationDateTime, credits);
        reservations.put(reservation.getKey(), reservation);
        customer.addRes(reservation);
        return reservation;
    }

    public ArrivalStatus customerArrives(Customer customer, LocalDate reservationDate, LocalTime arrivalTime, LocalTime reservationTime) {
        Reservation reservation;
        LocalDateTime reservationDateTime;
        if (reservationTime == null) {
            reservationDateTime = LocalDateTime.of(reservationDate, arrivalTime);
            reservation = makeReservation(customer, 1, reservationDateTime, 0);
            return ArrivalStatus.WALK_IN;
        } else {
            reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
            reservation = reservations.get(Reservation.generateKey(customer, reservationDateTime));
        }        
        // TODO: Handle cases, look at canvas assignment for details, use Arrival status enum:
        //  - 15 minutes late: mark as missed, handle cases
        //  - on time: seat and reward
        //  - arrive early: tell them to come back later
        //  - walk in
        // if (reservation == null) {
        //     return ArrivalStatus.WALK_IN;
        // }
        reservationTime = reservation.getDateTime().toLocalTime();
        if (arrivalTime.isAfter(reservationTime.plusMinutes(15))) {
            reservation.getCustomer().setMissedReservations(reservation.getCustomer().getMissedReservations() + 1);
            // If customer misses three reservations reset missed reservation counter and reset credits.\
            boolean reset = false;
            if (reservation.getCustomer().getMissedReservations() == 3) {
                reservation.getCustomer().setMissedReservations(0);
                reservation.getCustomer().setCredits(0);
                reset = true;
            }
            // LocalDateTime dateTime = LocalDateTime.of(reservationDate, arrivalTime);
            // int partySize = reservation.getPartySize();
            // int credits = reservation.getCredits();
            // removeReservation(customer, reservationDateTime);
            // makeReservation(customer, partySize, dateTime, credits);
            int orginalParty = reservation.getPartySize();
            reservation.setPartySize(0);
            if (checkSpace(reservationDateTime) >= reservation.getPartySize()) {
                reservation.setDateTime(reservationDateTime);
                reservation.setPartySize(orginalParty);
            }
            if (reset) {
                return ArrivalStatus.LATE_RESET;
            }
            return ArrivalStatus.LATE;
        } else if (arrivalTime.isBefore(reservationTime) && arrivalTime.isBefore(reservationTime.minusMinutes(30))) {
            return ArrivalStatus.EARLY;
        } else if (arrivalTime.isBefore(reservationTime.plusMinutes(15))) {
            reservation.getCustomer().setCredits(reservation.getCustomer().getCredits() + reservation.getCredits());
            return ArrivalStatus.ON_TIME;
        }
        return ArrivalStatus.WALK_IN;
    }

    public int checkSpace(LocalDateTime arrivalTime) {
        // TODO: calculate the number of seats available based on the reservations at the time.
        //  Remember, a walk in party is treated like a reservation, so we can just use that list
        int usedSeats = 0;
        for (Reservation reservation : reservations.values()) {
            if (reservation.getDateTime().isEqual(arrivalTime) ||
                (reservation.getDateTime().isBefore(arrivalTime) && 
                reservation.getEndTime().isAfter(arrivalTime))) {
                usedSeats += reservation.getPartySize();
            }
        } 
        return seatingCapacity - usedSeats;
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
