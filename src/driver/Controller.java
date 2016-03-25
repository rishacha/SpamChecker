package driver;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import naivebayes.SpamCheck;

import java.io.File;
public class Controller {
    @FXML
    private GridPane gridPane; // this is my root element in FXML (fx:id="borderPane")
    @FXML
    private TextField fileName;
    @FXML
    private Text loadStatus;
    @FXML
    private Text result;
    @FXML
    private TextArea manualData;
    @FXML
    private CheckBox spamCheckBox;
    @FXML
    private TextArea queryText;
    @FXML
    private TextField outPath;
    @FXML
    private Text writeResult;

    private SpamCheck spamCheck = new SpamCheck();
//C:\Users\Jasmin2332\IdeaProjects\SideProject\trainingdata.txt
    @FXML
    public void launchFileSelector() {
        File file = Main.fileChooser.showOpenDialog(gridPane.getScene().getWindow());
        if (file != null) {
            fileName.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void startQuery() {
        if(spamCheck.isReady()) {
            if(queryText.getText()!=null)
            result.setText(spamCheck.query(queryText.getText()));
        }
    }

    @FXML
    public void loadFile() {
        if(fileName!=null) {
            loadStatus.setText("Loading.");
            spamCheck.trainWithFile(new File(fileName.getText()));
            if(spamCheck.isReady())
                loadStatus.setText("Loaded.");
            else loadStatus.setText("Error. Check if invalid file or empty.");
        }
    }

    public void trainManually() {
        if(manualData.getText()!=null && manualData.getText().length()>0)
        spamCheck.trainManually(manualData.getText(), spamCheckBox.isSelected());
    }

    public void saveData() {
        if(outPath.getText()!=null) {
            if(spamCheck.saveData(outPath.getText())) {
                writeResult.setText("Success.");
            }
            else writeResult.setText("Failed.");
        }
        else {
            writeResult.setText("Invalid Path.");
        }
    }
}
