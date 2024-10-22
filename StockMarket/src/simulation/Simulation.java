package simulation;

import investors.Investor;
import investors.RandomInvestor;
import investors.SMAInvestor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// Simulation creates an order book for every stock option. Order books have data structures for
// managing the orders. Simulation also has a list of investors that will make decisions based on
// the type of investor.
public class Simulation {
    private static int numRounds;
    private static int currRound;
    private static ArrayList<Investor> investors = new ArrayList<>();
    private static ArrayList<OrderBook> orderBooks = new ArrayList<>();
    private static final boolean printToStrings = false; // Turns on and off every to string method.
    private static final boolean limitQueueSize = true; // Turn on and off the queue size limit to not degrade performance.

    // GETTERS AND SETTERS.

    public static ArrayList<Investor> getInvestors() {
        return investors;
    }
    public static boolean getLimitQueueSize() {
        return limitQueueSize;
    }

    public static boolean getPrintToStrings() {
        return printToStrings;
    }
    public static int getCurrRound() {
        return currRound;
    }

    public static ArrayList<OrderBook> getOrderBooks() {
        return orderBooks;
    }

    // READ DATA.
    private static void parseInvestors(String lineNumberOfInv, String lineWallet) {
        String[] letters = lineNumberOfInv.split(" "), wallets = lineWallet.split(" ");
        int startingCash = Integer.parseInt(wallets[0]), idx = 0;

        assert startingCash > 0 : "Invalid starting cash: " + startingCash;

        for (String letter : letters) {
            Investor inv = new RandomInvestor(startingCash, idx);
            if (letter.equals("R"))
                inv = new RandomInvestor(startingCash, idx++);
            else if (letter.equals("S"))
                inv = new SMAInvestor(startingCash, idx++);
            for (int i = 1; i < wallets.length; i++) {
                String[] name = wallets[i].split(":");

                boolean found = false;
                // Check if the stock name is valid.
                for (OrderBook orderBook : orderBooks) {
                    if (orderBook.getCompanyName().equals(name[0])) {
                        found = true;
                        break;
                    }

                }
                assert found : "Invalid stock name: " + name[0];

                inv.addStock(name[0], Integer.parseInt(name[1]));
            }
            investors.add(inv);
        }
    }

    private static void parseOrderBooks(String line) {
        String[] pairs = line.split(" ");
        for (String pair : pairs) {
            String[] parts = pair.split(":");
            String companyName = parts[0];
            int lastPrice = Integer.parseInt(parts[1]);

            OrderBook orderBook = new OrderBook(companyName);

            orderBook.addSalePrice(lastPrice);
            orderBooks.add(orderBook);
        }
    }

    // I assume that there is at least one investor and one stock option. Otherwise, the simulation
    // makes less sense.
    private static void readData(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();

        while (line.charAt(0) == '#') line = scanner.nextLine();
        // Get the investors.
        String investorsLine = line;
        line = scanner.nextLine();

        while (line.charAt(0) == '#') line = scanner.nextLine();
        // Create the order books.
        parseOrderBooks(line);
        line = scanner.nextLine();

        while (line.charAt(0) == '#') line = scanner.nextLine();
        // Get the starting values of wallets.
        parseInvestors(investorsLine, line);
    }
    
    // RUN THE SIMULATION.
    public static void clearData() {
        investors.clear();
        orderBooks.clear();
        currRound = 1;
    }
    public static void run(String filename, int numRounds) throws FileNotFoundException {
        Simulation.numRounds = numRounds;
        readData(filename);

        System.out.println("Printing of events is currently turned " + (printToStrings ? "ON." : "OFF.") +
                " To turn it " + (printToStrings ? "OFF" : "ON") +
                " change the value of printToStrings in simulation.Simulation");
        System.out.println("Performance optimization (orders queue size limit) is currently turned "
                + (limitQueueSize ? "ON." : "OFF.") + " To turn it " + (limitQueueSize ? "OFF" : "ON") +
                " change the value of limitQueueSize in simulation.Simulation");
        for (int i = 1; i < numRounds; i++) {
            if (printToStrings)
                System.out.println("***ROUND " + currRound + " BEGINS***");

            ArrayList<Investor> invShuffle = new ArrayList<>(investors);
            Collections.shuffle(invShuffle);

            for (Investor investor : invShuffle) {
                investor.makeDecision();
            }
            for (OrderBook orderBook : orderBooks) {
                orderBook.processOrders();
            }

            Simulation.currRound++;
        }
        System.out.println("***PRINTING THE WALLET OF EACH INVESTOR***");
        for (Investor investor : investors) {
            investor.printWallet();
        }
        System.out.println("***SIMULATION ENDS***");
    }
}
