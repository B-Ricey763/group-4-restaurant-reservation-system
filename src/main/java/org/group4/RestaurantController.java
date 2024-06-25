package org.group4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;

public class RestaurantController {
    private final HashMap<String, Restaurant> restaurants = new HashMap<>();
    private final HashMap<String, Customer> customers = new HashMap<>();

    public void commandLoop() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        while (true) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println(" echo >> " + wholeInputLine);
                // TODO: change print statements to match expected
                if (tokens[0].indexOf("//") == 0) {
                    // System.out.println(wholeInputLine);

                    // instructions to create simulation resources
                } else if (wholeInputLine.isEmpty()) {
                    continue;
                } else if (tokens[0].equals("create_restaurant")) {
                    Address address = new Address(tokens[3], tokens[4], Integer.parseInt(tokens[5]));
                    Restaurant restaurant = new Restaurant(tokens[1], tokens[2], address, Integer.parseInt(tokens[6]),
                            Boolean.parseBoolean(tokens[7]), Integer.parseInt(tokens[8]));
                    System.out.printf("Restaurant created: %s (%s) - %s, %s %s\n", tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                    restaurants.put(restaurant.getId(), restaurant);
                } else if (tokens[0].equals("create_customer")) {
                    Address address = new Address(tokens[4], tokens[5], Integer.parseInt(tokens[6]));
                    Customer customer = new Customer(tokens[1], tokens[2], tokens[3], address,
                            Double.parseDouble(tokens[7]));
                    System.out.printf("Customer added: %s - %s %s\n", tokens[1], tokens[2], tokens[3]);
                    customers.put(customer.getId(), customer);
                } else if (tokens[0].equals("make_reservation")) {
                    Customer customer = customers.get(tokens[1]);
                    Restaurant restaurant = restaurants.get(tokens[2]);
                    // Turns the date (example: 2024-05-24) and the time (ex: 19:00) to an ISO datetime: "2024-05-24T19:00:00"
                    LocalDateTime dateTime = LocalDateTime.parse("%sT%s:00".formatted(tokens[4], tokens[5]));
                    try {
                        Reservation res = restaurant.makeReservation(customer, Integer.parseInt(tokens[3]), dateTime,
                                Integer.parseInt(tokens[6]));
                        System.out.printf("Reservation requested for %s %s", customer.getFirstName(), customer.getLastName());
                        System.out.printf("\nReservation confirmed");
                        System.out.printf("\nReservation made for %s (%s %s) at %s\n", customer.getId(), customer.getFirstName(), customer.getLastName(), restaurant.getName());
                    } catch(ReservationConflictException rce) {
                        System.out.printf("Reservation requested for %s %s", customer.getFirstName(), customer.getLastName());
                        System.out.printf("\nReservation request denied, customer already has reservation with another restaurant within 2 hours of the requested time\n");
                    } catch(NoSpaceException nse) {
                        System.out.printf("Reservation requested for %s %s", customer.getFirstName(), customer.getLastName());
                        System.out.printf("\nReservation request denied, table has another active reservation within 2 hours of the requested time\n");
                    }
                } else if (tokens[0].equals("customer_arrival")) {
                    System.out.print("customer_identifier: " + tokens[1] + ", restaurant_identifier: " + tokens[2]);
                    System.out.println(
                            ", reservation_date: " + tokens[3] + ", arrival_time: " + tokens[4] + ", reservation_time: " + tokens[5]);
                    Customer customer = customers.get(tokens[1]);
                    Restaurant restaurant = restaurants.get(tokens[2]);

                    LocalDate reservationDate = LocalDate.parse(tokens[3]);
                    
                    LocalTime reservationTime = tokens[5].equals("null") ? null : LocalTime.parse(tokens[5]);

                    // LocalDateTime dateTime = LocalDateTime.of(reservationDate, reservationTime);

                    LocalTime arrivalTime = LocalTime.parse(tokens[4]);
                    ArrivalStatus result;
                    try {
                        result = restaurant.customerArrives(customer, reservationDate, arrivalTime, reservationTime);
                        if (result == ArrivalStatus.ON_TIME) {
                            System.out.printf("Customer %s (%s %s) has arrived at %s", customer.getId(), customer.getFirstName(), customer.getLastName(), restaurant.getName());
                            System.out.printf("\n%s %s - Successfully completed reservation", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nFull credits rewarded");
                            System.out.printf("\nSeats were available, %s %s seated", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d\n", customer.getMissedReservations());
                        } else if (result == ArrivalStatus.EARLY) {
                            System.out.printf("Customer %s (%s %s) has arrived early at %s", customer.getId(), customer.getFirstName(), customer.getLastName(), restaurant.getName());
                            System.out.printf("\nPlease come back during the reservation window");
                            System.out.printf("\nNo credits rewarded and no misses added");
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d\n", customer.getMissedReservations());
                        } else if (result == ArrivalStatus.LATE) {
                            System.out.printf("Customer %s (%s %s) has arrived late at %s", customer.getId(), customer.getFirstName(), customer.getLastName(), restaurant.getName());
                            System.out.printf("\n%s %s - Missed Reservation", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nNo credits rewarded and 1 miss added");
                            System.out.printf("\nSeats were available, %s %s seated.\n", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d", customer.getMissedReservations());
                        } else if (result == ArrivalStatus.WALK_IN) {
                            System.out.printf("%s %s - Walk-in party", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nNo reservation, however open table so request validated");
                            System.out.printf("\nNo credits rewarded and no misses added");
                            System.out.printf("\nSeats were available, %s %s seated.\n", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d", customer.getMissedReservations());
                        }
                        else if (result == ArrivalStatus.LATE_RESET) {
                            System.out.printf("Customer %s (%s %s) has arrived late at %s", customer.getId(), customer.getFirstName(), customer.getLastName(), restaurant.getName());
                            System.out.printf("\n%s %s - Missed Reservation", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nNo credits rewarded and 1 miss added");
                            System.out.printf("\nMisses: 3");
                            System.out.printf("\n%s %s - 3 Misses reached, both misses and credits will reset back to 0", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nSeats were available, %s %s seated", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d\n", customer.getMissedReservations());
                        }
                    } catch (NoSpaceException nse){
                        if (reservationTime == null) {
                            System.out.printf("%s %s - Walk-in party", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nNo credits rewarded and no misses added");
                            System.out.printf("\nNo reservation, however open table so request validated");
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d", customer.getMissedReservations());
                            System.out.printf("\nSeats were not available, %s %s rejected.\n", customer.getFirstName(), customer.getLastName());
                        } else {
                            System.out.printf("Customer %s %s - Late arrival", customer.getFirstName(), customer.getLastName());
                            System.out.printf("\nNo credits rewarded and no misses added");
                            System.out.printf("\nNo reservation, however open table so request validated");
                            System.out.printf("\nCredits: %d", customer.getCredits());
                            System.out.printf("\nMisses: %d", customer.getMissedReservations());
                            System.out.printf("\nSeats were not available, %s %s rejected.\n", customer.getFirstName(), customer.getLastName());
                        }
                    }
                } else if (tokens[0].equals("exit")) {
                    System.out.println("stop acknowledged");
                    break;

                } else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }
                System.out.println("Enter command: ");
            } catch (Exception e) {
                displayMessage("error", "during command loop >> execution");
                e.printStackTrace();
                System.out.println();
            }
        }

        System.out.println("simulation terminated");
        commandLineInput.close();
    }

    void displayMessage(String status, String text_output) {
        System.out.println(status.toUpperCase() + ": " + text_output.toLowerCase());
    }

}
