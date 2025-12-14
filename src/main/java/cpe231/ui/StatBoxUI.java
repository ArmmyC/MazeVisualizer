package cpe231.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBoxUI {
    NumberFormat formatter = NumberFormat.getInstance(Locale.US);
    private final Label timeLabel = new Label("Times : 0 ns");
    private final Label stepLabel = new Label("Steps : 0");
    private final Label distLabel = new Label("Distances : 0");
    private final VBox statBox = new VBox(
            timeLabel,
            stepLabel,
            distLabel);
    private final VBox statContainer = new VBox(statBox);

    public StatBoxUI() {
        formatter.setMaximumFractionDigits(0);
        setupStyle();
    }

    private void setupStyle() {

        statContainer.getStyleClass().add("stat-container");
        statBox.getStyleClass().add("stat-box");
        timeLabel.getStyleClass().add("stat-value-label");
        stepLabel.getStyleClass().add("stat-value-label");
        distLabel.getStyleClass().add("stat-value-label");
    }

    public void updateStats(long timeNs, int steps, int distances) {
        timeLabel.setText("Times : " + formatter.format(timeNs) + " ns");
        stepLabel.setText(String.valueOf("Steps : " + steps));
        distLabel.setText(String.valueOf("Distances : " + distances));
    }

    public VBox getView() {
        return statContainer;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getStepLabel() {
        return stepLabel;
    }

    public Label getDistLabel() {
        return distLabel;
    }

    public VBox getStatBox() {
        return statBox;
    }

}