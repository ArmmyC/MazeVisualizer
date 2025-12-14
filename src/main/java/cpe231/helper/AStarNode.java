package cpe231.helper;

// Helper class specific to A* to track f, g, and h costs
public class AStarNode implements Comparable<AStarNode> {
    private int row; // Row coords
    private int col; // Col coords
    private int g; // Cost from start
    private int f; // g + heuristic

    // Constructor
    public AStarNode(int row, int col, int g, int f) {
        this.row = row;
        this.col = col;
        this.g = g;
        this.f = f;
    }

    // Getters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getG() {
        return g;
    }

    // Setters
    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    // Implement compareTo for comparing between Node
    @Override
    public int compareTo(AStarNode other) {
        return Integer.compare(this.f, other.f);
    }
}
