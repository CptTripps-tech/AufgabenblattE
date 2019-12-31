package E2;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public  class BinaryPersistency    {
    private ArrayList<Wikibook> zettelkasten;
    private DOMSource domSource;
    /**
     * @param zk        Name des Zettelkastens
     */
  //  @Override
    public void save(Zettelkasten zk) {

        try{
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db= dbf.newDocumentBuilder();
            Document doc= db.newDocument();

           Element root=doc.createElement("Wikibooks");
           doc.appendChild(root);

            int i=1;
            for(Wikibook wikibook:zk){
                Element wikib=doc.createElement("wikibook");
                wikib.setAttribute("id",""+i);
                root.appendChild(wikib);

                Element titel=doc.createElement("Titel");
                titel.appendChild(doc.createTextNode(String.valueOf(wikibook.getTitle())));
                wikib.appendChild(titel);

                Element regal=doc.createElement("Regal");
                regal.appendChild(doc.createTextNode(String.valueOf(wikibook.getRegal())));
                wikib.appendChild(regal);

                Element urheber=doc.createElement("Urheber");
                urheber.appendChild(doc.createTextNode(String.valueOf(wikibook.getContributor())));
                wikib.appendChild(urheber);

                Element timestamp=doc.createElement("Timestamp");
                timestamp.appendChild(doc.createTextNode(String.valueOf(wikibook.getTimestamp())));
                wikib.appendChild(timestamp);

                root.appendChild(wikib);
                i++;
            }

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");

            DOMSource source=new DOMSource(doc);
            try{
                Stage stage=new Stage();
                FileChooser saveChooser=new FileChooser();
                FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                saveChooser.getExtensionFilters().add(extensionFilter);

                File file=saveChooser.showSaveDialog(stage);
                FileWriter fos=new FileWriter(file);
                StreamResult result=new StreamResult(fos);
                transformer.transform(source, result);

            }catch (Exception ex){
                ex.printStackTrace();
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

/*
    public Zettelkasten load() {
        Stage stage=new Stage();
        FileChooser loadChooser=new FileChooser();
        FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("XML files( *.xml)","*.xml");
        loadChooser.getExtensionFilters().add(extensionFilter);

        File loadFile=loadChooser.showOpenDialog(stage);

        Zettelkasten zettelkasten=null;
        Wikibook wikibook = null;
        try {
           InputStream inputStream=new FileInputStream(loadFile);
           Reader reader=new InputStreamReader(inputStream,"UTF-8");

           InputSource is=new InputSource(reader);
           is.setEncoding("UTF-8");

           SAXParserFactory factory=SAXParserFactory.newInstance();
           SAXParser saxParser=factory.newSAXParser();
           DefaultHandler handler=new DefaultHandler();

           saxParser.parse(is,handler);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return zettelkasten;
    }
*/
}