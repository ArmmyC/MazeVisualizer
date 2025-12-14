package cpe231.ui;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpe231.algorithm.GeneticAlgorithm;
import cpe231.algorithm.MazeContext;
import cpe231.algorithm.MazeStrategy;
import cpe231.components.MazeVisualizer;
import cpe231.components.ZoomableScrollPane;
import cpe231.helper.MazeResult;
import cpe231.maze.Block;
import cpe231.maze.Maze;
import cpe231.responsives.Responsive;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

// MainUI contain every component, handlers for this project
public class MainUI {

        // GeneticAlgorithm Default
        private final int DEFAULT_POP_SIZE = 200;
        private final double DEFAULT_MUT_RATE = 0.05;
        private final double DEFAULT_CROSS_RATE = 0.7;
        private final int DEFAULT_MAX_GEN = 100;
        private final int DEFAULT_MAX_MOVE = -1;

        // Algorithm Handler
        private final MazeContext<Integer> solver = new MazeContext<>(null);

        // Maze Handler
        private Maze currentMaze;
        private MazeVisualizer mazeVisualizer;

        private final HBox rootPane = new HBox();
        private final ControlPanelUI controlPanel = new ControlPanelUI();

        private final Label mazeDisplayLabel = new Label("MAZE DISPLAY");

        private final ZoomableScrollPane mazePane = new ZoomableScrollPane(mazeDisplayLabel);

        // Constructor
        public MainUI() {
                setupLayout();
                setupStyle();
                setupResponsive();

                setupChooserHandler();
                setupStartButtonHandler();
        }

