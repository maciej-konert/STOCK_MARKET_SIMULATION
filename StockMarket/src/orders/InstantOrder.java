package orders;

import investors.Investor;

public class InstantOrder extends Order {
    public InstantOrder(boolean isBuy, int quantity, String id, int price, Investor investor) {
        super(isBuy, quantity, id, price, investor);
    }

    // We always want to delete the instant order.
    @Override
    public boolean shouldBeDeleted() {
        return true;
    }
}
