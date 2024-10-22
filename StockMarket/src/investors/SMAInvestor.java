package investors;

import simulation.OrderBook;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.HashMap;

public class SMAInvestor extends Investor {
    private static final int smaPeriodLonger = 10;
    private static final int smaPeriodShorter = 5;
    private HashMap<String, ArrayList<Integer>> lastPrices = new HashMap<>();
    public SMAInvestor(int walletCash, int index) {
        super(walletCash, index);
    }

    // SAVING THE LAST PRICES OF THE STOCKS.
    private void saveLastPrice(String stock) {
        ArrayList<Integer> toModify;
        if (!lastPrices.containsKey(stock))
            lastPrices.put(stock, new ArrayList<>());
        toModify = lastPrices.get(stock);

        if (toModify.size() == smaPeriodLonger) {
            toModify.removeFirst();
        }
        toModify.addLast(getPriceOfStock(stock));
        lastPrices.put(stock, toModify);
    }
    // Calculate the SMA of a stock for the last period rounds.
    private double calculateSMA(String stock, int period) {
        if (Simulation.getCurrRound() >= smaPeriodLonger + 1) {
            ArrayList<Integer> prices = lastPrices.get(stock);
            double sum = 0;

            assert prices.size() >= period : "Not enough prices to calculate SMA.";
            for (int i = 0; i < period; i++) {
                sum += prices.get(prices.size() - 1 - i);
            }
            sum /= period;

            return sum;
        } else
            return 0;
    }

    private boolean arePricesStayingTheSame(String stock) {
        ArrayList<Integer> prices = lastPrices.get(stock);
        int lastPrice = prices.getLast();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) != lastPrice)
                return false;
        }
        return true;
    }

    // SMA investor will ask for the last price of every stock in the simulation so that
    // it can calculate the SMA later.
    @Override
    public void makeDecision() {
        OrderBook orderBook = Simulation.getOrderBooks().get(getRandomNumber(0,
                Simulation.getOrderBooks().size() - 1));
        double oldSmaShorter = calculateSMA(orderBook.getCompanyName(), smaPeriodShorter);
        double oldSmaLonger = calculateSMA(orderBook.getCompanyName(), smaPeriodLonger);

        for (OrderBook orderB : Simulation.getOrderBooks())
            saveLastPrice(orderB.getCompanyName());
        if (Simulation.getCurrRound() >= smaPeriodLonger + 2) {
            double smaShorter = calculateSMA(orderBook.getCompanyName(), smaPeriodShorter);
            double smaLonger = calculateSMA(orderBook.getCompanyName(), smaPeriodLonger);

            OrderType type = OrderType.values()[getRandomNumber(0, OrderType.values().length - 1)];
            int price = 0, quantity = 0;
            boolean isBuy = true;
            if (oldSmaShorter <= oldSmaLonger && smaShorter >= smaLonger) { // Buy
                price = getPriceOfStock(orderBook.getCompanyName()) + 1;
                quantity = howManyStocksToBuy(price);
            }   // Sell
            else if ((oldSmaShorter >= oldSmaLonger && smaShorter <= smaLonger)
                    || arePricesStayingTheSame(orderBook.getCompanyName())){
                price = getPriceOfStock(orderBook.getCompanyName()) - 1;
                quantity = getRandomNumber(0, walletStocks.get(orderBook.getCompanyName()));
                isBuy = false;
            }
            price = Math.max(price, 10);

            if (quantity > 0) {
                if (type == OrderType.UNTILROUND) {
                    int round = getRandomNumber(Simulation.getCurrRound() + 4, Simulation.getCurrRound() + 10);
                    placeOrder(isBuy, quantity, orderBook.getCompanyName(), price, round);
                } else
                    placeOrder(type, isBuy, quantity, orderBook.getCompanyName(), price);
            }
        }
    }
}
