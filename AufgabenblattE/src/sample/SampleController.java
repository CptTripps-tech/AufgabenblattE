package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class SampleController {

    public Label halloWelt;

    public void sagHalloWelt(ActionEvent actionEvent) {
        halloWelt.setText("Hallo Welt!");
    }
}
