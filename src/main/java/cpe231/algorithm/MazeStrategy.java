package cpe231.algorithm;

import cpe231.helper.MazeResult;
import cpe231.maze.Block;

public interface MazeStrategy<T extends Comparable<T>> {
    public abstract MazeResult findShortestPath(T[][] maze, Block start, Block end);
}
