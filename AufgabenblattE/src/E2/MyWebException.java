package E2;

import javafx.scene.control.Alert;

public class MyWebException extends Exception {

    public MyWebException(String fehlermeldung){
        super(fehlermeldung);
        Alert dialog=new Alert(Alert.AlertType.INFORMATION);
        dialog.setContentText(fehlermeldung);
        dialog.show();
    }

}
