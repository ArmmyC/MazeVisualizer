package cpe231.responsives;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.Region;

/*
 * ResponsiveLabel.java 
 * สำหรับการจัดการขนาด Font และ Padding แบบ Responsive
 */

public class Responsive {

        private static double getResponsivePaddingSize(double minSize, double scaleFactor) {
                if (scaleFactor <= 0) {
                        return 0.0;
                }
                return minSize / scaleFactor;
        }

        public static void bindResponsiveScaling(
                        Region object,
                        Region container,
                        double fontScaleFactor,
                        double paddingTopScaleFactor,
                        double paddingRightScaleFactor,
                        double paddingBottomScaleFactor,
                        double paddingLeftScaleFactor) {

                object.styleProperty().bind(
                                Bindings.createStringBinding(() -> {

                                        double containerWidth = container.getWidth();
                                        double containerHeight = container.getHeight();
                                        double minSize = Math.min(containerWidth, containerHeight);
                                        double fontSize = minSize / fontScaleFactor;

                                        String style = "";
                                        // Font Size
                                        if (fontSize != -1) {
                                                style += String.format("-fx-font-size: %.2fpx;", fontSize);
                                        }

                                        // Padding
                                        if (paddingTopScaleFactor != -1 && paddingRightScaleFactor != -1 &&
                                                        paddingBottomScaleFactor != -1
                                                        && paddingLeftScaleFactor != -1) {

                                                double paddingTopSize = getResponsivePaddingSize(minSize,
                                                                paddingTopScaleFactor);
                                                double paddingRightSize = getResponsivePaddingSize(minSize,
                                                                paddingRightScaleFactor);
                                                double paddingBottomSize = getResponsivePaddingSize(minSize,
                                                                paddingBottomScaleFactor);
                                                double paddingLeftSize = getResponsivePaddingSize(minSize,
                                                                paddingLeftScaleFactor);

                                                style += String.format(
                                                                " -fx-padding: %.2fpx %.2fpx %.2fpx %.2fpx;",
                                                                paddingTopSize, paddingRightSize, paddingBottomSize,
                                                                paddingLeftSize);
                                        } else {
                                                style += " -fx-padding: 0px;";
                                        }

                                        return style;
                                }, container.widthProperty(), container.heightProperty()));
        }

}
