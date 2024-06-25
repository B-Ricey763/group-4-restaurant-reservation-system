package org.group4;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Restaurant Reservation System!\nEnter commands (type 'exit' to finish):");
        RestaurantController simulator = new RestaurantController();
        simulator.commandLoop();
    }
}
