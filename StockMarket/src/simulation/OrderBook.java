package simulation;

import orders.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Represents the order book for a specific company.
public class OrderBook {
    private final String companyName;
    private int lastSalePrice = -1;
    private ArrayList<Order> buyOrders = new ArrayList<>();
    private ArrayList<Order> sellOrders = new ArrayList<>();
    private long nextOrderNumber = 0;

    public OrderBook(String companyName) {
        this.companyName = companyName;
    }

    // GETTERS AND SETTERS.
    public String getCompanyName() {
        return companyName;
    }

    public int getLastSalePrice() {
        return lastSalePrice;
    }

    // OTHER FUNCTIONALITIES.
    public void addSalePrice(int price) {
        lastSalePrice = price;
    }

    private static boolean validateOrder(Order order) {
        if (order.isBuy())
            return order.getPrice() >= 0 &&
                    order.getInvestor().getWalletCash() >= order.getPrice() * order.getQuantity();
        else {
            return order.getPrice() >= 0 && order.getInvestor().getStocks().containsKey(order.getId())
                     && order.getInvestor().getStocks().get(order.getId()) >= order.getQuantity();
        }
    }

    public void addOrder(Order order) {
        assert validateOrder(order) : "Invalid order: " + order;
        order.setOrderNumber(nextOrderNumber++);

        Comparator<Order> c = new Comparator<>() {
            public int compare(Order o1, Order o2) {
                if (o1.getPrice() != o2.getPrice()) {
                    return order.isBuy() ? o2.getPrice() - o1.getPrice() : o1.getPrice() - o2.getPrice();
                } else {
                    return o1.getRoundWhenAdded() - o2.getRoundWhenAdded();
                }
            }
        };

        ArrayList<Order> orderList = order.isBuy() ? buyOrders : sellOrders;
        int insertIdx = Collections.binarySearch(orderList, order, c);

        if (insertIdx < 0) {
            insertIdx = -(insertIdx + 1);
        } else {
            // Insert at the end of the block of equal prices.
            while (insertIdx < orderList.size() && c.compare(orderList.get(insertIdx), order) == 0) {
                insertIdx++;
            }
        }

        orderList.add(insertIdx, order);
    }

    // PROCESSING ORDERS.
    private void processOrder(Order buyOrder, Order sellOrder) {
        int price = (buyOrder.getOrderNumber() > sellOrder.getOrderNumber()) ?
                buyOrder.getPrice() : sellOrder.getPrice();

        if (sellOrder.getInvestor().getStocks().containsKey(sellOrder.getId())) {
            int quantity = Math.min(Math.min(Math.min(buyOrder.getQuantity(), sellOrder.getQuantity()),
                            buyOrder.getInvestor().getWalletCash() / price),
                            sellOrder.getInvestor().getStocks().get(sellOrder.getId()));
            if (quantity > 0) {
                if (Simulation.getPrintToStrings())
                    System.out.println("Transaction: " + buyOrder.getInvestor().toString() + "bought " + quantity +
                        " " + sellOrder.getId() + " stocks from " + sellOrder.getInvestor().toString() +
                        "for " + price + "$ each.");
                // Manipulate the cash and stock of the investors after transaction.
                buyOrder.getInvestor().removeCash(price * quantity);
                sellOrder.getInvestor().addCash(price * quantity);
                buyOrder.getInvestor().addStock(sellOrder.getId(), quantity);
                sellOrder.getInvestor().removeStock(sellOrder.getId(), quantity);

                addSalePrice(price);
                buyOrder.setQuantity(buyOrder.getQuantity() - quantity);
                sellOrder.setQuantity(sellOrder.getQuantity() - quantity);
            }
        }
    }
    public void processOrders() {
        int j = 0;
        for (int i = 0; i < buyOrders.size(); i++) {
            if (buyOrders.get(i).getInvestor().getWalletCash() <
                    buyOrders.get(i).getPrice() * buyOrders.get(i).getQuantity())   // If the investor doesn't have enough cash.
                buyOrders.get(i).setQuantity(0);     // Will get deleted.
            else {
                while (j < sellOrders.size() && buyOrders.get(i).getPrice() >= sellOrders.get(j).getPrice()) {
                    if (sellOrders.get(j).getInvestor().getStocks().get(sellOrders.get(j).getId()) <
                            sellOrders.get(j).getQuantity())   // If the investor doesn't have enough stocks.
                        sellOrders.remove(j);
                    else if (buyOrders.get(i).getInvestor() != sellOrders.get(j).getInvestor()) {
                        processOrder(buyOrders.get(i), sellOrders.get(j));
                        j++;
                    } else
                        j++;
                }
            }
        }
        buyOrders.removeIf(Order::shouldBeDeleted);
        sellOrders.removeIf(Order::shouldBeDeleted);

        if (Simulation.getLimitQueueSize()) {
            //We delete orders with non-competitive prices to not degrade performance.
            if (sellOrders.size() > 1000)
                sellOrders.subList(0, 1000).clear();
            else if (buyOrders.size() > 1000)
                buyOrders.subList(0, 1000).clear();
        }
    }
}