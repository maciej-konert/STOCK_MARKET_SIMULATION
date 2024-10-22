package orders;

import investors.Investor;
import simulation.Simulation;

public abstract class Order {
    private final boolean isBuy;
    protected int quantity;
    private final String id;
    private final int price;
    protected final int roundWhenAdded;
    private long orderNumber;
    private final Investor investor;

    public Order(boolean isBuy, int quantity, String id, int price, Investor investor) {
        this.isBuy = isBuy;
        this.quantity = quantity;
        this.id = id;
        this.price = price;
        this.roundWhenAdded = Simulation.getCurrRound();
        this.investor = investor;
    }

    // GETTERS AND SETTERS.
    public boolean isBuy() {
        return isBuy;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public int getRoundWhenAdded() {
        return roundWhenAdded;
    }

    public Investor getInvestor() {
        return investor;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // OTHER.

    public abstract boolean shouldBeDeleted();

    @Override
    public String toString() {
        return "Order{" +
                "isBuy=" + isBuy +
                ", quantity=" + quantity +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", roundWhenAdded=" + roundWhenAdded +
                '}';
    }
}
