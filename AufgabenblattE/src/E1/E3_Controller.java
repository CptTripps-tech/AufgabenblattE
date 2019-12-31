package E1;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.awt.*;

public class E3_Controller {
       @FXML
       private javafx.scene.control.Button addBTN;
       @FXML
       private javafx.scene.control.Button resetElem;
       @FXML
       private javafx.scene.control.TextField add;
       @FXML
       private javafx.scene.control.ListView<String> showList;

        @FXML
        private void handleAddButton(ActionEvent actionEvent){
            showList.getItems().add(add.getText());
        }

        public void handleResetButton(ActionEvent actionEvent) {
            showList.getItems().clear();
        }
}
