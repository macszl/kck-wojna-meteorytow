package com.example.scenebuilderrepo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button exitButton;

    @FXML
    private Button optionsButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    private Scene scene;
    private Stage stage;

    Stage getStage()
    {
        return stage;
    }

    public void exitScene()
    {

    }
    @FXML
    void switchToOptions(ActionEvent event)  throws IOException {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("options.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLoadedGame(ActionEvent event)
    {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    }
    public void switchToNewGame(ActionEvent event)
    {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}