package investors;

import orders.*;
import simulation.OrderBook;
import simulation.Simulation;

import java.util.HashMap;
import java.util.Random;

public abstract class Investor {
//    private static int seed = 0;
    protected int walletCash;
    private final int index;
    protected HashMap<String, Integer> walletStocks = new HashMap<>();

    public Investor(int walletCash, int index) {
        this.walletCash = walletCash;
        this.index = index;
    }

    // GETTERS AND SETTERS.
    public int getWalletCash() {
        return walletCash;
    }

    public HashMap<String, Integer> getStocks() {
        return walletStocks;
    }

    // WALLET OPERATIONS.
    public void addCash(int cash) {
        walletCash += cash;
    }

    public void removeCash(int cash) {
        walletCash -= cash;
    }

    public void addStock(String stock, int quantity) {
        if (walletStocks.containsKey(stock))
            walletStocks.put(stock, walletStocks.get(stock) + quantity);
        else {
            walletStocks.put(stock, quantity);
        }
    }

    public void removeStock(String stock, int quantity) {
        walletStocks.put(stock, walletStocks.get(stock) - quantity);
    }

    // INVESTOR AVAILABLE OPERATIONS.
    protected int howManyStocksToBuy(int price) {
        assert price > 0 : "Invalid price: " + price;
        if (walletCash / price / 4 == 0)
            return 0;
        return getRandomNumber(1, walletCash / price / 4);
    }
    protected static int getRandomNumber(int min, int max) {
        Random r = new Random();
//        r.setSeed(++seed);
        return r.nextInt(min, max + 1);
    }
    protected void getNumberOfRound() {
        if (Simulation.getPrintToStrings())
            System.out.println("Investor " + this.index + " has asked for the number of current round."
            + " The number of current round is: " + Simulation.getCurrRound());
    }

    /**
     * This method is used to get the last price of a stock.
     * @param stock The stock name.
     * @return The last price of the stock. -1 if stock has not been sold yet.
     */
    protected int getPriceOfStock(String stock) {
        int price = 0;
        boolean found = false;
        for (OrderBook orderBook : Simulation.getOrderBooks()) {
            if (orderBook.getCompanyName().equals(stock)) {
                price = orderBook.getLastSalePrice();   // Is -1 if the stock has not been sold yet.
                found = true;
            }
        }
        assert found : "Invalid stock name: " + stock;

        if (Simulation.getPrintToStrings()) {
            System.out.printf("Investor " + this.index + " has asked for the last price of " + stock + ".");
            if (price == -1)
                System.out.printf(" The stock " + stock + " has not been sold yet.");
            else
                System.out.printf(" The last price of " + stock + " is: " + price + "$.");
            System.out.printf(" Current round is: " + Simulation.getCurrRound() + "\n");
        }
        return price;
    }

    protected enum OrderType {
        INSTANT,
        NOEXP,
        UNTILROUND
    }

    protected void placeOrder(OrderType type, boolean isBuy, int quantity, String id, int price) {
        if (quantity > 0 && price >= 10) {
            if (Simulation.getPrintToStrings())
                System.out.println("Investor " + this.index + " wants to " + (isBuy ? "buy " : "sell ") +
                        quantity + " " + id + " stocks for " + price + "$ each.");
            Order order = switch (type) {
                case INSTANT -> new InstantOrder(isBuy, quantity, id, price, this);
                case NOEXP -> new NoSpecifiedExpOrder(isBuy, quantity, id, price, this);
                default -> throw new IllegalArgumentException("Invalid order type: " + type);
            };

            for (OrderBook orderBook : Simulation.getOrderBooks()) {
                if (orderBook.getCompanyName().equals(id)) {
                    orderBook.addOrder(order);
                    break;
                }
            }
        }
    }
    // Only for the UntilRoundOrder type.
    protected void placeOrder(boolean isBuy, int quantity, String id, int price, int round) {
        if (quantity > 0 && price >= 10) {
            if (Simulation.getPrintToStrings())
                System.out.println("Investor " + this.index + " wants to " + (isBuy ? "buy " : "sell ") +
                        quantity + " " + id + " stocks for " + price + "$ until round " + round);
            ActiveUntilRoundOrder order = new ActiveUntilRoundOrder(isBuy, quantity, id, price, round, this);

            for (OrderBook orderBook : Simulation.getOrderBooks()) {
                if (orderBook.getCompanyName().equals(id)) {
                    orderBook.addOrder(order);
                    break;
                }
            }
        }
    }

    public abstract void makeDecision();

    @Override
    public String toString() {
        return "Investor " + index + " ";
    }

    public void printWallet() {
        System.out.print("Investor " + this.index + " has " + this.walletCash + "$ in cash and the following stocks: ");
        for (String stock : walletStocks.keySet()) {
            System.out.print(stock + ":" + walletStocks.get(stock) + " ");
        }
        System.out.println();
    }
}
