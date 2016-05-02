package driver;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import spam.SpamCheck;

import java.io.File;

public class Controller {
    public Button fileSelectButton;
    @FXML
    private GridPane gridPane; // this is my root element in FXML (fx:id="borderPane")
    @FXML
    private TextField folderName;
    @FXML
    private TextField queryFile;
    @FXML
    private Text loadStatus;
    @FXML
    private Button queryNB;
    @FXML
    private Button querySVD;
    @FXML
    private Button loadButton;
    @FXML
    private Button folderSelectButton;
    @FXML
    private Label resultOut;

    private SpamCheck spamCheck;

    @FXML
    public void launchFileSelector(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(folderSelectButton)) {
            File folder = Main.directoryChooser.showDialog(gridPane.getScene().getWindow());
            if (folder != null) {
                folderName.setText(folder.getAbsolutePath());
            }
        }
        else if(actionEvent.getSource().equals(fileSelectButton)){
            File file = Main.fileChooser.showOpenDialog(gridPane.getScene().getWindow());
            if (file != null) {
                queryFile.setText(file.getAbsolutePath());
            }
        }
    }

    @FXML
    public void startQuery(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(queryNB)) {
            spamCheck.setMode(true);
            spamCheck.setQueryFile(new File(queryFile.getText()));
            Task<String> t = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    spamCheck.run();
                    return spamCheck.getResult();
                }
            };
            t.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    t1 -> resultOut.setText(t.getValue()));
            new Thread(t).start();
        } else {
            spamCheck.setMode(false);
            spamCheck.setQueryFile(new File(queryFile.getText()));
            Task<String> t = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    spamCheck.run();
                    return spamCheck.getResult();
                }
            };
            t.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    t1 -> resultOut.setText(t.getValue()));
            new Thread(t).start();
        }
    }

    @FXML
    public void load() {
        if(folderName!=null) {
            File folder = new File(folderName.getText());
            if(folder.isDirectory()) {
                loadStatus.setText("Loading.");
                spamCheck = new SpamCheck(folder);
                boolean status = spamCheck.load();
                if(status) {
                    folderSelectButton.setDisable(true);
                    loadButton.setDisable(true);
                    queryNB.setDisable(false);
                    querySVD.setDisable(false);
                    loadStatus.setText("Ready");
                } else {
                    loadStatus.setText("Error.");
                }
            }
            else loadStatus.setText("Not a folder");
        }
    }
}
