package cpe231.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import cpe231.helper.AStarNode;
import cpe231.helper.MazeResult;
import cpe231.maze.Block;

// A* Algorithm with 
// Manhattan Heuristic (No Diagonal)
public class AStar implements MazeStrategy<Integer> {

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

        // 1. Start Time Tracking (Nanoseconds)
        long startTime = System.nanoTime();
        int stepsCount = 0;

        ROWS = maze.length;
        COLS = maze[0].length;

        PriorityQueue<AStarNode> pq = new PriorityQueue<>();

        Map<String, String> cameFrom = new HashMap<>();
        int[][] gScore = new int[ROWS][COLS];

        // Initialize cost to Infinity
        for (int[] row : gScore) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        gScore[start.getRow()][start.getCol()] = 0;

        int startH = heuristic(start.getRow(), start.getCol(), end);

        pq.add(new AStarNode(start.getRow(), start.getCol(), 0, startH));
        stepsCount++;

        while (!pq.isEmpty()) {
            AStarNode current = pq.poll();
            int r = current.getRow();
            int c = current.getCol();

            if (r == end.getRow() && c == end.getCol()) {
                long executionTimeNs = System.nanoTime() - startTime;

                List<Block> finalPath = constructPath(current, cameFrom);

                return new MazeResult(finalPath, current.getG(), executionTimeNs, stepsCount);
            }

            for (int[] d : DIRS) {
                int nr = r + d[0];
                int nc = c + d[1];

                if (isValid(nr, nc, ROWS, COLS, maze)) {
                    int weight = maze[nr][nc];
                    int G = gScore[r][c] + weight;

                    if (G < gScore[nr][nc]) {

                        if (gScore[nr][nc] == Integer.MAX_VALUE) {
                            stepsCount++;
                        }

                        cameFrom.put(nr + "," + nc, r + "," + c);
                        gScore[nr][nc] = G;

                        int h = heuristic(nr, nc, end);
                        int f = G + h;

                        pq.add(new AStarNode(nr, nc, G, f));
                    }
                }
            }
        }

        long executionTimeNs = System.nanoTime() - startTime;
        return new MazeResult(Collections.emptyList(), -1, executionTimeNs, stepsCount);
    }

    // Manhattan Distance Heuristic (No Diagonal)
    private int heuristic(int r, int c, Block end) {
        return Math.abs(r - end.getRow()) + Math.abs(c - end.getCol());
    }

    // Check if wall (wall is -1)
    private boolean isValid(int r, int c, int rows, int cols, Integer[][] maze) {
        return r >= 0 && r < rows && c >= 0 && c < cols && maze[r][c] != -1;
    }

    // Create best path from result (Separated for clarity)
    private List<Block> constructPath(AStarNode endNode, Map<String, String> cameFrom) {

        List<Block> finalPath = new LinkedList<>();
        String currentKey = endNode.getRow() + "," + endNode.getCol();

        while (currentKey != null) {
            String[] parts = currentKey.split(",");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);

            // ใช้ 0 เป็น weight ชั่วคราวสำหรับการสร้าง Block
            // (Total weight ถูกเก็บใน endNode.getG() และถูกส่งผ่าน MazeResult)
            finalPath.add(0, new Block(r, c, 0));

            currentKey = cameFrom.get(currentKey);
        }

        return finalPath;
    }
}