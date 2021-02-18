package com.webclient.userinterface;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.TextField;
import java.awt.event.ActionEvent;
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
/**
 * FXML Controller class
 *
 * @author lobo_
 */
public class LoginController implements Initializable {
    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String POST_URL = "http://144.126.216.255:3006/desktop_authentication_key";

    @FXML
    private Label txtLabel;

    @FXML
    private Label txtLabelEmail;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnFirebase;

    @FXML
    private Button btnGoogle;

    @FXML
    private Button btnFacebook;

    @FXML
    private Label txtLabelPass;

    @FXML
    private PasswordField txtPass;

    @FXML
    private Label txtLabelKey;

    @FXML
    private TextField txtKey;
    
    
    private static void loginCall(JSONObject userData) throws MalformedURLException, IOException{
        String userDataString = userData.toString();
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(userDataString.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                }
                in.close();

                // print result
                System.out.println(response.toString());
        } else {
                System.out.println("POST request not worked");
        }
    }
    

    @FXML
    void login(ActionEvent event) throws IOException, JSONException {
        String email = txtEmail.getText();
        String pass = txtPass.getText();
        String key = txtKey.getText();
        String provider = "firebase.com";

        JSONObject resultJson = new JSONObject().put("email", email).put("pass", pass).put("key", key).put("provider", provider);
 
        loginCall(resultJson);
    }
    

    @FXML
    void loginFacebook(ActionEvent event) throws JSONException, IOException {
        String email = txtEmail.getText();
        String key = txtKey.getText();
        String provider = "facebook.com";
        JSONObject resultJson = new JSONObject().put("email", email).put("key", key).put("provider", provider);
 
        loginCall(resultJson);
    }

    @FXML
    void loginGoogle(ActionEvent event) throws JSONException, IOException {
        String email = txtEmail.getText();
        String key = txtKey.getText();
        String provider = "google.com";
        JSONObject resultJson = new JSONObject().put("email", email).put("key", key).put("provider", provider);
 
        loginCall(resultJson);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
