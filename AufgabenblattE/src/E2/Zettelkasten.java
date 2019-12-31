package E2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.util.*;

public class Zettelkasten implements Iterable<Wikibook>, Comparable<Wikibook>, Serializable {
    private ArrayList<Wikibook> myZettelkasten;
    private String SORTED = "0";

    /**
     * Erstellt einen Zettelkasten in Form einer Arraylist für die Medien
     */
    public Zettelkasten() {
        myZettelkasten = new ArrayList<Wikibook>();
    }

    /**
     * @param wikibook Hinzufügen des Mediums zum Zettelkasten
     * @return false    Medium nicht erfolgreich hinzugefügt
     */
    public boolean addWikibook(Wikibook wikibook) {
        return myZettelkasten.add(wikibook);
    }

        /**Sortieren des Zettelkastens anhand zwei Parameter
         * @param  p        Der erste Sortier-Parameter
         * @return true     Sortieren hat funktioniert
         * @return false    Sortieren hat nicht funktioniert*/
    public boolean sort(String p) {
        if (p=="AZ") {
            if(SORTED.equals("AZ")){
                return false;
            }
                Collections.sort(myZettelkasten);
            SORTED="AZ";
            }
            if (p=="ZA") {
                if(SORTED.equals("ZA")){
                    return false;
                }
                Collections.sort(myZettelkasten, Collections.reverseOrder());
                SORTED="ZA";
            }
            return true;
        }

        public String getSORTED(){
        return SORTED;
        }

    @Override
    public Iterator<Wikibook> iterator() {
        return myZettelkasten.iterator();
    }



    @Override
    public int compareTo(Wikibook o) {
        return 0;
    }
}

