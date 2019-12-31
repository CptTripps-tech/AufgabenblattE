package E2;

public class DuplicateEntryException extends IllegalArgumentException {

    public DuplicateEntryException(){
        super("Deine Exception");
    }

    public DuplicateEntryException(String fehlermeldung){
        super(fehlermeldung);
    }
}

