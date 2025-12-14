package cpe231.algorithm;

import cpe231.helper.MazeResult;
import cpe231.maze.Block;

public class MazeContext<T extends Comparable<T>> {
    private MazeStrategy<T> strategy;

    // Constructor
    public MazeContext(MazeStrategy<T> strategy) {
        this.strategy = strategy;
    }

    // Setter
    public void setStrategy(MazeStrategy<T> strategy) {
        this.strategy = strategy;
    }

    // Getter
    public MazeStrategy<T> getStrategy() {
        return this.strategy;
    }

    // Run Algorithm
    public MazeResult executeMaze(T[][] maze, Block start, Block end) {
        return strategy.findShortestPath(maze, start, end);
    }

}
