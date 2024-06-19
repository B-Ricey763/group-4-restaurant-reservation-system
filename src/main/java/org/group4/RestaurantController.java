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
                    System.out.println(restaurant);
                    restaurants.put(restaurant.getId(), restaurant);
                } else if (tokens[0].equals("create_customer")) {
                    Address address = new Address(tokens[4], tokens[5], Integer.parseInt(tokens[6]));
                    Customer customer = new Customer(tokens[1], tokens[2], tokens[3], address,
                            Double.parseDouble(tokens[7]));
                    System.out.println(customer);
                    customers.put(customer.getId(), customer);
                } else if (tokens[0].equals("make_reservation")) {
                    Customer customer = customers.get(tokens[1]);
                    Restaurant restaurant = restaurants.get(tokens[2]);
                    // Turns the date (example: 2024-05-24) and the time (ex: 19:00) to an ISO datetime: "2024-05-24T19:00:00"
                    LocalDateTime dateTime = LocalDateTime.parse("%sT%s:00".formatted(tokens[4], tokens[5]));

                    Reservation res = restaurant.makeReservation(customer, Integer.parseInt(tokens[3]), dateTime,
                            Integer.parseInt(tokens[6]));
                    System.out.println(res);
                } else if (tokens[0].equals("customer_arrival")) {
                    System.out.print("customer_identifier: " + tokens[1] + ", restaurant_identifier: " + tokens[2]);
                    System.out.println(
                            ", reservation_date: " + tokens[3] + ", arrival_time: " + tokens[4] + ", reservation_time: " + tokens[5]);
                    Customer customer = customers.get(tokens[1]);
                    Restaurant restaurant = restaurants.get(tokens[2]);

                    LocalDate reservationDate = LocalDate.parse(tokens[3]);
                    LocalTime reservationTime = LocalTime.parse(tokens[5]);

                    LocalDateTime dateTime = LocalDateTime.of(reservationDate, reservationTime);

                    LocalTime arrivalTime = LocalTime.parse(tokens[4]);
                    ArrivalStatus result = restaurant.customerArrives(customer, dateTime, arrivalTime);

                    System.out.println("Arrival status: " + result);
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
