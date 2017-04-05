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
class AutoCompleteTextField<T> extends TextField
{
    private final SortedSet<T> entries;
    private ContextMenu entriesPopup;
    private T currentSelection = null;

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
                                               LinkedList<T> searchResult = new LinkedList<>();
                                               searchResult.addAll(entries.stream()
                                                                          .filter(e -> e.toString().toLowerCase().contains(getText().toLowerCase()))
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
    private void populatePopup(List<T> searchResult)
    {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        int count = Math.min(searchResult.size(), 10);
        for (int i = 0; i < count; i++)
        {
            final T current = searchResult.get(i);
            String result = current.toString();
            Label entryLabel = new Label(result);

            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent ->
                             {
                                 this.setText(result);
                                 entriesPopup.hide();
                                 currentSelection = current;
                             });

            menuItems.add(item);
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    SortedSet<T> getEntries()
    {
        return entries;
    }

    T getCurrentSelection()
    {
        return currentSelection;
    }

    String getCurrentSelectionString()
    {
        if (currentSelection == null)
        {
            String out = "";

            for (Location elem : HospitalData.getAllLocations())
            {
                if (elem.toString().equals(getText()))
                {
                    out = elem.toString();
                    break;
                }
            }

            return out;
        }
        else
        {
            return currentSelection.toString();
        }
    }

    void setCurrentSelection(T currentSelection)
    {
        this.currentSelection = currentSelection;
    }
}
