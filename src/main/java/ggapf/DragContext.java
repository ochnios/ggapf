package ggapf;

/**
 * Mouse drag context used for scene and nodes.
 * Adapted from <a href="https://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer">Stackoverflow</a>
 * @see PannableCanvas
 * @see DragContext
 * @see NodeGestures
 * @see SceneGestures
 */

public class DragContext {

   double mouseAnchorX;
   double mouseAnchorY;

   double translateAnchorX;
   double translateAnchorY;

}
