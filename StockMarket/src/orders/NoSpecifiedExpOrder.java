package orders;

import investors.Investor;

// Class represents an order with no specified expiration round.
public class NoSpecifiedExpOrder extends Order {
    public NoSpecifiedExpOrder(boolean isBuy, int quantity, String id, int price, Investor investor) {
        super(isBuy, quantity, id, price, investor);
    }

    @Override
    public boolean shouldBeDeleted() {
        return quantity == 0;
    }
}