        // Setup layout, sizes, add child,etc.
        private void setupLayout() {
                var controlPane = controlPanel.getView();
                controlPane.prefHeightProperty().bind(rootPane.heightProperty().multiply(1));
                controlPane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.2));
                mazePane.prefHeightProperty().bind(rootPane.heightProperty().multiply(1));
                mazePane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.8));

                HBox.setHgrow(mazePane, Priority.ALWAYS);
                mazePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                rootPane.getChildren().addAll(mazePane, controlPane);
        }

        // Setup style, alignment
        private void setupStyle() {
                HBox.setHgrow(mazePane, Priority.ALWAYS);
                VBox.setVgrow(mazePane, Priority.ALWAYS);

                mazePane.getStyleClass().add("maze-panel");
                mazeDisplayLabel.getStyleClass().add("maze-display-label");
        }

        // Set up responsiveness
        private void setupResponsive() {
                Responsive.bindResponsiveScaling(
                                mazeDisplayLabel,
                                mazePane, 100,
                                0,
                                0,
                                0, 0);
        }

        // Store last selected maze path for use in lambdas
        private String lastSelectedPath = null;

        // Setup handler for menuButton
        private void setupChooserHandler() {
                // get mazeChooser
                var mazeChooser = controlPanel.getMazeChooser();
                Map<MenuItem, String> mazeMap = new HashMap<>();

                // Loop through mazeChooser to initalize setOnAction
                for (var item : mazeChooser.getItems()) {
                        String path = (String) item.getUserData(); // get path
                        mazeMap.put(item, path);
                        item.setOnAction(event -> {
                                var selectedText = item.getText();
                                mazeChooser.setText(selectedText);

                                String selectedPath = mazeMap.get(item);
                                this.lastSelectedPath = selectedPath;
                                loadMaze(selectedPath);
                                controlPanel.getStatBoxUI().updateStats(0, 0, 0);
                        });
                }

                var algorithmChooser = controlPanel.getAlgorithmChooser();
                Map<MenuItem, MazeStrategy<Integer>> algorithmMap = new HashMap<>();

                for (var item : algorithmChooser.getItems()) {
                        MazeStrategy<Integer> strategy = (MazeStrategy<Integer>) item.getUserData();
                        algorithmMap.put(item, strategy);
                        item.setOnAction(event -> {
                                var selectedText = item.getText();
                                algorithmChooser.setText(selectedText);
                                loadMaze(lastSelectedPath);
                                controlPanel.getStatBoxUI().updateStats(0, 0, 0);

                                var selectedStrategy = algorithmMap.get(item);
                                solver.setStrategy(selectedStrategy);
                                boolean isGenetic = selectedStrategy instanceof GeneticAlgorithm;
                                controlPanel.getParameterBox().setVisible(isGenetic);
                                controlPanel.getParameterBox().setManaged(isGenetic);
                        });
                }
        }

        private void setupStartButtonHandler() {
                controlPanel.getStartButton().setOnAction(event -> {
                        if (currentMaze == null || mazeVisualizer == null) {
                                System.err.println("Error: Please select and load a maze first.");
                                return;
                        }
                        if (solver.getStrategy() == null) {
                                System.err.println("Error: Please select an algorithm.");
                                return;
                        }

                        if (solver.getStrategy() instanceof GeneticAlgorithm) {
                                try {
                                        String popText = controlPanel.getPopulationSize().getText();
                                        String mutText = controlPanel.getMutationRate().getText();
                                        String crossText = controlPanel.getCrossOverRate().getText();
                                        String genText = controlPanel.getMaxGeneration().getText();
                                        String moveText = controlPanel.getMaxMoves().getText();

                                        int newPopulationSize = safeParseInt(popText, DEFAULT_POP_SIZE);
                                        double newMutationRate = safeParseDouble(mutText, DEFAULT_MUT_RATE);
                                        double newCrossOverRate = safeParseDouble(crossText, DEFAULT_CROSS_RATE);
                                        int newMaxGeneration = safeParseInt(genText, DEFAULT_MAX_GEN);
                                        int newMaxMove = safeParseInt(moveText, DEFAULT_MAX_MOVE);

                                        GeneticAlgorithm gaSolver = (GeneticAlgorithm) solver.getStrategy();

                                        gaSolver.setPopulationSize(newPopulationSize);
                                        gaSolver.setMutationRate(newMutationRate);
                                        gaSolver.setCrossoverRate(newCrossOverRate);
                                        gaSolver.setMaxGenerations(newMaxGeneration);
                                        gaSolver.setMaxMoves(newMaxMove);
                                } catch (NumberFormatException e) {
                                        System.err.println(
                                                        "Error: Invalid number format in Genetic Algorithm parameters.");
                                        return;
                                }
                        }

                        try {
                                MazeResult result = solver.executeMaze(currentMaze.getGrid(),
                                                currentMaze.getStart(),
                                                currentMaze.getEnd());

                                if (result != null && result.getPath() != null) {
                                        loadMaze(lastSelectedPath);
                                        List<Block> path = result.getPath();
                                        if (!path.isEmpty()) {
                                                mazeVisualizer.drawPath(path);
                                                controlPanel.getStatBoxUI().updateStats(result.getExecutionTimeNs(),
                                                                result.getStepsCount(), result.getTotalWeight());
                                        }
                                }

                        } catch (Exception e) {
                                System.err.println("An error occurred during solving: " + e.getMessage());
                        }
                });
        }

        private void loadMaze(String mazeFilePath) {
                try {
                        String fullPath = getClass().getResource(mazeFilePath).getPath();
                        this.currentMaze = new Maze(fullPath);

                        this.mazeVisualizer = new MazeVisualizer(this.currentMaze.getGrid(),
                                        this.currentMaze.getStart(), this.currentMaze.getEnd());
                        Canvas mazeCanvas = this.mazeVisualizer.getView();

                        mazePane.setNode(mazeCanvas);
                } catch (FileNotFoundException e) {
                        System.err.println("Error: Maze file not found at " + mazeFilePath);
                } catch (NullPointerException e) {
                        System.err.println("Error: Resource file path is invalid at " + mazeFilePath);
                }
        }

        private int safeParseInt(String text, int defaultValue) {
                if (text == null || text.trim().isEmpty()) {
                        return defaultValue;
                }
                try {
                        return Integer.parseInt(text.trim());
                } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid integer input. Using default value: " + defaultValue);
                        return defaultValue;
                }
        }

        private double safeParseDouble(String text, double defaultValue) {
                if (text == null || text.trim().isEmpty()) {
                        return defaultValue;
                }
                try {
                        return Double.parseDouble(text.trim());
                } catch (NumberFormatException e) {
                        System.err.println("Warning: Invalid double input. Using default value: " + defaultValue);
                        return defaultValue;
                }
        }

        public HBox getRootPane() {
                return rootPane;
        }
}
