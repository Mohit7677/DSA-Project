import java.io.*;
import java.util.*;

/**
 * Bus Reservation System
 * College Project - Problem Statement No. 19
 *
 * Compile: javac BusReservationSystem.java
 * Run:     java BusReservationSystem
 *
 * Keep buses.txt in the same folder. The program creates/updates:
 * bookings.txt, waiting.txt and next_ticket.txt automatically.
 */
public class BusReservationSystem {

    private static final String BUS_FILE = "buses.txt";
    private static final String BOOKING_FILE = "bookings.txt";
    private static final String WAITING_FILE = "waiting.txt";
    private static final String COUNTER_FILE = "next_ticket.txt";

    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<Bus> buses = new ArrayList<>();
    private static final ArrayList<Booking> bookings = new ArrayList<>();
    private static int nextTicketNumber = 101;

    // Represents one bus and its waiting queue.
    static class Bus {
        String busId;
        String source;
        String destination;
        int fare;
        int totalSeats;
        int availableSeats;
        Queue<Passenger> waitingQueue;

        Bus(String busId, String source, String destination, int fare, int totalSeats) {
            this.busId = busId;
            this.source = source;
            this.destination = destination;
            this.fare = fare;
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;
            this.waitingQueue = new LinkedList<>();
        }
    }

    // Represents a passenger.
    static class Passenger {
        String name;

        Passenger(String name) {
            this.name = name;
        }
    }

    // Represents a confirmed ticket.
    static class Booking {
        String ticketId;
        String passengerName;
        String busId;

        Booking(String ticketId, String passengerName, String busId) {
            this.ticketId = ticketId;
            this.passengerName = passengerName;
            this.busId = busId;
        }
    }

