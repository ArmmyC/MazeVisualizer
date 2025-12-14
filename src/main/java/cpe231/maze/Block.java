package cpe231.maze;

// Helper Class to manage a single maze cell
public class Block implements Comparable<Block> {

    private int row; // Row Coords
    private int col; // Column Coords
    private int weight; // Value

    // Constructor
    public Block(int row, int col, int weight) {
        this.row = row;
        this.col = col;
        this.weight = weight;
    }

    // Getters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getWeight() {
        return weight;
    }

    // Setters
    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public static Block fromKey(String key, int weight) {
        String[] parts = key.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        return new Block(row, col, weight);
    }

    // และสำคัญมาก:
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Block block = (Block) o;
        return row == block.row && col == block.col; // เปรียบเทียบที่พิกัด
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col); // ใช้พิกัดในการสร้าง Hash
    }

    // Implement compare between Node
    @Override
    public int compareTo(Block other) {
        return Integer.compare(this.weight, other.weight);
    }

    // toString for debugging
    @Override
    public String toString() {
        return "(" + row + "," + col + " : " + weight + ")";
    }

}