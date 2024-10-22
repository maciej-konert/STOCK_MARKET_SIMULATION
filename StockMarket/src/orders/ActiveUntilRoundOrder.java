package orders;

import simulation.Simulation;
import investors.Investor;

// Class represents an order that is active until a certain specified round.
public class ActiveUntilRoundOrder extends Order {
    private final int roundUntilActive;

    public ActiveUntilRoundOrder(boolean isBuy, int quantity, String id, int price,
                                 int roundUntilActive, Investor investor) {
        super(isBuy, quantity, id, price, investor);
        this.roundUntilActive = roundUntilActive;
    }

    @Override
    public boolean shouldBeDeleted() {
        return roundUntilActive >= Simulation.getCurrRound() || quantity == 0;
    }
}
