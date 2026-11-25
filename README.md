🚌 Bus Reservation System

A Console-Based Bus Reservation Application Using Java and Data Structures

A Java-based Bus Reservation System developed as a Data Structures and Algorithms (DSA) project. It manages bus information, passenger bookings, ticket cancellations, and waiting lists using fundamental Java programming concepts and data structures.

The project demonstrates the practical application of ArrayList, Queue, Searching, Sorting, Comparator, Object-Oriented Programming, and File Handling.

---

📌 Project Overview

The Bus Reservation System provides a menu-driven console interface through which users can manage bus reservations.

It allows users to view available buses, search for buses by destination, book tickets, cancel reservations, and manage waiting passengers.

When a bus is full, new passengers are automatically added to its waiting queue. If a confirmed ticket is cancelled, the first passenger in the waiting queue receives the released seat.

The application also saves its data in text files so that information can be loaded again when the program is restarted.

---

✨ Features

- View All Buses: Display bus IDs, routes, fares, total seats, and available seats.
- Search by Destination: Find buses using destination names with case-insensitive searching.
- Book Tickets: Reserve seats by entering a bus ID and passenger name.
- Automatic Waiting List: Add passengers to a FIFO queue when a bus is full.
- Cancel Tickets: Cancel confirmed reservations using ticket IDs.
- Automatic Seat Reallocation: Assign released seats to the first passenger in the waiting queue.
- Sort by Fare: Arrange buses from lowest to highest fare using Comparator.
- View Confirmed Bookings: Display all confirmed passenger reservations.
- View Waiting Lists: Display passengers waiting for seats.
- File Handling: Save and load buses, bookings, waiting lists, and ticket numbers.

---

🛠️ Technologies Used

Technology| Purpose
Java| Core programming language
ArrayList| Store bus and booking records
Queue| Manage waiting passengers
Comparator| Sort buses by fare
Searching| Find buses and tickets
File Handling| Save and retrieve application data
OOP| Organize the application using classes and objects

---

🧠 Data Structures Used

1. ArrayList

Used to store:

- Bus objects.
- Confirmed booking records.

It allows the application to maintain dynamically sized collections.

2. Queue

Each bus maintains its own waiting queue.

The queue follows FIFO (First In, First Out), ensuring that the passenger who joined the waiting list first receives the next available seat.

3. Comparator

Used to sort buses according to their fares in ascending order.

---

⚙️ How the System Works

1. The application loads bus details and previously saved information.
2. A menu is displayed to the user.
3. The user selects an operation.
4. The system performs the selected task, such as searching, booking, cancellation, or sorting.
5. If a bus is full, the passenger joins its waiting queue.
6. If a confirmed ticket is cancelled, the first waiting passenger receives the released seat.
7. The application saves the updated information when the user exits.

---

📂 Project Structure

Bus-Reservation-System/
│
├── BusReservationSystem.java
├── buses.txt
├── bookings.txt
├── waiting.txt
├── next_ticket.txt
│
└── README.md

File descriptions:

File| Purpose
"BusReservationSystem.java"| Main Java source code and application logic
"buses.txt"| Stores bus information
"bookings.txt"| Stores confirmed ticket records
"waiting.txt"| Stores waiting passengers
"next_ticket.txt"| Stores the next ticket number
"README.md"| Project documentation

The data files are created or updated by the application as required.

---

🚀 Getting Started

Prerequisites

Before running the project, make sure you have:

- Java Development Kit (JDK) 8 or above.
- Visual Studio Code, IntelliJ IDEA, or another Java-compatible editor.
- A terminal or command prompt.

Installation

Step 1: Clone the repository

git clone YOUR_GITHUB_REPOSITORY_URL
