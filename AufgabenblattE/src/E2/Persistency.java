package E2;

public interface Persistency {
    /**@param zk            Name des Zettelkastens*/
    public void save(Zettelkasten zk);

    /**@param dateiname     Name der Datei*/
     public Zettelkasten load(String dateiname);
}
