// package org.group4;

// import org.junit.jupiter.api.*;

// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.HashMap;

// import static org.junit.jupiter.api.Assertions.*;

// @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
// public class Integration_Tests {
//     private final HashMap<String, Restaurant> restaurants = new HashMap<>();
//     private final HashMap<String, Customer> customers = new HashMap<>();

//     @BeforeEach
//     void setUp() {
//         // create_restaurant,REST001,Olive Garden,Coral Springs,FL,33071,45,false,6
//         Restaurant rest001 = new Restaurant("REST001", "Olive Garden",
//                 new Address("Coral Springs", "FL", 33071),
//                 45, false, 6);
//         restaurants.put(rest001.getId(), rest001);

//         // create_restaurant,REST002,Cheesecake Factory,Boca Raton,FL,33431,48,false,6
//         Restaurant rest002 = new Restaurant("REST002", "Cheesecake Factory",
//                 new Address("Boca Raton", "FL", 33431),
//                 48, false, 6);
//         restaurants.put(rest002.getId(), rest002);

//         // create_restaurant,REST003,Papa Johns,Lauderdale Lakes,FL,33313,50,false,6
//         Restaurant rest003 = new Restaurant("REST003", "Papa Johns",
//                 new Address("Lauderdale Lakes", "FL", 33313),
//                 50, false, 6);
//         restaurants.put(rest003.getId(), rest003);

//         // create_restaurant,REST004,IL Mulino,Tampa,FL,33019,33,false,6
//         Restaurant rest004 = new Restaurant("REST004", "IL Mulino",
//                 new Address("Tampa", "FL", 33019),
//                 33, false, 6);
//         restaurants.put(rest004.getId(), rest004);
//         // create_customer,CUST001,Angel,Cabrera,Miami,FL,33122,100.0
//         Customer cust001 = new Customer("CUST001", "Angel", "Cabrera",
//                 new Address("Miami", "FL", 33122), 100.0);
//         customers.put(cust001.getId(), cust001);

//         // create_customer,CUST002,Mark,Moss,Atlanta,GA,30313,100.0
//         Customer cust002 = new Customer("CUST002", "Mark", "Moss",
//                 new Address("Atlanta", "GA", 30313), 100.0);
//         customers.put(cust002.getId(), cust002);

//         // create_customer,CUST003,Neel,Ganediwal,Parkland,FL,33067,100.0
//         Customer cust003 = new Customer("CUST003", "Neel", "Ganediwal",
//                 new Address("Parkland", "FL", 33067), 100.0);
//         customers.put(cust003.getId(), cust003);

//         // create_customer,CUST004,Henry,Owen,Chicago,IL,60629,100.0
//         Customer cust004 = new Customer("CUST004", "Henry", "Owen",
//                 new Address("Chicago", "IL", 60629), 100.0);
//         customers.put(cust004.getId(), cust004);
//     }

//     @AfterEach
//     void cleanup() {
//         restaurants.clear();
//         customers.clear();
//     }

//     @Test
//     void Customer_Reservation_And_Arrival_Happy_Path() {
//         Customer customer = customers.get("CUST001");
//         Restaurant restaurant = restaurants.get("REST001");

//         int initialCredits = customer.getCredits();
//         int reservationCredits = 80;
//         int initalSpace = restaurant.getSeatingCapacity();

//         // make_reservation,CUST001,REST001,4,2024-05-24,19:00,80
//         Reservation reservation = restaurant.makeReservation(customer, 4, LocalDateTime.parse("2024-05-24T19:00:00"), reservationCredits);
//         assertNotNull(reservation, "Reservation not created properly");

//         // customer_arrival,CUST001,REST001,2024-05-24,18:50,19:00
//         ArrivalStatus status = restaurant.customerArrives(customer, LocalDateTime.parse("2024-05-24T19:00:00"), LocalTime.parse("18:50:00"));
//         assertAll("Customer Arrival",
//                 () -> assertEquals(ArrivalStatus.ON_TIME, status, "On time arrival not handled properly"),
//                 () -> assertEquals(initialCredits + reservationCredits, customer.getCredits(), "Customer credits not awarded for on time reservation"),
//                 () -> assertEquals(initalSpace - reservation.getPartySize(), restaurant.checkSpace(reservation.getDateTime()), "Restaurant space not updated properly")
//         );
//     }
// }
