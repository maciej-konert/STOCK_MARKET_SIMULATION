package investors;

import simulation.OrderBook;
import simulation.Simulation;

public class RandomInvestor extends Investor {

    public RandomInvestor(int walletCash, int index) {
        super(walletCash, index);
    }
    @Override
    public void makeDecision() {
        int whatToDo = getRandomNumber(0, 2); // 0 = buy, 1 = sell, 2 = do nothing.
        if (whatToDo != 2) {
            OrderBook chosenCompany = Simulation.getOrderBooks().get(getRandomNumber(0,
                    Simulation.getOrderBooks().size() - 1));
            OrderType type = OrderType.values()[getRandomNumber(0, OrderType.values().length - 1)];

            int price = getPriceOfStock(chosenCompany.getCompanyName());
            int quantity = (whatToDo == 0) ? howManyStocksToBuy(price) :
                    getRandomNumber(0, walletStocks.get(chosenCompany.getCompanyName()));
            int priceRand = price + getRandomNumber(-10, 10);
            priceRand = Math.max(priceRand, 10);   // We don't want to sell for too small of a price.

            if (quantity > 0) {
                if (type == OrderType.UNTILROUND) {
                    int round = getRandomNumber(Simulation.getCurrRound() + 1, Simulation.getCurrRound() + 20);
                    placeOrder(whatToDo == 0, quantity, chosenCompany.getCompanyName(),
                            priceRand, round);
                } else
                    placeOrder(type, whatToDo == 0, quantity, chosenCompany.getCompanyName(),
                            priceRand);
            }
        } else
            getNumberOfRound();
    }
}
