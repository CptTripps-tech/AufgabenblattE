package E2;


import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**Der Controller für die Benutzeroberfläche*/
public class WikiBooksController  {
    @FXML
    private TextField tfSearchTermf;
    @FXML
    private Button btnSearch;
    @FXML
    private Button addMediaBTN;
    @FXML
    private Button deleteMediaBTN;
    @FXML
    private Button zettelSortBTN;
    @FXML
    private Button zettelSaveBTN;
    @FXML
    private Button zettelLoadBTN;
    @FXML
    private WebView browser;
    @FXML
    private Label contributorLabel;
    @FXML
    private Label timeStampLabel;
    @FXML
    private ListView<String> zettelkastenList;
    @FXML
    private TextField synonymSearchTf;
    @FXML
    private ListView<String> synonymList;
    @FXML
    private Button synonymSearchBTN;
    @FXML
    private Button searchSynonymListBTN;
    @FXML
    private ComboBox synonymBox;

    private String searchterm;
    private WebEngine webEngine;
    private Wikibooks wikibooks=new Wikibooks();
    private Zettelkasten zettelkasten=wikibooks.getZettelkasten();
    private Wikibook wikibook;
    private String sortparam;

    public void handleSearchButton(ActionEvent actionEvent) throws MyWebException {
        try {
            searchterm = tfSearchTermf.getText();
            navigateBrowser(searchterm);
        }catch (MyWebException ex){
            ex.getLocalizedMessage();
        }
    }

    public void tfonAction(ActionEvent actionEvent) throws MyWebException {
       try {
           searchterm = tfSearchTermf.getText();
           navigateBrowser(searchterm);
       }catch (MyWebException ex){
           ex.getLocalizedMessage();
       }
    }

    public void navigateBrowser(String searchterm) throws MyWebException {
            webEngine = browser.getEngine();
            try {
                wikibooks.WikiBooksContributorRequest(searchterm);
                webEngine.load(Wikibooks.getUrl(searchterm));
                showContributor();
                showTimestamp();
            }catch (MyWebException ex){
                webEngine.load("https://de.wikibooks.org/w/index.php?search="+searchterm+"&title=Spezial%3ASuche&go=Seite&ns0=1");
            }
    }

    public void handleBrowserZoom(KeyEvent keyEvent) {
        KeyCombination zoomin=new KeyCodeCombination(KeyCode.PLUS,KeyCombination.CONTROL_DOWN);
        KeyCombination zoomout=new KeyCodeCombination(KeyCode.MINUS,KeyCombination.CONTROL_DOWN);
        if(zoomin.match(keyEvent)){
            browser.setZoom(browser.getZoom()+0.2);
        }
        if(zoomout.match(keyEvent)){
            browser.setZoom(browser.getZoom()-0.2);
        }
    }

    public Wikibook handleWikibook(){
        wikibook=new Wikibook();
        wikibook.setTitel(wikibooks.getTitle());
        wikibook.setRegal(wikibooks.getRegal());
        wikibook.setContributor(wikibooks.getContributor());
        wikibook.setTimestamp(wikibooks.getTimestamp());
        return wikibook;
    }
    public void initialize() {
        try {
            deleteMediaBTN.setDisable(true);
            zettelLoadBTN.setDisable(true);
            webEngine = browser.getEngine();
            browser.setZoom(0.7);
            webEngine.load("https://de.wikibooks.org/wiki/Hauptseite");
        } catch (Exception e) {
            Alert dialog=new Alert(Alert.AlertType.ERROR);
            dialog.setContentText(e.getMessage());
            dialog.show();
        }
    }

    public void showContributor(){
          contributorLabel.setText("Urheber: "+wikibooks.getContributor());
    }

    public void showTimestamp(){
        timeStampLabel.setText("Letzte Änderung: "+wikibooks.getTimestamp());
    }

    public void handleMediaAddBTN(ActionEvent actionEvent) {
            //zettelkasten=wikibooks.getZettelkasten();
            wikibook=handleWikibook();
            zettelkasten.addWikibook(wikibook);
            zettelkastenList.getItems().add(wikibook.getTitle());
    }