    public static void main(String[] args) {
        loadBuses();
        loadBookings();
        loadWaitingQueues();
        loadTicketCounter();

        System.out.println("========================================");
        System.out.println("          BUS RESERVATION SYSTEM");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            showMenu();
            System.out.print("Enter your choice: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    displayBuses();
                    break;
                case "2":
                    searchBusesByDestination();
                    break;
                case "3":
                    bookTicket();
                    break;
                case "4":
                    cancelTicket();
                    break;
                case "5":
                    sortBusesByFare();
                    break;
                case "6":
                    displayBookings();
                    break;
                case "7":
                    displayWaitingLists();
                    break;
                case "0":
                    saveAllData();
                    System.out.println("All data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 0 to 7.");
            }
            System.out.println();
        }
    }

    private static void showMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. View all buses");
        System.out.println("2. Search buses by destination");
        System.out.println("3. Book ticket");
        System.out.println("4. Cancel ticket");
        System.out.println("5. Sort buses by fare");
        System.out.println("6. View all confirmed bookings");
        System.out.println("7. View waiting lists");
        System.out.println("0. Save and exit");
        System.out.println("-----------------------------------------");
    }

    // ---------- BUS LOADING AND SEARCHING ----------

    private static void loadBuses() {
        File file = new File(BUS_FILE);
        if (!file.exists()) {
            createDefaultBusFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(BUS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] fields = line.split("\\|");
                if (fields.length != 5) {
                    System.out.println("Skipping invalid bus record: " + line);
                    continue;
                }

                try {
                    String id = fields[0].trim();
                    String source = fields[1].trim();
                    String destination = fields[2].trim();
                    int fare = Integer.parseInt(fields[3].trim());
                    int seats = Integer.parseInt(fields[4].trim());

                    if (fare < 0 || seats < 0 || findBus(id) != null) {
                        System.out.println("Skipping invalid/duplicate bus: " + id);
                        continue;
                    }
                    buses.add(new Bus(id, source, destination, fare, seats));
                } catch (NumberFormatException ex) {
                    System.out.println("Skipping bus with invalid fare/seats: " + line);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading buses.txt: " + ex.getMessage());
        }

        // Fallback data if the file is empty or unavailable.
        if (buses.isEmpty()) {
            buses.add(new Bus("B101", "Raipur", "Bhopal", 700, 2));
            buses.add(new Bus("B102", "Raipur", "Nagpur", 450, 2));
        }
    }

    private static void createDefaultBusFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BUS_FILE))) {
            writer.println("# busId|source|destination|fare|totalSeats");
            writer.println("B101|Raipur|Bhopal|700|2");
            writer.println("B102|Raipur|Nagpur|450|2");
        } catch (IOException ex) {
            System.out.println("Could not create buses.txt: " + ex.getMessage());
        }
    }

    private static Bus findBus(String busId) {
        for (Bus bus : buses) {
            if (bus.busId.equalsIgnoreCase(busId)) {
                return bus;
            }
        }
        return null;
    }

    private static void displayBuses() {
        System.out.println("\nBUS DETAILS");
        System.out.printf("%-8s %-12s %-12s %-10s %-12s %-12s%n",
                "Bus ID", "Source", "Destination", "Fare", "Total Seats", "Seats Left");
        for (Bus bus : buses) {
            System.out.printf("%-8s %-12s %-12s Rs.%-7d %-12d %-12d%n",
                    bus.busId, bus.source, bus.destination, bus.fare,
                    bus.totalSeats, bus.availableSeats);
        }
    }

    private static void searchBusesByDestination() {
        System.out.print("Enter destination: ");
        String destination = input.nextLine().trim();
        boolean found = false;

        for (Bus bus : buses) {
            if (bus.destination.equalsIgnoreCase(destination)) {
                System.out.println("Bus found: " + bus.busId + " | "
                        + bus.source + " to " + bus.destination
                        + " | Fare: Rs." + bus.fare
                        + " | Seats left: " + bus.availableSeats);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No buses found for destination: " + destination);
        }
    }

    // ---------- BOOKING AND CANCELLATION ----------

    private static void bookTicket() {
        System.out.print("Enter Bus ID: ");
        String busId = input.nextLine().trim();
        Bus bus = findBus(busId);

        if (bus == null) {
            System.out.println("Bus " + busId + " not found.");
            return;
        }

        System.out.print("Enter passenger name: ");
        String name = input.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Passenger name cannot be empty.");
            return;
        }

        if (bus.availableSeats > 0) {
            confirmBooking(bus, name);
        } else {
            bus.waitingQueue.offer(new Passenger(name));
            saveAllData();
            System.out.println("Bus " + bus.busId + " is full. "
                    + name + " added to the waiting queue.");
        }
    }

    private static void confirmBooking(Bus bus, String passengerName) {
        String ticketId = "TKT" + nextTicketNumber;
        nextTicketNumber++;

        bookings.add(new Booking(ticketId, passengerName, bus.busId));
        bus.availableSeats--;

        saveAllData();
        System.out.println("Ticket booked. " + ticketId + " | "
                + passengerName + " | " + bus.busId + " "
                + bus.source + "-" + bus.destination);
        System.out.println("Seats left: " + bus.availableSeats);
    }

    private static void cancelTicket() {
        System.out.print("Enter Ticket ID to cancel: ");
        String ticketId = input.nextLine().trim();

        Booking bookingToCancel = null;
        for (Booking booking : bookings) {
            if (booking.ticketId.equalsIgnoreCase(ticketId)) {
                bookingToCancel = booking;
                break;
            }
        }

        if (bookingToCancel == null) {
            System.out.println("Ticket " + ticketId + " not found.");
            return;
        }

        Bus bus = findBus(bookingToCancel.busId);
        bookings.remove(bookingToCancel);
        System.out.println("Ticket " + ticketId + " cancelled.");

        if (bus != null && !bus.waitingQueue.isEmpty()) {
            // FIFO: the passenger who joined the queue first gets the seat.
            Passenger nextPassenger = bus.waitingQueue.poll();
            String newTicketId = "TKT" + nextTicketNumber;
            nextTicketNumber++;

            bookings.add(new Booking(newTicketId, nextPassenger.name, bus.busId));
            // Do not increase availableSeats: the cancelled seat is reassigned.
            System.out.println("Seat given to " + nextPassenger.name
                    + ". New ticket: " + newTicketId);
        } else if (bus != null) {
            bus.availableSeats++;
            System.out.println("Seat is now available. Seats left: "
                    + bus.availableSeats);
        }

        saveAllData();
    }

    // ---------- SORTING AND DISPLAY ----------

    private static void sortBusesByFare() {
        buses.sort(Comparator.comparingInt(bus -> bus.fare));
        System.out.println("Buses sorted by fare (lowest to highest):");
        displayBuses();
    }

    private static void displayBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No confirmed bookings.");
            return;
        }

        System.out.println("\nCONFIRMED BOOKINGS");
        for (Booking booking : bookings) {
            Bus bus = findBus(booking.busId);
            String route = bus == null ? "Unknown route"
                    : bus.source + "-" + bus.destination;
            System.out.println(booking.ticketId + " | "
                    + booking.passengerName + " | "
                    + booking.busId + " | " + route);
        }
    }

    private static void displayWaitingLists() {
        boolean found = false;
        for (Bus bus : buses) {
            if (!bus.waitingQueue.isEmpty()) {
                found = true;
                System.out.println("\nWaiting list for " + bus.busId + ":");
                int position = 1;
                for (Passenger passenger : bus.waitingQueue) {
                    System.out.println(position + ". " + passenger.name);
                    position++;
                }
            }
        }
        if (!found) {
            System.out.println("No passengers are currently waiting.");
        }
    }

    // ---------- FILE HANDLING ----------

    private static void loadBookings() {
        File file = new File(BOOKING_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", 3);
                if (fields.length != 3) {
                    continue;
                }

                Bus bus = findBus(fields[2].trim());
                if (bus == null) {
                    continue;
                }

                String ticketId = fields[0].trim();
                String passengerName = fields[1].trim();
                bookings.add(new Booking(ticketId, passengerName, bus.busId));
                bus.availableSeats = Math.max(0, bus.availableSeats - 1);

                // Recover the next ID from saved tickets as an extra safeguard.
                try {
                    int number = Integer.parseInt(ticketId.replace("TKT", ""));
                    nextTicketNumber = Math.max(nextTicketNumber, number + 1);
                } catch (NumberFormatException ignored) {
                    // Ignore malformed ticket number.
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading bookings: " + ex.getMessage());
        }
    }

    private static void loadWaitingQueues() {
        File file = new File(WAITING_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", 2);
                if (fields.length != 2) {
                    continue;
                }

                Bus bus = findBus(fields[0].trim());
                String name = fields[1].trim();
                if (bus != null && !name.isEmpty()) {
                    bus.waitingQueue.offer(new Passenger(name));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading waiting list: " + ex.getMessage());
        }
    }

    private static void loadTicketCounter() {
        File file = new File(COUNTER_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String value = reader.readLine();
            if (value != null) {
                nextTicketNumber = Math.max(nextTicketNumber, Integer.parseInt(value.trim()));
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Could not read ticket counter; deriving it from bookings.");
        }
    }

    private static void saveAllData() {
        saveBookings();
        saveWaitingQueues();
        saveTicketCounter();
    }

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking booking : bookings) {
                writer.println(booking.ticketId + "|"
                        + safeText(booking.passengerName) + "|" + booking.busId);
            }
        } catch (IOException ex) {
            System.out.println("Error saving bookings: " + ex.getMessage());
        }
    }

    private static void saveWaitingQueues() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(WAITING_FILE))) {
            for (Bus bus : buses) {
                for (Passenger passenger : bus.waitingQueue) {
                    writer.println(bus.busId + "|" + safeText(passenger.name));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error saving waiting list: " + ex.getMessage());
        }
    }

    private static void saveTicketCounter() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUNTER_FILE))) {
            writer.println(nextTicketNumber);
        } catch (IOException ex) {
            System.out.println("Error saving ticket counter: " + ex.getMessage());
        }
    }

    private static String safeText(String value) {
        return value.replace("|", " ").replace("\n", " ").replace("\r", " ");
    }
}
