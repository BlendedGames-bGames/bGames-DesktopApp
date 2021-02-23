/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgapp.utiilities;

import static com.webclient.userinterface.BGFApp.ListSensors;
import static com.webclient.userinterface.BGFApp.Players;
import static com.webclient.userinterface.BGFApp.myControllerHandle;
import static com.webclient.userinterface.BGFApp.sensorsSubs;
import static com.webclient.userinterface.BGFApp.setIdPlayer;
import com.webclient.userinterface.FXMLController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javax.swing.JOptionPane;
import org.json.JSONObject;
        
/**
 * API REST Requests
 * @author Bad-K
 */
public class Send_HTTP_Request2 {
        private static final String USER_AGENT = "Mozilla/5.0";


    /*
    * Input: Void array of a player's summary of attributes
    * Output: Full array of data
    * Description: HTTPUrlConnection for the HTTP request, reformat the output of the request and adding it to the array
    */    
    public static void call_resum_attributes(ArrayList<PlayerSummaryAttribute> attributes, String urlEnter ) throws Exception {
         String url = urlEnter;
         URL obj = new URL(url);
         HttpURLConnection con = (HttpURLConnection) obj.openConnection();
         // optional default is GET
         con.setRequestMethod("GET");
         //add request header
         con.setRequestProperty("User-Agent", "Mozilla/5.0");
         int responseCode = con.getResponseCode();
         System.out.println("\nSending 'GET' request to URL : " + url);
         System.out.println("Response Code : " + responseCode);
         BufferedReader in = new BufferedReader(
                 new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
         String inputLine;
         StringBuffer response = new StringBuffer();
         while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }
         in.close();
         //print in String
         //System.out.println(response.toString());
         //Read JSON response and print

         String aux = (response.toString()).substring(1, (response.toString()).length()-1);


         String[] resumeAttributes;
         resumeAttributes = aux.split("\\}\\,\\{|\\{|\\}");

         PlayerSummaryAttribute attaux;
         JSONObject myResponse;
         String auxString;
         int contador = 0;
         for (String attribute : resumeAttributes){
             if (contador !=0){
             auxString = "{"+attribute+"}";
             myResponse = new JSONObject(auxString);
             //System.out.println("Linea ------: "+myResponse.length());
             try{
                 attaux = new PlayerSummaryAttribute();
                 attaux.setId(myResponse.getInt("id_attributes"));
                 attaux.setName(myResponse.getString("name"));
                 attaux.setData(myResponse.getInt("data"));
                 attaux.setData_type(removeLastCharacter(myResponse.getString("data_type")));
                 attaux.setDate_time(myResponse.getString("date_time"));
                 attributes.add(attaux);
             } catch (Exception e){
                 e.printStackTrace ();
             }
             } else {contador = 1;}
        }
         /*System.out.println("Linea 00: "+attributes.size());
         System.out.println("Linea 0: "+"{"+resumeAttributes[0]+"}");
         System.out.println("Linea 1: "+resumeAttributes[1]);
         System.out.println("Linea 2: "+resumeAttributes[2]);
         System.out.println("Linea 3: "+resumeAttributes[3]);
         System.out.println("Linea 4: "+resumeAttributes[4]);
         System.out.println("Linea 5: "+resumeAttributes[5]);*/

         /*
         JSONObject myResponse = new JSONObject(response.toString());

         System.out.println("result after Reading JSON Response");
         System.out.println("statusCode- "+attributes.getString("statusCode"));
         System.out.println("statusMessage- "+attributes.getString("statusMessage"));
         System.out.println("ipAddress- "+attributes.getString("ipAddress"));
         System.out.println("countryCode- "+attributes.getString("countryCode"));*/
       }