    public void handleMediaDeleteBTN(ActionEvent actionEvent) {
    }

    public void zettelSort(ActionEvent actionEvent) {
        if(sortparam==null){
            sortparam="AZ";
        }
        switch (sortparam){
            case "AZ":
                zettelkasten.sort(sortparam);
                sortparam="ZA";
                zettelkastenList.getItems().clear();
                for(Wikibook wikibook:zettelkasten){
                    zettelkastenList.getItems().add(wikibook.getTitle());
                }
                break;
            case "ZA":
                zettelkasten.sort(sortparam);
                sortparam="AZ";
                zettelkastenList.getItems().clear();
                for(Wikibook wikibook:zettelkasten){
                    zettelkastenList.getItems().add(wikibook.getTitle());
                }
                break;
        }
    }


    public void zettelSave(ActionEvent actionEvent) {
        BinaryPersistency binaryPersistency=new BinaryPersistency();
        binaryPersistency.save(zettelkasten);
    }


    public void zettelLoad(ActionEvent actionEvent) {
        /*
        BinaryPersistency binaryPersistency=new BinaryPersistency();
        binaryPersistency.load();

         */
    }

        ArrayList<String> searched=new ArrayList<String>();
    public void handleSyonymSearchBTN(ActionEvent actionEvent) {
        try {
            SynonymSearch(synonymSearchTf.getText());
            fillSynonymBox(synonymSearchTf.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSynonymSearchField(ActionEvent actionEvent) {
        try{
            SynonymSearch(synonymSearchTf.getText());
            fillSynonymBox(synonymSearchTf.getText());
            } catch (Exception e) {
                Alert dialog=new Alert(Alert.AlertType.WARNING,"Fehler beim Zugriff auf Wortschatz Server",ButtonType.OK);
                dialog.show();
            }
    }

    public void fillSynonymBox(String searchitem){
        searched.add(searchitem);
        Collections.reverse(searched);
        for(int i=0;i<searched.size();i++){
            synonymBox.getItems().add(searched.get(i));
            if(searched.contains(searched.get(i))){
                searched.remove(i);
            }
        }
    }

    public void SynonymSearch(String suchbegriff) throws IOException, ParseException {
        String Suchbegriff=suchbegriff;
        URL myURL;
        String url="http://api.corpora.uni-leipzig.de/ws/similarity/deu_news_2012_1M/coocsim/"+Suchbegriff+"?minSim=0.1&limit=50";
        myURL=new URL(url);
        String synonym;

        JSONParser jsonParser=new JSONParser();
        String jsonResponse=streamToString(myURL.openStream());
        JSONArray jsonResponseArr=(JSONArray) jsonParser.parse(jsonResponse);


        if(synonymList.getItems()!=null){
            synonymList.getItems().clear();
        }

        if(jsonResponseArr.size()==0){
            synonymList.getItems().add("<keine>");
            searchSynonymListBTN.setDisable(true);
            synonymList.setDisable(true);


        } else {
            searchSynonymListBTN.setDisable(false);
            synonymList.setDisable(false);

            ArrayList<String> synonyme = new ArrayList<String>();

            for (Object el : jsonResponseArr) {
                JSONObject wordcontainer = (JSONObject) ((JSONObject) el).get("word");

                synonym = (String) wordcontainer.get("word");

                synonyme.add(synonym);
            }
            Collections.sort(synonyme, String.CASE_INSENSITIVE_ORDER);

            for (String begriffe : synonyme) {
                synonymList.getItems().add(begriffe);
            }
        }
    }

    public String streamToString(InputStream is) throws IOException{
        String result="";
        try(Scanner s=new Scanner(is)){
            s.useDelimiter("\\A");
            result=s.hasNext() ? s.next():"";
            is.close();
        }
        return result;
    }

    public void handlesearchSynonymListBTN(ActionEvent actionEvent) {
        try {
            SynonymSearch(synonymList.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSynoynmList(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2){
            try {
                SynonymSearch(synonymList.getSelectionModel().getSelectedItem());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
