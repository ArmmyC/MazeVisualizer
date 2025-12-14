package cpe231.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Maze Class to manage maze, start point, end point
public class Maze {
    private int row; // Row Size
    private int column; // Column Size
    private Integer[][] grid; // Grid to hold the maze

    private Block start; // Start point
    private Block end; // End Point

    // Constructor
    public Maze(String filename) throws FileNotFoundException {
        loadMaze(filename);
    }

    // Load maze from file
    private void loadMaze(String filename) throws FileNotFoundException {
        File file = new File(filename);
        List<Integer[]> temp = new ArrayList<>();

        try (Scanner sc = new Scanner(file)) {
            int currentRow = 0; // Track current Row

            // Scan file line by line
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                List<Integer> rowList = new ArrayList<>();
                StringBuilder numBuffer = new StringBuilder();
                int currentCol = 0;

                // Scan line char by char
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);

                    // Buffer to handle two digit value ex."10","15"
                    // append 1 and 0 as character and parseInt it to get 10 as integer
                    if (Character.isDigit(ch)) {
                        numBuffer.append(ch);
                    } else {
                        if (numBuffer.length() > 0) {
                            int weight = Integer.parseInt(numBuffer.toString());
                            rowList.add(weight);
                            numBuffer.setLength(0);
                            currentCol++;
                        }

                        // -1 for wall (#)
                        // 0 for start,end (S,G)
                        switch (ch) {
                            case '#':
                                rowList.add(-1);
                                currentCol++;
                                break;
                            case 'S':
                                this.start = new Block(currentRow, currentCol, 0);
                                rowList.add(0);
                                currentCol++;
                                break;
                            case 'G':
                                this.end = new Block(currentRow, currentCol, 0);
                                rowList.add(0);
                                currentCol++;
                                break;
                            default:
                                break;
                        }
                    }
                }

                // parse last numBuffer
                if (numBuffer.length() > 0) {
                    int weight = Integer.parseInt(numBuffer.toString());
                    rowList.add(weight);
                }

                // add row array to temp list
                if (!rowList.isEmpty()) {
                    Integer[] rowArray = new Integer[rowList.size()];
                    rowList.toArray(rowArray);
                    temp.add(rowArray);
                    currentRow++;
                }
            }
        }

        // Set value to class
        // Convert temp list to grid array
        this.row = temp.size();
        if (this.row > 0) {
            this.column = temp.get(0).length;
            this.grid = new Integer[row][column];
            for (int i = 0; i < row; i++) {
                grid[i] = temp.get(i);
            }
        }
    }

    // Getters
    public int getRows() {
        return row;
    }

    public int getColumns() {
        return column;
    }

    public Integer[][] getGrid() {
        return grid;
    }

    public Block getStart() {
        return start;
    }

    public Block getEnd() {
        return end;
    }

    // Print Maze for Debugging
    public void printMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (grid[i][j] == -1)
                    System.out.print(" [#] ");
                else if (start != null && i == start.getRow() && j == start.getCol())
                    System.out.print(" [S] ");
                else if (end != null && i == end.getRow() && j == end.getCol())
                    System.out.print(" [G] ");
                else
                    System.out.printf(" %3d ", grid[i][j]);
            }
            System.out.println();
        }
    }
}