    /*
    * Input: Void array of a player's attributes
    * Output: Full array of data
    * Description: HTTPUrlConnection for the HTTP request, reformat the output of the request and adding it to the array
     */
    public static void call_all_attributes(ArrayList<AttributePlayer> attributes,String urlEnter) throws Exception {
         String url = urlEnter;
         URL obj = new URL(url);
         HttpURLConnection con = (HttpURLConnection) obj.openConnection();
         // optional default is GET
         con.setRequestMethod("GET");
         //add request header
         con.setRequestProperty("User-Agent", "Mozilla/5.0");
         int responseCode = con.getResponseCode();
         //System.out.println("\nSending 'GET' request to URL : " + url);
         //System.out.println("Response Code : " + responseCode);
         BufferedReader in = new BufferedReader(
                 new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
         String inputLine;
         StringBuffer response = new StringBuffer();
         while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }
         in.close();
         //print in String
         String string = Normalizer.normalize(response.toString(), Normalizer.Form.NFD);
         //System.out.println(string);
                 //.replaceAll("[^\\p{ASCII}]", ));
         //Read JSON response and print

         String aux = (response.toString()).substring(1, (response.toString()).length()-1);


         String[] resumeAttributes;
                resumeAttributes = aux.split("\\}\\,\\{|\\{|\\}");

         //ArrayList<AttributePlayer> attributes= new ArrayList();
         AttributePlayer attaux;
         JSONObject myResponse;
         String auxString;
         int contador = 0;
         for (String attribute : resumeAttributes){
             if (contador !=0){
             auxString = "{"+attribute+"}";
             myResponse = new JSONObject(auxString);
             //System.out.println("Linea ------: "+myResponse.length());
             try{
                 attaux = new AttributePlayer();
                 attaux.setId(myResponse.getInt("id_subattributes"));
                 attaux.setName(myResponse.getString("nameat"));
                 attaux.setName_category(myResponse.getString("namecategory"));
                 attaux.setData(myResponse.getInt("data"));
                 attaux.setData_type(myResponse.getString("data_type"));
                 attaux.setInput_source(myResponse.getString("input_source"));
                 attaux.setDate_time(removeLastCharacter(myResponse.getString("date_time")));
                 attributes.add(attaux);
             } catch (Exception e){
                 e.printStackTrace ();
             }
             } else {contador = 1;}
        }
         /*System.out.println("Linea 00: "+attributes.size());
         System.out.println("Linea 0: "+attributes.get(0).getData());
         System.out.println("Linea 1: "+attributes.get(1).getData());
         System.out.println("Linea 2: "+attributes.get(2).getData());
         System.out.println("Linea 3: "+attributes.get(3).getData());*/

       }

    /*
    * Input: Full string
    * Output: String without the last character
    * Description: Encapsulation of a simple function
     */
    public static String removeLastCharacter(String str) {
       String result = null;
       if ((str != null) && (str.length() > 0)) {
          result = str.substring(0, str.length() - 1);
       }
       return result;
    }

    /*
    * Input: Void array of a player's summary of attributes
    * Output: Full array of data
    * Description: HTTPUrlConnection for the HTTP request, reformat the output of the request and adding it to the array
    */    
    /*
           
        var id_player = req.body.id_player
        var id_videogame = req.body.id_videogame
        // [2,20,4,0,0]
        var id_modifiable_mechanic = req.body.id_modifiable_mechanic
        // Ej: ['chess_blitz,records,win', 'elo','puzzle_challenge,record','puzzle_rush','chess_rapid,record,win']
        var data = req.body.data

    */
    public static String reduce_attribute_player(String urlEnter, int id_player, int id_videogame, int id_modifiable_mechanic, int data) throws Exception {
        System.out.println(urlEnter);
        System.out.println("hola como");

        String resultJson = new JSONObject().
                            put("id_player", id_player).
                            put("id_videogame", id_videogame).
                            put("id_modifiable_mechanic", id_modifiable_mechanic).
                            put("data", data)
                            .toString();
        
        System.out.println(resultJson);
        URL obj = new URL(urlEnter);
        StringBuilder response = null;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        try{                 

            // For POST only - START
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                System.out.println(resultJson);
                os.write(resultJson.getBytes());
                os.flush();
                // For POST only - END
            }

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            try ( //success
                BufferedReader in = new BufferedReader(new java.io.InputStreamReader(
                        con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {                


                    // print result
                    System.out.println(response.toString());
                    String jsonString = response.toString();
                    JSONObject properJSON = new JSONObject(jsonString);
                    Boolean message = (Boolean) properJSON.get("message");
                    int consumeData = (int) properJSON.get("data");
                    JSONObject reply = new JSONObject();
                    reply.put("message", message);
                    reply.put("data", consumeData);
                    //Se tienen suficientes atributos para gastar
                    String replyString = reply.toString();
                    return replyString;
                   

            }
            else{
                return "";
            }
        }catch(FileNotFoundException ex){
            System.out.println(ex);
            System.out.println("Se encontro un error al hacer la llamada para gastar atributos");
            return "";
        }
    }
    

    /*
    * Input: Authentication Microservice's host url and player's name and password to be log in
    * Output: Player's id if it exists in the database
    * Description: HTTPUrlConnection for the HTTP request, reformat the output of the request storing the player's id
     */
    public static void call_id_Player(int id, String HOST, String name, String password) throws MalformedURLException, IOException {
        String url = HOST+name+"/"+password;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        StringBuffer response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        //print in String
        System.out.println("aqui deberia estar");
        System.out.println(response.toString());
        System.out.println("este es el resultado");
        //Read JSON response and print

        String aux = (response.toString()).substring(1, (response.toString()).length()-1);

        id = Integer.parseInt(aux);
    }

}