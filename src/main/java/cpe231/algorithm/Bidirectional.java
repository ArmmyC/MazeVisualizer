package cpe231.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import cpe231.helper.MazeResult;
import cpe231.maze.Block;

// Bidirectional Dijkstra Algorithm
// Using Dijkstra at the start and the end at the same time
public class Bidirectional implements MazeStrategy<Integer> {

    private int ROWS; // Row Size
    private int COLS; // Col Size

    // Possible Direction
    // (-1,0) : left
    // (1,0) : right
    // (0,-1) : down
    // (0,1) : up
    private static final int[][] DIRS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    @Override
    public MazeResult findShortestPath(Integer[][] maze, Block start, Block end) {

        // to track time
        long startTime = System.nanoTime();

        // to track step
        int stepsCount = 0;

        ROWS = maze.length;
        COLS = maze[0].length;

        PriorityQueue<Block> pqStart = new PriorityQueue<>();
        int[][] distStart = new int[ROWS][COLS];
        Map<String, Block> parentStart = new HashMap<>();

        PriorityQueue<Block> pqEnd = new PriorityQueue<>();
        int[][] distEnd = new int[ROWS][COLS];
        Map<String, Block> parentEnd = new HashMap<>();

        // Initialize distances to Infinity
        for (int[] r : distStart) {
            Arrays.fill(r, Integer.MAX_VALUE);
        }
        for (int[] r : distEnd) {
            Arrays.fill(r, Integer.MAX_VALUE);
        }

        // Initialize first point
        pqStart.add(new Block(start.getRow(), start.getCol(), 0));
        distStart[start.getRow()][start.getCol()] = 0;

        pqEnd.add(new Block(end.getRow(), end.getCol(), 0));
        distEnd[end.getRow()][end.getCol()] = 0;

        int totalWeightSoFar = Integer.MAX_VALUE;
        String meetingBlock = null;

        boolean[][] visitedStart = new boolean[ROWS][COLS];
        boolean[][] visitedEnd = new boolean[ROWS][COLS];

        while (!pqStart.isEmpty() && !pqEnd.isEmpty()) {

            // Check current path with the best path found so far
            if (pqStart.peek().getWeight() + pqEnd.peek().getWeight() >= totalWeightSoFar) {
                break;
            }

            // Search from Start
            if (!pqStart.isEmpty()) {
                // Get current block
                Block current = pqStart.poll();
                int r = current.getRow();
                int c = current.getCol();

                if (!visitedStart[r][c]) {
                    visitedStart[r][c] = true;
                    stepsCount++;

                    for (int[] d : DIRS) {
                        int nr = r + d[0];
                        int nc = c + d[1];

                        // Check if it's a wall or not
                        if (isValid(nr, nc, maze)) {
                            int weight = maze[nr][nc];
                            int newDist = distStart[r][c] + weight;
                            String nextBlockKey = nr + "," + nc;
                            if (newDist < distStart[nr][nc]) {
                                distStart[nr][nc] = newDist;
                                parentStart.put(nextBlockKey, current);
                                pqStart.add(new Block(nr, nc, newDist));

                                if (visitedEnd[nr][nc]) {
                                    int totalPath = distStart[nr][nc] + distEnd[nr][nc];
                                    if (totalPath < totalWeightSoFar) {
                                        totalWeightSoFar = totalPath;
                                        meetingBlock = nextBlockKey;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search from End
            if (!pqEnd.isEmpty()) {
                // Get current block
                Block current = pqEnd.poll();
                int r = current.getRow();
                int c = current.getCol();

                if (!visitedEnd[r][c]) {
                    visitedEnd[r][c] = true;
                    stepsCount++;

                    for (int[] d : DIRS) {
                        int nr = r + d[0];
                        int nc = c + d[1];

                        // Check if it's a wall or not
                        if (isValid(nr, nc, maze)) {
                            int weight = maze[nr][nc];
                            int newDist = distEnd[r][c] + weight;
                            String nextBlockKey = nr + "," + nc;
                            if (newDist < distEnd[nr][nc]) {
                                distEnd[nr][nc] = newDist;
                                parentEnd.put(nextBlockKey, current);
                                pqEnd.add(new Block(nr, nc, newDist));

                                if (visitedStart[nr][nc]) {
                                    int totalPath = distStart[nr][nc] + distEnd[nr][nc];
                                    if (totalPath < totalWeightSoFar) {
                                        totalWeightSoFar = totalPath;
                                        meetingBlock = nextBlockKey;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        long executionTimeMs = endTime - startTime;

        // if there's no meeting block between two search
        if (meetingBlock == null) {
            return new MazeResult(Collections.emptyList(), -1, 0, 0);
        }

        List<Block> finalPath = constructPath(meetingBlock, parentStart, parentEnd);
        return new MazeResult(finalPath, totalWeightSoFar, executionTimeMs, stepsCount);
    }

    // Check if wall (wall is -1)
    private boolean isValid(int r, int c, Integer[][] maze) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS && maze[r][c] != -1;
    }

    // Create best path from start search and end search
    private List<Block> constructPath(String meetingBlockKey, Map<String, Block> pStart, Map<String, Block> pEnd) {

        LinkedList<Block> fullPath = new LinkedList<>();

        String currKey = meetingBlockKey;
        while (currKey != null) {
            Block currentBlock = Block.fromKey(currKey, 0);

            fullPath.addFirst(currentBlock);

            Block parentBlock = pStart.get(currKey);

            if (parentBlock == null) {
                currKey = null;
            } else {
                currKey = parentBlock.getRow() + "," + parentBlock.getCol();
            }
        }

        Block currBlockEnd = pEnd.get(meetingBlockKey);

        while (currBlockEnd != null) {
            fullPath.addLast(currBlockEnd);

            Block parentBlock = pEnd.get(currBlockEnd.getRow() + "," + currBlockEnd.getCol());

            if (parentBlock != null) {
                currBlockEnd = parentBlock;
            } else {
                currBlockEnd = null;
            }
        }

        return fullPath;
    }
}