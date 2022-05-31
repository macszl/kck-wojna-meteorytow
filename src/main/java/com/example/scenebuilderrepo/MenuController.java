package com.example.scenebuilderrepo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public
class MenuController implements Initializable
{

    Image buttonImage      = new Image(new File("BUTTON.png").toURI()
                                               .toString());
    Image clickbuttonImage = new Image(new File("BUTTONCLICK.png").toURI()
                                               .toString());
    Image logo             = new Image(new File("LOGO.png").toURI()
                                               .toString());
    Image bg               = new Image(new File("mainbg.png").toURI()
                                               .toString());
    @FXML
    private Button exitButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button newGameButton;
    @FXML
    private ImageView newGameButtonImage;
    @FXML
    private ImageView loadGameButtonImage;
    @FXML
    private ImageView optionsButtonImage;
    @FXML
    private ImageView exitButtonImage;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView menuBackgroundImage;
    private Scene optionsScene;
    private Stage stage;

    Stage getStage()
    {
        return stage;
    }

    public
    void exitScene()
    {
        stage = (Stage) newGameButton.getScene()
                .getWindow();
        stage.close();
    }

    @FXML
    void switchToOptions(ActionEvent event) throws IOException
    {
        optionsButtonImage.setImage(clickbuttonImage);
        stage = (Stage) ((Node) event.getSource()).getScene()
                .getWindow();
        if ( optionsScene == null )
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("options.fxml"));
            optionsScene = new Scene(fxmlLoader.load(), GameInfo.x, GameInfo.y);
        }
        stage.setScene(optionsScene);
        stage.show();
    }


    public
    void switchToLoadedGame(ActionEvent event)
    {
        loadGameButtonImage.setImage(clickbuttonImage);
        stage = (Stage) ((Node) event.getSource()).getScene()
                .getWindow();
    }

    public
    void switchToNewGame(ActionEvent event) throws IOException
    {
        newGameButtonImage.setImage(clickbuttonImage);
        stage = (Stage) ((Node) event.getSource()).getScene()
                .getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("playerselect.fxml"));
        Scene      scene      = new Scene(fxmlLoader.load(), GameInfo.x, GameInfo.y);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public
    void initialize(URL url, ResourceBundle resourceBundle)
    {
        newGameButtonImage.setImage(buttonImage);
        loadGameButtonImage.setImage(buttonImage);
        optionsButtonImage.setImage(buttonImage);
        exitButtonImage.setImage(buttonImage);
        logoImage.setImage(logo);
        menuBackgroundImage.setImage(bg);
    }
}
