# Warehouse Inventory Management System

This project is a warehouse inventory management system designed to manage natural resources and their derivatives, maintain partner records, handle transactions, and implement a loyalty rewards system. It is developed using Object-Oriented Programming principles in Java and relies on a user interaction framework provided by `po-uilib.jar`.

## Overview

The system supports the following functionalities:

1. **Product Management**: Manages various products (simple and derived) available in the warehouse.
2. **Partner Management**: Stores partner details, assigns loyalty statuses, and tracks transactions with the warehouse.
3. **Transaction Management**: Handles purchases, sales, and breakdown transactions, along with transaction records and payment details.
4. **Notifications**: Sends notifications to partners about product stock and price changes.
5. **Loyalty Rewards**: Implements a point-based rewards system and applies discounts or penalties based on partner loyalty.

## Build and Run Instructions

### Compilation

There are two ways to compile the project. Both commands assume you are in the directory that contains the `po-uilib.jar` and `ggc` (the root directory containing the application code).

1. **Using `find` with inline compilation**:
   ```
   javac -cp po-uilib.jar:. `find ggc -name "*.java"`
   ```

2. **Using `xargs` for batch compilation**:
   ```
   find ggc -name "*.java" -print | xargs javac -cp po-uilib.jar:.
   ```

   > **Note**: If `po-uilib.jar` is in a different location or has a different name, adjust the command to reflect those changes.

### Execution

To execute the project after compilation, run the following command from the same directory:

1. **Basic Execution**:
   ```
   java -cp po-uilib.jar:. ggc.app.App
   ```

2. **Execution with Initial State File**:
   To load an initial state file (e.g., `file.im`), use:
   ```
   java -Dimport=file.im -cp po-uilib.jar:. ggc.app.App
   ```
   Here, `-Dimport=file.im` specifies the file to import upon startup.

## Project Structure

- **ggc.core**: Contains core domain classes, including `Product`, `Partner`, `Transaction`, and `WarehouseManager`.
- **ggc.app**: Implements the main application entry point (`App.java`) and handles user interaction via commands and menus.
- **ggc.app.main**: Manages primary commands for saving/loading, date management, and accessing submenus.
- **ggc.app.products**: Contains commands for managing and viewing product information.
- **ggc.app.partners**: Manages partner details, loyalty statuses, and transactions.
- **ggc.app.transactions**: Handles commands for processing different transaction types (purchases, sales, and breakdowns).
- **ggc.app.lookups**: Implements query functions to fetch information based on specified criteria.

## Key Functionalities

1. **File Management**: Supports saving and loading the application state using serialization.
2. **Date Management**: Manages the system date to calculate transaction deadlines and loyalty adjustments.
3. **Notification System**: Alerts partners about changes in product stock or availability of cheaper options.
4. **Loyalty Rewards**: Tracks partner activity, calculates rewards, and applies penalties based on payment history.

---

This project is part of an Object-Oriented Programming course and demonstrates advanced inventory management principles, client interaction, and reward-based systems.
