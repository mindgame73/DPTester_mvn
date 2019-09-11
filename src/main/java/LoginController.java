import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginController {

    static String username;

    @FXML
    private Button exitButton;

    @FXML
    private Button enterLoginButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextArea textArea;

    @FXML
    private CheckBox newsCheckBox;

    @FXML
    private ImageView imageView;

    private final File usernamesFile = new File("usernames.txt");
    private final File newsFile = new File("news.txt");
    private ArrayList<String> usernames;

    @FXML
    void initialize() {

        imageView.setImage(new Image("niiar.png"));

        newsCheckBox.setSelected(true);
        textArea.setVisible(true);
        try {
            BufferedReader br = new BufferedReader(new FileReader(newsFile));
            String line;
            while ((line = br.readLine())!=null){
                textArea.appendText(line + "\n");
            }
        }
        catch (IOException ex){

        }

        newsCheckBox.setOnAction(e->{
            if(newsCheckBox.isSelected()){
                textArea.setVisible(true);
            }
            else
            {
                textArea.setVisible(false);
            }
        });

        usernames = new ArrayList<>();

        if (usernamesFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(usernamesFile));
                String line;
                while ((line = br.readLine())!=null){
                    usernames.add(line);
                }
            }
            catch (IOException ex){

            }
        }

        TextFields.bindAutoCompletion(loginField, usernames);

        enterLoginButton.setOnAction(e->{
            if (!loginField.getText().equals("")){
                Stage currentStage = (Stage) exitButton.getScene().getWindow();
                currentStage.close();

                if (!usernames.contains(loginField.getText().trim())){
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(usernamesFile, true))) {
                        bw.newLine();
                        bw.write(loginField.getText().trim());
                        bw.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                username = loginField.getText();

                Parent root;
                try {
                    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                    root = FXMLLoader.load(getClass().getResource("sample.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root,dimension.width-100,dimension.height-100));
                    stage.setTitle("Тестирование персонала Диспетчерского Пункта АО ГНЦ НИИАР.");
                    stage.setMaximized(true);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.show();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitButton.setOnAction(e->{
            Platform.exit();
        });
    }
}