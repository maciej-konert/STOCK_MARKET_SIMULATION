package test;

import org.junit.jupiter.api.Test;
import simulation.Simulation;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    // TEST FILE test1.txt

    // Test data reading.
    @Test
    void test1() throws FileNotFoundException {
        Simulation.run("test1.txt", 1);
        assertEquals(5, Simulation.getInvestors().size());
        assertEquals(3, Simulation.getOrderBooks().size());
        assertEquals("A", Simulation.getOrderBooks().get(0).getCompanyName());
        assertEquals("B", Simulation.getOrderBooks().get(1).getCompanyName());
        assertEquals("C", Simulation.getOrderBooks().get(2).getCompanyName());
        Simulation.clearData();
    }

    // Check if the number of stocks and amount of money in circulation stays the same.
    @Test
    void test2() throws FileNotFoundException {
        Simulation.run("test1.txt", 10000);
        Integer numStocks = 0;
        int money = 0;
        for (int i = 0; i < Simulation.getInvestors().size(); i++) {
            for (Integer integ : Simulation.getInvestors().get(i).getStocks().values())
                numStocks += integ;
            money += Simulation.getInvestors().get(i).getWalletCash();
        }
        assertEquals(5 * 60, numStocks);
        assertEquals(5 * 10000, money);
        Simulation.clearData();
    }

    // TEST FILE test2.txt

    // Test data reading.
    @Test
    void test3() throws FileNotFoundException {
        Simulation.run("test2.txt", 1);
        assertEquals(18, Simulation.getInvestors().size());
        assertEquals(4, Simulation.getOrderBooks().size());
        assertEquals("A", Simulation.getOrderBooks().get(0).getCompanyName());
        assertEquals("B", Simulation.getOrderBooks().get(1).getCompanyName());
        assertEquals("C", Simulation.getOrderBooks().get(2).getCompanyName());

        Simulation.clearData();

    }

    // Check if the number of stocks and amount of money in circulation stays the same.
    @Test
    void test4() throws FileNotFoundException {
        Simulation.run("test2.txt", 10000);
        Integer numStocks = 0;
        int money = 0;
        for (int i = 0; i < Simulation.getInvestors().size(); i++) {
            for (Integer integ : Simulation.getInvestors().get(i).getStocks().values())
                numStocks += integ;
            money += Simulation.getInvestors().get(i).getWalletCash();
        }
        assertEquals(18 * 200, numStocks);
        assertEquals(18 * 50000, money);
        Simulation.clearData();
    }

    // TEST FILE test3.txt

    // We check if it asserts when the wallet contains unavailable stocks.
    @Test
    void test5() throws FileNotFoundException {
        assertThrows(AssertionError.class, () -> Simulation.run("test3.txt", 1));
        Simulation.clearData();
    }

    // TEST FILE test4.txt

    // We check if it asserts when the wallet money amount is negative.
    @Test
    void test6() throws FileNotFoundException {
        assertThrows(AssertionError.class, () -> Simulation.run("test4.txt", 1));
        Simulation.clearData();
    }


}