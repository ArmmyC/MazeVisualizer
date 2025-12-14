package cpe231.helper;

import java.util.List;

import cpe231.maze.Block;

// DTO : Data Transfer Object
// Keep data from algorithm
public class MazeResult {
    private final List<Block> path;
    private final int totalWeight;
    private final long executionTimeNs;
    private final int stepsCount;

    // Constructor
    public MazeResult(List<Block> path, int totalWeight, long executionTimeNs, int stepsCount) { // <--- อัปเดต
        this.path = path;
        this.totalWeight = totalWeight;
        this.executionTimeNs = executionTimeNs;
        this.stepsCount = stepsCount;
    }

    // Getters
    public List<Block> getPath() {
        return path;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "MazeResult: path size=" + path.size() +
                ", cost=" + totalWeight +
                ", time=" + executionTimeNs + "ms" +
                ", steps=" + stepsCount;
    }
}