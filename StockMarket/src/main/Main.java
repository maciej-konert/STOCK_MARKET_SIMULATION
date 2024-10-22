package main;

import simulation.Simulation;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Simulation.run(args[0], Integer.parseInt(args[1]));
    }
}
