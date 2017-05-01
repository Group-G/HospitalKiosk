package groupg.jfx;

import groupg.database.Location;
import groupg.database.Person;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.*;
import java.util.stream.Collectors;

import static groupg.Main.h;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class AutoCompleteTextField extends TextField {
    private final SortedSet<Location> entries;
    private ContextMenu entriesPopup;
    private Location currentSelection = null;

    public AutoCompleteTextField() {
        super();
        entries = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener((observableValue, s, s2) ->
                                   {
                                       String text = getText();
                                       if (text.length() == 0)
                                           entriesPopup.hide();
                                       else {
                                           if (entries.size() > 0) {
                                               LinkedList<Location> searchResult = new LinkedList<>();
                                               LinkedList<Location> searchResult2 = new LinkedList<>();

                                               searchResult2.addAll(h.getAllLocations()
                                                                     .stream()
                                                                     .filter(elem -> FuzzySearch.ratio(elem.getName().toLowerCase(), getText().toLowerCase()) > 25)
                                                                     .sorted((loc1, loc2) -> FuzzySearch.ratio(loc1.getName().toLowerCase(), loc2.getName().toLowerCase()))
                                                                     .collect(Collectors.toList()));

                                               searchResult2.addAll(h.getAllPeople()
                                                                     .stream()
                                                                     .map(Person::getLocations)
                                                                     .map(loc -> {
                                                                         if (loc.size() != 0)
                                                                             return h.getLocationById(loc.get(0));
                                                                         return null;
                                                                     })
                                                                     .filter(Objects::nonNull)
                                                                     .collect(Collectors.toList()));

                                               searchResult2.addAll(entries.stream()
                                                                           .filter(e -> e.toString().toLowerCase().contains(getText().toLowerCase()))
                                                                           .collect(Collectors.toList()));

                                               LinkedList<Location> filtered = new LinkedList<>();
                                               filtered.addAll(searchResult2.stream().filter(elem -> !elem.getName().equals("My Node")).collect(Collectors.toList()));

                                               filtered.forEach(elem -> {
                                                   if (!searchResult.contains(elem))
                                                       searchResult.add(elem);
                                               });


//                                               System.out.println("searchResult = " + searchResult.size());
                                               ArrayList<Integer> values = new ArrayList<>();
                                               for(int i = 0; i < searchResult.size(); i++){
                                                    values.add(FuzzySearch.ratio(searchResult.get(i).getName().toLowerCase(), getText().toLowerCase()));
//                                                   System.out.println(values.get(i));
                                               }
//                                               System.out.println("values.size() = " + values.size());

                                               for(int i = 0; i < values.size(); i++){
                                                   for(int j = values.size()-1; j > i; j--){
                                                       if(values.get(j) < values.get(j-1)){

                                                           int tempV = values.get(j);
                                                           values.set(j, values.get(j));
                                                           values.set(j-1, tempV);

                                                       }
                                                   }
                                               }


                                               populatePopup(searchResult);

                                               if (!entriesPopup.isShowing())
                                                   entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                                           } else
                                               entriesPopup.hide();
                                       }
                                   });

        focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> entriesPopup.hide());
    }

    /**
     * Populate the entry set with the given search results. Limited to 10 entries
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<Location> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();

        int count = Math.min(searchResult.size(), 10);
        for (int i = 0; i < count; i++) {
            final Location current = searchResult.get(i);
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

    public SortedSet<Location> getEntries() {
        return entries;
    }

    public Location getCurrentSelection() {
        if (currentSelection == null) {
            Location out = null;

            for (Location elem : h.getAllLocations())
                if (elem.toString().equals(getText())) {
                    out = elem;
                    break;
                }

            return out;
        }
        return currentSelection;
    }

    public void setCurrentSelection(Location currentSelection) {
        this.currentSelection = currentSelection;
        setText(currentSelection.getName());
        entriesPopup.hide();

    }
}
