package E2;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Wikibook implements Comparable<Wikibook>{

        private String export_url = "https://de.wikibooks.org/wiki/Spezial:Exportieren/";
        private String search_key;
        private String wikiurl="https://de.wikibooks.org/wiki/";
        private String title;
        private String regal;
        private String timestamp;
        private String searchurl;
        private URL url;
        private String contributor;
        private int indexfirst, indexlast;
        private ArrayList<String> kapitel;

        /**Konstruktor für den Wikibook-Eintrag*/
    public Wikibook() {}


    /**Informationen über ein bestimmtes Wikibook sammeln und anschließend zurückliefern*/
        public  void WikipediaBooksContributorRequest(String search_book) throws MyWebException {
            this.search_key=search_book;
            search_book=search_book.replaceAll("\\s","_");
            searchurl = export_url + search_book;

            try {
                url = new URL(searchurl);
                URLConnection con = url.openConnection();
                con.setDoInput(true);
                InputStream inStream = con.getInputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(inStream));

                String tmp = null;

                while (input.readLine() != null) {
                    tmp = input.readLine();


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

                        setTimestamp(""+requiredDate+" um "+""+requiredTime+" Uhr");
                        continue;
                    }

                    //Herausgeber suchen und setzen, falls IP-Adresse angegeben ist, diese stattdessen setzen
                    if (tmp.contains("<username>")) {
                        indexfirst = tmp.indexOf("<username>");
                        indexlast = tmp.indexOf("</username>");
                        tmp = tmp.substring(indexfirst, indexlast);

                        contributor = tmp.replace("<username>", "");
                        setContributor(contributor);
                        continue;
                    }
                    if (tmp.contains("<ip>")) {
                        indexfirst = tmp.indexOf("<ip>");
                        indexlast = tmp.indexOf("</ip>");
                        tmp = tmp.substring(indexfirst, indexlast);

                        contributor = tmp.replace("<ip>", "");
                        setContributor(contributor);
                        //System.out.println("Urheber:"+contributor);
                        continue;
                    }
                }
                inStream.close();
            } catch (Exception e) {
                throw new MyWebException("Keine Daten!");
            }

            setKapitel_Regal(search_book);

        }


    /**Die Kapitel heraussuchen und setzen*/
    public void setKapitel_Regal(String search_book){
        String tmp=null;

        kapitel=new ArrayList<String>();
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

                    if(tmp.contains(title+":")){

                        tmp=tmp.replace(title+":","");
                        indexlast=tmp.indexOf("</p");
                        tmp=tmp.substring(0,indexlast);
                        tmp=tmp.replace(">","");
                        tmp=tmp.replaceAll("^\"|\"$", "");
                        kapitel.add(tmp);
                    }
                    if(tmp.contains("Regal:")){
                        tmp=tmp.replace("Regal:","");
                        indexlast=tmp.indexOf("</p");
                        tmp=tmp.substring(0,indexlast);
                        tmp=tmp.replace(">","");
                        tmp=tmp.replaceAll("^\"|\"$", "");
                        setRegal(tmp);
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

    /**Die Getter- und Setter-Methoden*/
    public void setTitel(String titel) {
        this.title=titel;
    }

    StringBuilder sb=new StringBuilder();


    public String getTitle(){
            return title;
    }

    public void setRegal(String regal){
            this.regal=regal;
    }

    public String getRegal(){
            return regal;
    }

    public ArrayList<String> getKapitel(){
        return kapitel;
    }

    public void setURL(String url) {
        this.wikiurl=wikiurl+search_key;
    }

    public String getURL(){
            return wikiurl;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp=timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }


    public void setContributor(String contributor) {
        this.contributor=contributor;
    }

    public String getContributor(){
            return contributor;
    }


    public int compareTo(Wikibook o) {
        return this.getTitle().compareTo(o.getTitle());
    }
}
