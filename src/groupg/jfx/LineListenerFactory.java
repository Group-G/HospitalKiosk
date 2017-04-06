package groupg.jfx;

import groupg.controller.AdminMainController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.Arrays;

/**
 * @author Ryan Benasutti
 * @since 2017-04-05
 */
public class LineListenerFactory
{
    private static double mouseX, mouseY;

    public static void attachListeners(UniqueLine... lines)
    {
        Arrays.stream(lines).forEach(line ->
                                     {
                                         line.setOnContextMenuRequested(event ->
                                                                        {
                                                                            ContextMenu contextMenu = new ContextMenu();
                                                                            MenuItem remove = new MenuItem("Remove Connection");
                                                                            remove.setOnAction(event1 -> {
                                                                                AdminMainController.displayedLines.remove(line);
                                                                                AdminMainController.lineOverlay.getChildren().setAll(AdminMainController.displayedLines);
                                                                                if (NodeListenerFactory.currentSelection != null)
                                                                                {
                                                                                    NodeListenerFactory.currentSelection.getLocation().getNeighbors().remove(line.getTo());
                                                                                }
                                                                            });
                                                                            contextMenu.getItems().add(remove);
                                                                            contextMenu.show(line, mouseX, mouseY);
                                                                        });

                                         line.setOnMouseMoved(event ->
                                                              {
                                                                  mouseX = event.getScreenX();
                                                                  mouseY = event.getScreenY();
                                                              });
                                     });
    }
}
