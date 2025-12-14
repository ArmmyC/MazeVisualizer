package cpe231.ui;

import cpe231.components.ZoomableScrollPane;
import javafx.scene.layout.HBox;

public class MazePanel extends HBox {
    public final ZoomableScrollPane mazePane;

    public MazePanel(ZoomableScrollPane mazePane) {
        this.mazePane = mazePane;
        getChildren().add(mazePane);
    }
}
