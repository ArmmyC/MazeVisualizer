package cpe231.components;

import cpe231.maze.Block;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

// Class to draw Maze using Canvas
public class MazeVisualizer {

    // Cell size
    public static final double CELL_SIZE = 50.0;

    // All components
    private final Canvas mazeCanvas;
    private final Integer[][] gridData;
    private final int rows;
    private final int cols;

    private final Block start;
    private final Block end;

    // Color for draw text
    private final Color TEXT_COLOR = Color.web("#e4e4e4ff");

    // Constructor
    public MazeVisualizer(Integer[][] grid, Block start, Block end) {
        this.gridData = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.start = start;
        this.end = end;

        this.mazeCanvas = new Canvas(cols * CELL_SIZE, rows * CELL_SIZE);

        drawMaze();
    }

    // Method to get color based on value ,start ,end
    private Color getColor(int value, int row, int col) {
        if (value == -1) {
            return Color.web("#2d2d2dff");
        }
        if (row == start.getRow() && col == start.getCol() && value == 0) {
            return Color.GREEN;
        }
        if (row == end.getRow() && col == end.getCol() && value == 0) {
            return Color.RED;
        }

        return Color.rgb(100, 200, 255, 0.5);
    }

    // Method to get label based on value ,start ,end
    private String getLabel(int value, int row, int col) {
        if (value == -1) {
            return "";
        }
        if (row == start.getRow() && col == start.getCol() && value == 0) {
            return "S";
        }
        if (row == end.getRow() && col == end.getCol() && value == 0) {
            return "G";
        }

        return String.valueOf(value);
    }

    // Method to draw maze
    private void drawMaze() {
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("NEXORA", CELL_SIZE * 0.6));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                double x = col * CELL_SIZE;
                double y = row * CELL_SIZE;
                Integer cellValue = gridData[row][col];

                // Fill Background
                Color fillColor = getColor(cellValue, row, col);
                gc.setFill(fillColor);
                gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Fill Label
                String cellLabel = getLabel(cellValue, row, col);
                gc.setFill(TEXT_COLOR);
                gc.fillText(cellLabel, x + CELL_SIZE / 2, y + CELL_SIZE / 2);

            }
        }
    }

    // Method for update single cell
    public void updateCell(int row, int col, Integer newValue) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return;
        }

        gridData[row][col] = newValue;

        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        double x = col * CELL_SIZE;
        double y = row * CELL_SIZE;

        gc.clearRect(x, y, CELL_SIZE, CELL_SIZE);

        Color fillColor = getColor(newValue, row, col);
        gc.setFill(fillColor);
        gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        if (newValue != null && newValue > 0) {
            gc.setFill(Color.web("#CCCCCC"));
            gc.fillText(String.valueOf(newValue),
                    x + CELL_SIZE / 2,
                    y + CELL_SIZE / 2);
        }
    }

    private final Color PATH_COLOR = Color.web("#FFC107"); // สีเหลือง/ส้มสว่าง

    public void drawPath(java.util.List<cpe231.maze.Block> path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        // 1. ล้าง Path เก่าก่อน (Clear เฉพาะ Block ใน Path เก่า)

        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        // 2. วนลูปวาดพื้นหลังสี Path ทับ Block ที่เป็นเส้นทาง
        for (cpe231.maze.Block currentBlock : path) {
            int r = currentBlock.getRow();
            int c = currentBlock.getCol();
            Integer cellValue = gridData[r][c];

            // ข้ามจุดเริ่มต้นและจุดสิ้นสุดเพื่อให้สีเดิม (เขียว/แดง) ยังคงอยู่
            if ((r == start.getRow() && c == start.getCol()) ||
                    (r == end.getRow() && c == end.getCol())) {
                continue;
            }

            double x = c * CELL_SIZE;
            double y = r * CELL_SIZE;

            // A. Fill Background ด้วย PATH_COLOR
            gc.setFill(PATH_COLOR);
            gc.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            // B. Fill Label
            String cellLabel = getLabel(cellValue, r, c);
            gc.setFill(TEXT_COLOR);

            // ตั้งค่า Text Alignment และ Font เหมือนใน drawMaze()
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(new Font("NEXORA", CELL_SIZE * 0.6));

            gc.fillText(cellLabel, x + CELL_SIZE / 2, y + CELL_SIZE / 2);
        }

    }

    public void clearPath() {
        drawMaze();
    }

    public Canvas getView() {
        return mazeCanvas;
    }
}