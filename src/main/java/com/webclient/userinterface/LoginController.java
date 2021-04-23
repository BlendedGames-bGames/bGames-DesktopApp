package com.webclient.userinterface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.json.JSONObject;
import java.io.InputStreamReader;
import org.json.JSONException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author lobo_
 */
public class LoginController implements Initializable {
    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String POST_URL = "http://144.126.216.255:3006/desktop_authentication_key";

    @FXML
    public Label txtLabel;

    @FXML
    public Label txtLabelEmail;

    @FXML
    public TextField txtEmail;

    @FXML
    public Button btnFirebase;

    @FXML
    public Button btnGoogle;

    @FXML
    public Button btnFacebook;

    @FXML
    public Label txtLabelPass;

    @FXML
    public PasswordField txtPass;

    @FXML
    public Label txtLabelKey;

    @FXML
    public TextField txtKey;

    
    
    public void loginCall(JSONObject userData) throws MalformedURLException, IOException{

    }
    

    @FXML
    public void login(ActionEvent event) throws IOException, JSONException {
     
    }
    

    @FXML
    public void loginFacebook(ActionEvent event) throws JSONException, IOException {
     
    }

    @FXML
    public void loginGoogle(ActionEvent event) throws JSONException, IOException {
      
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
