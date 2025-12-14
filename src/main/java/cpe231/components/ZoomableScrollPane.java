package cpe231.components;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ZoomableScrollPane extends ScrollPane {

    private double scaleValue = 1.0;
    private final double zoomIntensity = 0.02;
    private final double MAX_SCALE = 5.0;

    private double minScaleRequired = 1.0;
    private final double MIN_SCALE_BUFFER = 0.95;

    private Node target;
    private final Group zoomNode;

    public ZoomableScrollPane(Node target) {
        super();

        this.zoomNode = new Group();
        setContent(outerNode(zoomNode));

        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true);
        setFitToWidth(true);

        this.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            if (this.target != null) {
                calculateMinScale();
                if (scaleValue < minScaleRequired) {
                    scaleValue = minScaleRequired;
                    updateScale();
                }
            }
        });

        setNode(target);
    }

    public final void setNode(Node target) {
        zoomNode.getChildren().clear();

        this.target = target;

        if (target != null) {
            zoomNode.getChildren().add(target);

            javafx.application.Platform.runLater(() -> {
                calculateMinScale();
                scaleValue = minScaleRequired;
                updateScale();

                this.setHvalue(0.5);
                this.setVvalue(0.5);
            });
        }

    }

    private void calculateMinScale() {
        if (target == null) {
            this.minScaleRequired = 1.0;
            return;
        }

        Bounds targetBounds = target.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double mazeWidth = targetBounds.getWidth();
        double mazeHeight = targetBounds.getHeight();
        double paneWidth = viewportBounds.getWidth();
        double paneHeight = viewportBounds.getHeight();

        if (paneWidth <= 0 || paneHeight <= 0 || mazeWidth <= 0 || mazeHeight <= 0) {
            this.minScaleRequired = 1.0;
            return;
        }

        double scaleX = paneWidth / mazeWidth;
        double scaleY = paneHeight / mazeHeight;

        this.minScaleRequired = Math.min(scaleX, scaleY) * MIN_SCALE_BUFFER;
    }

    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(e -> {
            e.consume();
            onScroll(e.getDeltaY(), new Point2D(e.getX(), e.getY()));
        });
        return outerNode;
    }

    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private void updateScale() {
        if (target != null) {
            target.setScaleX(scaleValue);
            target.setScaleY(scaleValue);
        }
    }

    private void onScroll(double wheelDelta, Point2D mousePoint) {
        if (target == null) {
            return;
        }
        double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        double newScaleValue = scaleValue * zoomFactor;

        newScaleValue = Math.max(minScaleRequired, Math.min(newScaleValue, MAX_SCALE));

        if (newScaleValue == scaleValue)
            return;

        double oldScale = scaleValue;
        scaleValue = newScaleValue;
        updateScale();
        this.layout();

        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        double f = scaleValue / oldScale;

        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(f - 1));

        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        double hMax = updatedInnerBounds.getWidth() - viewportBounds.getWidth();
        double vMax = updatedInnerBounds.getHeight() - viewportBounds.getHeight();

        if (hMax <= 0 && vMax <= 0) {
            this.setHvalue(0.5);
            this.setVvalue(0.5);
        } else {
            this.setHvalue(hMax <= 0 ? 0.5 : (valX + adjustment.getX()) / hMax);
            this.setVvalue(vMax <= 0 ? 0.5 : (valY + adjustment.getY()) / vMax);
        }
    }
}