package groupg;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class AutoCompleteTextField extends TextField
{
    private final SortedSet<String> entries;
    private ContextMenu entriesPopup;

    AutoCompleteTextField()
    {
        super();
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener((observableValue, s, s2) ->
                                   {
                                       String text = getText();
                                       if (text.length() == 0)
                                       {
                                           entriesPopup.hide();
                                       }
                                       else
                                       {
                                           if (entries.size() > 0)
                                           {
                                               LinkedList<String> searchResult = new LinkedList<>();
                                               searchResult.addAll(entries.stream()
                                                                          .filter(e -> e.toLowerCase().contains(getText().toLowerCase()))
                                                                          .collect(Collectors.toList()));

                                               populatePopup(searchResult);

                                               if (!entriesPopup.isShowing())
                                               {
                                                   entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                                               }
                                           }
                                           else
                                           {
                                               entriesPopup.hide();
                                           }
                                       }
                                   });

        focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> entriesPopup.hide());
    }

    /**
     * Populate the entry set with the given search results. Limited to 10 entries
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<String> searchResult)
    {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        int count = Math.min(searchResult.size(), 10);
        for (int i = 0; i < count; i++)
        {
            String result = searchResult.get(i);
            Label entryLabel = new Label(result);

            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent ->
                             {
                                 setText(result);
                                 entriesPopup.hide();
                             });

            menuItems.add(item);
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    SortedSet<String> getEntries()
    {
        return entries;
    }
}
