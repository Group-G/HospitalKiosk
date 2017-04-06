package groupg.jfx;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Created by will on 4/6/17.
 */
public class ImageViewFactory {
    private static final int MIN_PIXELS = 100;

    public static ImageView getImageView(Image image, Pane pane){
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
//        reset(imageView, image.getWidth() / 2, image.getHeight() / 2);

//        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

//        imageView.setOnMousePressed(e -> {
//
//            Point2D mousePress = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//            mouseDown.set(mousePress);
//        });

//        imageView.setOnMouseDragged(e -> {
//            Point2D dragPoint = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//            shift(imageView, dragPoint.subtract(mouseDown.get()));
//            mouseDown.set(imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
//        });

//        imageView.setOnScroll(e ->
//        {
//            double delta = e.getDeltaY();
//            Rectangle2D viewport = imageView.getViewport();
//
//            double scale = clamp(Math.pow(1.005, delta),  // altered the value from 1.01to zoom slower
//                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
//                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
//                    // don't scale so that we're bigger than image dimensions:
//                    Math.max(image.getWidth() / viewport.getWidth(), image.getHeight() / viewport.getHeight())
//            );
//            if (scale != 1.0) {
//                Point2D mouse = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//
//                double newWidth = viewport.getWidth();
//                double newHeight = viewport.getHeight();
//                double imageViewRatio = (imageView.getFitWidth() / imageView.getFitHeight());
//                double viewportRatio = (newWidth / newHeight);
//                if (viewportRatio < imageViewRatio) {
//                    // adjust width to be proportional with height
//                    newHeight = newHeight * scale;
//                    newWidth = newHeight * imageViewRatio;
//                    if (newWidth > image.getWidth()) {
//                        newWidth = image.getWidth();
//                    }
//                } else {
//                    // adjust height to be proportional with width
//                    newWidth = newWidth * scale;
//                    newHeight = newWidth / imageViewRatio;
//                    if (newHeight > image.getHeight()) {
//                        newHeight = image.getHeight();
//                    }
//                }
//
//                // To keep the visual point under the mouse from moving, we need
//                // (x - newViewportMinX) / (x - currentViewportMinX) = scale
//                // where x is the mouse X coordinate in the image
//                // solving this for newViewportMinX gives
//                // newViewportMinX = x - (x - currentViewportMinX) * scale
//                // we then clamp this value so the image never scrolls out
//                // of the imageview:
//                double newMinX = 0;
//                if (newWidth < image.getWidth()) {
//                    newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
//                            0, image.getWidth() - newWidth);
//                }
//                double newMinY = 0;
//                if (newHeight < image.getHeight()) {
//                    newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
//                            0, image.getHeight() - newHeight);
//                }
//
//                imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
//            }
//        });

//        imageView.setOnMouseClicked(e -> {
//            if (e.getClickCount() == 2) {
//                reset(imageView, image.getWidth(), image.getHeight());
//            }
//        });

//        imageView.fitWidthProperty().bind(pane.widthProperty());
//        imageView.fitHeightProperty().bind(pane.heightProperty());

        return imageView;
    }

    // reset to the top left:
    private static void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private static void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private static double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }
}
