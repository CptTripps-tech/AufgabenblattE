package E2;
/** Das Programm bietet einen integrierten Browser zum Durchsuchen der Wikibooks-Bibliothek.
 * Desweiteren können gefundene Wikibook-Einträge einem Zettelkasten hinzugefügt werden und dieser dann sortiert werden.
 *
 * Eine weitere Schaltfläche ermöglicht das Suchen von Synonymen eines bestimmten Begriffes.
 *
 * @Author Andy Gahler
 * @since 27.12.2019
 * **/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Wikibooks extends Application {
    private static String url;
    private String timestamp;
    private String contributor;
    private String title;
    private Zettelkasten zettelkasten=new Zettelkasten();
    private String regal;


    @Override
    public void start(Stage wikistage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getClassLoader().getResource("webview2.fxml"));
        wikistage.setTitle("WikiLibrary");
        wikistage.setScene(new Scene(root));
        wikistage.show();
    }

    public static String getUrl(String searchterm){
            url="https://de.wikibooks.org/wiki/"+searchterm;
            return url;
    }

    /**Informationen über ein bestimmtes Wikibook sammeln und anschließend zurückliefern*/
    public  void WikiBooksContributorRequest(String search_book) throws MyWebException {
        int indexfirst,indexlast;
        search_book=search_book.replaceAll("\\s","_");
        String searchurl ="https://de.wikibooks.org/wiki/Spezial:Exportieren/" + search_book;

        try {
            URL url = new URL(searchurl);
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            InputStream inStream = con.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inStream));

            String tmp = null;

            while (input.readLine() != null) {
                tmp = input.readLine();

                //Titel suchen und setzen
                if(tmp.contains("<title>")){
                    //tmp=tmp.replaceAll("\\s","");

                    indexfirst=tmp.indexOf("<title>");
                    indexlast=tmp.indexOf("</title>");
                    tmp=tmp.substring(indexfirst,indexlast);

                    tmp=tmp.replace("<title>","");
                    this.title=tmp;
                    continue;

                }
                //Timestamp suchen und setzen
                if(tmp.contains("<timestamp>")){

                    SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    SimpleDateFormat outputFormat_Date=new SimpleDateFormat("dd.MM.yyyy");
                    SimpleDateFormat outputFormat_Time=new SimpleDateFormat("HH:mm");

                    Date date=null;
                    String requiredDate=null;
                    String requiredTime=null;

                    tmp=tmp.replaceAll("\\s","");

                    tmp=tmp.replace("<timestamp>","");
                    tmp=tmp.replace("</timestamp>","");

                    inputFormat.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                    date=inputFormat.parse(tmp);

                    requiredDate=outputFormat_Date.format(date);
                    requiredTime=outputFormat_Time.format(date);

                   this.timestamp=(""+requiredDate+" um "+""+requiredTime+" Uhr");
                    continue;
                }

                //Herausgeber suchen und setzen, falls IP-Adresse angegeben ist, diese stattdessen setzen
                if (tmp.contains("<username>")) {
                    indexfirst = tmp.indexOf("<username>");
                    indexlast = tmp.indexOf("</username>");
                    tmp = tmp.substring(indexfirst, indexlast);

                    this.contributor = tmp.replace("<username>", "");
                    continue;
                }
                if (tmp.contains("<ip>")) {
                    indexfirst = tmp.indexOf("<ip>");
                    indexlast = tmp.indexOf("</ip>");
                    tmp = tmp.substring(indexfirst, indexlast);

                    this.contributor = tmp.replace("<ip>", "");
                    continue;
                }
            }
            inStream.close();
        } catch (Exception ex) {
            throw new MyWebException("Wikibooks enthält gesuchten Titel nicht!");
        }
        setKapitel_Regal(search_book);

    }

    /**Die Kapitel heraussuchen und setzen*/
    public void setKapitel_Regal(String search_book){
        String tmp=null;
        int indexfirst,indexlast;
        try {
            URL api;
            String api_url;
            api_url="https://de.wikibooks.org/w/api.php?action=parse&format=xml&prop=links&page="+search_book+"&redirect";
            api = new URL(api_url);
            URLConnection con = api.openConnection();
            con.setDoInput(true);
            InputStream inStream = con.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
            while ((tmp=input.readLine())!=null){
                String[] pattern=tmp.split("preserve");

                int i=1;
                while(i<pattern.length){
                    tmp=pattern[i];

                    if(tmp.contains("Regal:")){
                        tmp=tmp.replace("Regal:","");
                        indexlast=tmp.indexOf("</p");
                        tmp=tmp.substring(0,indexlast);
                        tmp=tmp.replace(">","");
                        tmp=tmp.replaceAll("^\"|\"$", "");
                       this.regal=tmp;
                    }
                    i++;
                }
            }

            inStream.close();

        } catch (Exception e) {
            Alert dialog=new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            dialog.show();
        }
    }

    public String getContributor(){
        return contributor;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getTitle(){
        return title;
    }

    public String getRegal(){
        return regal;
    }

    public Zettelkasten getZettelkasten(){
        return zettelkasten;
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
