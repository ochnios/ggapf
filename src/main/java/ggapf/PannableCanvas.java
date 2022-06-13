package ggapf;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

/**Pannable and zoomable canvas for displaying a graph
 * Adapted from <a href="https://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer">Stackoverflow</a>
 * @see PannableCanvas
 * @see DragContext
 * @see NodeGestures
 * @see SceneGestures
 */

public class PannableCanvas extends Pane {
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    public PannableCanvas(double prefWidth, double prefHeight) {
        setPrefSize(prefWidth, prefHeight);

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }

    public double getScale() {
        return myScale.get();
    }

    public void setScale( double scale) {
        myScale.set(scale);
    }

    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}
