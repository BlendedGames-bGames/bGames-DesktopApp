/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgapp.utiilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * API REST Requests
 * @author Bad-K
 */
public class Send_HTTP_Request2 {

        
    public static void call_resum_attributes(ArrayList<PlayerSummaryAttribute> attributes, String urlEnter ) throws Exception {
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


    public static String removeLastCharacter(String str) {
       String result = null;
       if ((str != null) && (str.length() > 0)) {
          result = str.substring(0, str.length() - 1);
       }
       return result;
    }

    
    //Change this if you create a new method to managment accounts
    public static void call_id_Player(int id, String HOST, String name, String password) throws MalformedURLException, IOException {
        String url = HOST+"//"+name+"//"+password;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
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
        //System.out.println(response.toString());
        //Read JSON response and print

        String aux = (response.toString()).substring(1, (response.toString()).length()-1);
        
        id = 1;
    }

}