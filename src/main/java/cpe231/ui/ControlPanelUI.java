package cpe231.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import cpe231.constants.AlgorithmData;
import cpe231.constants.MazeData;
import cpe231.responsives.Responsive;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ControlPanelUI {

    // Main Pane
    private final VBox controlPane = new VBox();

    // Components
    private final Label controlLabel = new Label("CONTROL PANEL");
    private final Button startButton = new Button("Start Solving");
    private final Separator separator1 = new Separator();
    private final Separator separator2 = new Separator();
    private final Region spacer = new Region();

    // Components for Maze Options
    private final Label mazeLabel = new Label("Select Maze : ");
    private final MenuButton mazeChooser = new MenuButton("Options");

    // Components for Algorithm Options
    private final Label algorithmLabel = new Label("Select Algorithm : ");
    private final MenuButton algorithmChooser = new MenuButton("Options");

    // Components for GeneticAlgorithm Parameter Input
    // Population Sizes
    private final Label populationLabel = new Label("Population Size (default: 200) :");
    private final TextField populationSize = new TextField("200");
    private final VBox populationBox = new VBox(populationLabel, populationSize);
    // Mutation Rates
    private final Label mutationLabel = new Label("Mutation Rate (default: 0.05) :");
    private final TextField mutationRate = new TextField("0.05");
    private final VBox mutationBox = new VBox(mutationLabel, mutationRate);
    // Cross over Rates
    private final Label crossOverLabel = new Label("Cross-over Rate (default: 0.7) :");
    private final TextField crossOverRate = new TextField("0.7");
    private final VBox crossOverBox = new VBox(crossOverLabel, crossOverRate);
    // Max Generations
    private final Label generationLabel = new Label("Max Generations (default: 100) :");
    private final TextField maxGeneration = new TextField("100");
    private final VBox generationBox = new VBox(generationLabel, maxGeneration);
    // Max Moves
    private final Label movesLabel = new Label("Max Moves (default: -1) :");
    private final TextField maxMoves = new TextField("-1");
    private final VBox movesBox = new VBox(movesLabel, maxMoves);
    private final VBox parameterBox = new VBox(10, populationBox, mutationBox, crossOverBox, generationBox,
            movesBox);

    // Stat box
    StatBoxUI statBoxUI = new StatBoxUI();

    public ControlPanelUI() {
        setupChooser();
        setupLayout();
        setupStyle();
        setupResponsive();
    }

    private void setupLayout() {

        VBox statBox = statBoxUI.getView();

        mazeLabel.setMaxWidth(Double.MAX_VALUE);
        mazeChooser.setMaxWidth(Double.MAX_VALUE);
        algorithmLabel.setMaxWidth(Double.MAX_VALUE);
        algorithmChooser.setMaxWidth(Double.MAX_VALUE);
        startButton.setMaxWidth(Double.MAX_VALUE);
        parameterBox.setVisible(false);
        parameterBox.setManaged(false);

        VBox.setVgrow(spacer, Priority.ALWAYS);
        mazeLabel.setAlignment(Pos.CENTER_LEFT);
        algorithmLabel.setAlignment(Pos.CENTER_LEFT);
        mazeChooser.setAlignment(Pos.CENTER_LEFT);
        algorithmChooser.setAlignment(Pos.CENTER_LEFT);
        startButton.setAlignment(Pos.CENTER);

        populationSize.getStyleClass().add("param-text-field");
        mutationRate.getStyleClass().add("param-text-field");
        crossOverRate.getStyleClass().add("param-text-field");
        maxGeneration.getStyleClass().add("param-text-field");
        maxMoves.getStyleClass().add("param-text-field");

        controlPane.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(controlPane, Priority.ALWAYS);
        controlPane.getChildren().addAll(
                controlLabel,
                separator1,
                mazeLabel, mazeChooser,
                separator2,
                algorithmLabel, algorithmChooser,
                parameterBox,
                spacer,
                statBox,
                startButton);
    }

    private void setupStyle() {
        controlPane.getStyleClass().add("control-panel");
        controlLabel.getStyleClass().add("main-label");
        mazeLabel.getStyleClass().add("main-label");
        algorithmLabel.getStyleClass().add("main-label");
        mazeChooser.getStyleClass().add("chooser");
        algorithmChooser.getStyleClass().add("chooser");
        startButton.getStyleClass().add("button");

        populationLabel.getStyleClass().add("main-label");
        mutationLabel.getStyleClass().add("main-label");
        crossOverLabel.getStyleClass().add("main-label");
        generationLabel.getStyleClass().add("main-label");
        movesLabel.getStyleClass().add("main-label");
    }

    private void setupResponsive() {

        // Parameter:
        // fontScaleFactor,
        // paddingTopScaleFactor,
        // paddingRightScaleFactor,
        // paddingBottomScaleFactor,
        // paddingLeftScaleFactor
        Map<Region, double[]> Bindings = new LinkedHashMap<>();

        Bindings.put(controlPane, new double[] { -1, 10, 10, 10, 10 });
        Bindings.put(controlLabel, new double[] { 10, 0, 0, 10, 0 });
        Bindings.put(mazeLabel, new double[] { 15, 10, 0, 20, 0 });
        Bindings.put(algorithmLabel, new double[] { 15, 10, 0, 20, 0 });
        Bindings.put(mazeChooser, new double[] { 18, 150, 150, 150, 150 });
        Bindings.put(algorithmChooser, new double[] { 18, 150, 150, 150, 150 });
        Bindings.put(separator2, new double[] { -1, 10, 0, 0, 0 });
        Bindings.put(startButton, new double[] { 12, 40, 40, 40, 40 });

        // Parameter TextFields
        double[] textFieldParams = new double[] { 20, 100, 100, 100, 100 };
        Bindings.put(populationSize, textFieldParams);
        Bindings.put(mutationRate, textFieldParams);
        Bindings.put(crossOverRate, textFieldParams);
        Bindings.put(maxGeneration, textFieldParams);
        Bindings.put(maxMoves, textFieldParams);
        Bindings.put(populationLabel, textFieldParams);
        Bindings.put(mutationLabel, textFieldParams);
        Bindings.put(crossOverLabel, textFieldParams);
        Bindings.put(generationLabel, textFieldParams);
        Bindings.put(movesLabel, textFieldParams);
        Bindings.put(parameterBox, new double[] { -1, 10, 0, 0, 0 });

        double[] statTextParams = new double[] { 20, 200, 200, 200, 200 };
        Bindings.put(statBoxUI.getDistLabel(), statTextParams);
        Bindings.put(statBoxUI.getStepLabel(), statTextParams);
        Bindings.put(statBoxUI.getTimeLabel(), statTextParams);
        Bindings.put(statBoxUI.getStatBox(), new double[] { -1, 30, 30, 30, 30 });
        Bindings.put(statBoxUI.getView(), new double[] { -1, 0, 0, 20, 0 });

        for (Map.Entry<Region, double[]> entry : Bindings.entrySet()) {
            Region region = entry.getKey();
            double[] params = entry.getValue();

            Responsive.bindResponsiveScaling(
                    region,
                    controlPane,
                    params[0],
                    params[1],
                    params[2],
                    params[3],
                    params[4]);
        }
    }

    private void setupChooser() {
        // Maze
        for (MazeData data : MazeData.allMazeData) {
            MenuItem item = new MenuItem(data.getName());

            item.setUserData(data.getPath());

            mazeChooser.getItems().add(item);
        }

        // Algorithm
        for (AlgorithmData data : AlgorithmData.allAlgorithmData) {
            MenuItem item = new MenuItem(data.getName());

            item.setUserData(data.getStrategy());

            algorithmChooser.getItems().add(item);
        }
    }

    public VBox getView() {
        return controlPane;
    }

    public Button getStartButton() {
        return startButton;
    }

    public MenuButton getMazeChooser() {
        return mazeChooser;
    }

    public MenuButton getAlgorithmChooser() {
        return algorithmChooser;
    }

    public VBox getParameterBox() {
        return parameterBox;
    }

    public StatBoxUI getStatBoxUI() {
        return statBoxUI;
    }

    public TextField getPopulationSize() {
        return populationSize;
    }

    public TextField getMutationRate() {
        return mutationRate;
    }

    public TextField getCrossOverRate() {
        return crossOverRate;
    }

    public TextField getMaxGeneration() {
        return maxGeneration;
    }

    public TextField getMaxMoves() {
        return maxMoves;
    }

}