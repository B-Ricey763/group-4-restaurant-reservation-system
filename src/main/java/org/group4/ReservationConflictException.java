package org.group4;

class ReservationConflictException extends RuntimeException {
    public ReservationConflictException() {
        super("Customer has a reservation conflict.");
    }
}