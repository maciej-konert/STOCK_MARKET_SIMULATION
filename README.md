# Stock Market Simulation

This project implements a comprehensive stock market simulation system that models transactional behaviors between investors, orders, and market dynamics. The simulation provides insights into how different investment strategies and order types interact within a market environment.

## Project Overview

The Stock Market Simulation creates a realistic model of market transactions by implementing a system where investors with various strategies can place different types of orders to buy and sell stocks. The simulation runs for a specified number of turns, allowing for the observation of market trends, investor behaviors, and transaction outcomes over time.

## Task Description

Complete specifications for the project can be found in:
- `StockMarket/task_en.pdf` (English version)
- `StockMarket/task_pl.pdf` (Polish version)

## Project Structure

The source code is organized into several directories, each responsible for specific aspects of the simulation:

### Investors Directory
Contains implementations of different investor types, each with unique strategies for market participation:
- Investors using Simple Moving Average (SMA) to make buy/sell decisions
- Investors performing random operations.

### Orders Directory
Implements various order types with different execution requirements:
- Immediate execution orders (must be completed in the turn they are placed)
- All-or-nothing orders (must be fully executed or not executed at all)
- Other order varieties with specific execution constraints

### Main Directory
Contains core system components and utilities necessary for the simulation framework.

### Simulation Directory
Houses the central simulation engine that coordinates the execution of market activities, investor actions, and order processing across multiple turns.

### Test Directory
Provides validation utilities and test cases to ensure the system behaves as expected.

## Running the Simulation

To execute the simulation, use the following command:

```bash
java Simulation filename t
```

Where:
- `filename` is the path to the input file (e.g., `StockMarket/input.txt`)
- `t` is the number of turns to run the simulation

The input file contains initial market conditions, investor configurations, and other parameters necessary for the simulation.

## Features

The simulation supports:
- Multiple investor types with distinct trading strategies
- Various order types with different execution requirements
- Dynamic price movements based on market activities
- Transaction logging and market state tracking
- Temporal simulation over a specified number of turns

## Project Evaluation

The implementation received a score of 8.75/10 in official evaluation. The deduction of 1.25 points was primarily due to the omission of one specific order type. The remaining aspects of the simulation met or exceeded the project requirements.

## Design Philosophy

The system was designed with modularity and extensibility in mind, allowing for:
- Easy addition of new investor types with unique strategies
- Implementation of additional order types with specialized execution rules
- Expansion of market dynamics and simulation parameters
