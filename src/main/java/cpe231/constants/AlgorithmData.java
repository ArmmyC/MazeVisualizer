package cpe231.constants;

import java.util.List;

import cpe231.algorithm.AStar;
import cpe231.algorithm.Bidirectional;
import cpe231.algorithm.GeneticAlgorithm;
import cpe231.algorithm.MazeStrategy;

public class AlgorithmData {

    private final String name;
    private final MazeStrategy<Integer> strategy;

    public AlgorithmData(String name, MazeStrategy<Integer> strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public static final List<AlgorithmData> allAlgorithmData = List.of(
            new AlgorithmData("Bidirectional Dijkstra", new Bidirectional()),
            new AlgorithmData("A* ( Manhattan )", new AStar()),
            new AlgorithmData("Genetic Algorithm", new GeneticAlgorithm()));

    public String getName() {
        return name;
    }

    public MazeStrategy<Integer> getStrategy() {
        return strategy;
    }

}