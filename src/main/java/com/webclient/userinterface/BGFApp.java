package com.webclient.userinterface;

import mod.sensors.ObsPlayer;
import bgapp.utiilities.Send_HTTP_Request2;
import websocket.init.server.WebSocketServerInit;
import mod.sensors.SensorSubject;
import bgapp.utiilities.PlayerSummaryAttribute;
import bgapp.utiilities.AttributePlayer;
import bgapp.utiilities.SensorNeed;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static javafx.application.Application.launch;
import mod.sensors.DirectoryWatcher;


import java.awt.AWTException;
import java.awt.KeyboardFocusManager;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BGFApp extends Application {
    // Init constants
    private static final String FOLDER = "dist/plugins";
    public static final String WEBSOCKET_HOST = "http://localhost:8001";
    //private static final String ATTRIBUTES_HOST2 = "http://localhost:3030/getAttributes/";
    private static final String ATTRIBUTES_HOST = "https://bgames-attributedisplayconfig.herokuapp.com/getAttributes/";

    //private static final String SUMMARY_HOST2 = "http://localhost:3030/getAttributesSummary/";
    private static final String SUMMARY_HOST = "https://bgames-attributedisplayconfig.herokuapp.com/getAttributesSummary/";
    
    //private static final String SUMMARY_HOST_ATTRIBUTES = "http://localhost:3030/putAttributes/bycategory/";

    private static final String SUMMARY_HOST_ATTRIBUTES = "http://144.126.216.255:3008/spend_attributes_apis";
    private static final String LOGOUT_HOST = "http://144.126.216.255:3006/logout";
    private static final String CONSUME_ATTRIBUTES = "http://144.126.216.255:3008/consume_attributes";

    //private static final String ACCOUNTS_HOST2 = "http://localhost:3000/player/";
    private static final String ACCOUNTS_HOST = "https://bgames-configurationservice.herokuapp.com/player/";

    
    //Account vars
    public static String nameAccount = "Player1";
    public static int idPlayer = 1; // Or idPlayer of player init the app
    public static ArrayList<Integer> Players = new ArrayList();
    
    public static String consumedAtt;
    public static String expensedAtt;
    /*
    private static void batchDataSpendQuestion() {
        Object[] options = {"Yes, please",
                    "No way!"};
        JFrame frame = new JFrame();
        int n = JOptionPane.showOptionDialog(frame,
            "Would you like to spend your physical attribute data in the unity pong game ?",
            "Pedometer sensor question",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,     //do not use a custom Icon
            options,  //the titles of buttons
            options[0]); //default button title
        
        System.out.println(n);
        if(n == 0){
            //System.out.println("Data Found: " + _connection.getData());
            //notifica(this._sensorNeed);
            Boolean response = false;
             try {            
                
              int id_videogame = 1; //Ultimate-Tetris-3D id
              int id_modifiable_mechanic = 1; //Slow down blocks id in tetris
              int data = 1; //Data to convert
              response = Send_HTTP_Request2.reduce_attribute_player(SUMMARY_HOST_ATTRIBUTES,idPlayer, id_videogame,id_modifiable_mechanic,data);
              JSONObject obj = new JSONObject();
              JSONObject obj2 = new JSONObject();
              //Se tienen suficientes atributos para gastar
              if(response){
                  JOptionPane.showMessageDialog(null,"Se gastaron atributos" );
                  try {
                    obj.put("room", "SensorCerebral");
                    obj.put("name", "Mindwave");
                    java.util.Date dt = new java.util.Date();
                    java.text.SimpleDateFormat dataTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = dataTime.format(dt);
                    obj2.put("id_player", idPlayer);
                    obj2.put("nameat", "Resistencia");
                    obj2.put("namecategory", "Físico");
                    obj2.put("data", 50);
                    obj2.put("data_type", "por.on");
                    obj2.put("input_source", "ericblue.mindstream.client.ThinkGearSocketClient - Mindwave.Mobile: MW003");
                    obj2.put("date_time", currentTime);
                    obj.put("message",obj2);
                    socket2.emit("message",obj);  
                } catch (JSONException ex) {
                    System.out.println(ex);
                }
                socket2.emit("AllSensors");
              }
              else{
                  JOptionPane.showMessageDialog(null,"No se tienen suficientes atributos" );
              }
            } catch (Exception e) {
                //System.out.println("-- Failed to connect to information microservices --");
            }
           
            
        }
        
        
    }
    */

    private static void powerUpAction(JSONObject JSONInfo, String roomName) {
        
        System.out.println(JSONInfo);
        try {            
            
            int id_videogame = (int) JSONInfo.get("id_videogame"); //Ultimate-Tetris-3D id
            int id_modifiable_mechanic =  (int) JSONInfo.get("id_modifiable_mechanic"); //Slow down blocks id in tetris
            int data = (int) JSONInfo.get("data"); //Data to convert
            String response = Send_HTTP_Request2.reduce_attribute_player_petition(SUMMARY_HOST_ATTRIBUTES,idPlayer, id_videogame,id_modifiable_mechanic,data);
            JSONObject responseJson = new JSONObject(response);
            //Se tienen suficientes atributos para gastar
            if((Boolean) responseJson.get("message")){
                //room,message,name
                JSONObject sendJSON = new JSONObject();
                sendJSON.put("room", roomName);
                sendJSON.put("name", roomName);
                int[] message = { (int) responseJson.get("data"), (int) responseJson.get("modified_mechanic")};
                sendJSON.put("message",message);
                consumedAtt = (String) responseJson.get("consumedAtt");
                expensedAtt = (String) responseJson.get("expensedAtt");
                System.out.println("He guardado el json y el string de los puntos a consumir");

                System.out.println(consumedAtt);
                System.out.println(expensedAtt);

                System.out.println(sendJSON);
                socket.emit("message",sendJSON);
            }
            else{
                JOptionPane.showMessageDialog(null,"No se tienen suficientes atributos" );
            }
        } catch (Exception e) {
            System.out.println(e);
            
            //System.out.println("-- Failed to connect to information microservices --");
        }
        
    }

    
    //Minimize vars
    //DisplayTrayIcon DTI = new DisplayTrayIcon();
    private boolean firstTime;
    private TrayIcon trayIcon;
    private static Thread watcherProcess;
        
    //Attributes 
    public static ArrayList<PlayerSummaryAttribute> attributes= new ArrayList();
    public static ArrayList<AttributePlayer> attributesAll= new ArrayList();
    
    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String POST_URL = "http://144.126.216.255:3006/desktop_authentication_key";
    
    //Sensors 
    public static ArrayList<SensorNeed> ListSensors = new ArrayList();
    private static SensorSubject sensor;
    
    //Init Observer
    public static ObsPlayer obsp= new ObsPlayer();
    public static ArrayList<SensorSubject> sensorsSubs = new ArrayList();
    
    //Socket for WebSocket communication
    private static WebSocketServerInit websock;
    public static Socket socket;

    //FMX Controller
    public static FXMLController myControllerHandle;
    public static LoginController LoginController;

    private Stage stage0;
    

   
    public static void main(String[] args) throws IOException, JSONException, URISyntaxException{
        
        //Only have one player in this app
     
        //Create plugin folder if dosent exist
        createPluginFolder(FOLDER);
        
        //WebSocketServer Initialize and Start
        websock = new WebSocketServerInit("");
        websock.run();
        System.out.println("Ya existe el websocketserver en el 8001");
        
        socketComunicationInit(WEBSOCKET_HOST);
        JSONObject obj = new JSONObject();
        obj.put("room", "RoomOfBFApp");
        obj.put("name", "Blended Games Framework Desktop Aplication");
        socket.emit("join_BG", obj);
      
        
        launch(args);
        
        closeAll();
    }
    
    /*
    
          USER DATA SECTION
    
    
    */
        
 
   
    /*
    * Description: @return the idPlayer
    */
    public static int getIdPlayer() {
        return idPlayer;
    }

    /*
     * Description: @set the idPlayer
    */
    public static void setIdPlayer(int aId) {
        idPlayer = aId;
    }
    
    /*
    
          USER INTERFACE SECTION
    
    
    */    
        
    /*
    * Input: Name of the plugin folder in string format
    * Output: Styled user interface with the corresponding listeners of the components 
    * Description: Creates the trayIcon (typical top right buttons), header and css (fxml file) and initializes the websocket channel
    */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage0 = stage;

        //set tray icon
        createTrayIcon(stage0);
        firstTime = true;
        Platform.setImplicitExit(false);

        //Set up instance instead of using static load() method
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();

        //Now we have access to getController() through the instance... don't forget the type cast
        LoginController = (LoginController) loader.getController();
        LoginController.btnGoogle.setOnAction((ActionEvent event) -> {
            String email = LoginController.txtEmail.getText();
            String key = LoginController.txtKey.getText();
            String provider = "google.com";
            JSONObject resultJson;
            try {
                resultJson = new JSONObject().put("email", email).put("key", key).put("provider", provider);                
                loginCall(resultJson,email);
            } catch (JSONException | IOException ex) {
                Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        LoginController.btnFacebook.setOnAction((ActionEvent event) -> {
            String email = LoginController.txtEmail.getText();
            String key = LoginController.txtKey.getText();
            String provider = "facebook.com";
            JSONObject resultJson;
            try {
                resultJson = new JSONObject().put("email", email).put("key", key).put("provider", provider);
                loginCall(resultJson,email);
            } catch (JSONException | IOException ex) {
                Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        LoginController.btnFirebase.setOnAction((ActionEvent event) -> {
            String email = LoginController.txtEmail.getText();
            String pass = LoginController.txtPass.getText();
            String key = LoginController.txtKey.getText();
            String provider = "password";
            
            JSONObject resultJson;
            try {
                resultJson = new JSONObject().put("email", email).put("pass", pass).put("key", key).put("provider", provider);
                loginCall(resultJson,email);
                
            } catch (JSONException | IOException ex) {
                Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Scene scene = new Scene(root);
        //Set Image icon to app
        //Image icon = new Image("dist/image");
        //stage.getIcons().add(icon);
        scene.getStylesheets().add("/styles/Styles.css");

        stage0.setScene(scene);
        stage0.show();
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());//this line may need a try-catch block
        

       
    }

    
    /*
    * Input: PersonOverview.fxml file
    * Output: Data tables reloaded
    * Description: Shows the person overview inside the root layout.
    */
    public void showOverview() {
        // Load overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BGFApp.class.getResource("view/PersonOverview.fxml"));
        // Give the controller access to the main app.
        FXMLController controller = loader.getController();
        controller.reloadDataForTables();
    }
    
    /*
    * Input: Blended games icon in a CDN backup
    * Output: Tray icons and header of the UI with the corresponding listeners
    * Description: Simple initialization boilderplate that describes itself
     */
    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                URL url = new URL("https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX13313675.jpg");
                image = ImageIO.read(url);
                //image = Toolkit.getDefaultToolkit().getImage("images/BGTanns.png");
                //image = ImageIO.read(new File(System.getProperty("user.dir")+"\\dist\\icons\\BGTranns.png"));

            } catch (IOException ex) {
                System.out.println(ex);
            }

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    tray.remove(trayIcon);
                    stage.close();
                    closeAll();
                    System.exit(0);
                }
            });
            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    //System.exit(0);
                    tray.remove(trayIcon);
                    //stage.close();
                    closeAll();
                    System.exit(0);
                }
            };

            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem("Open b-Games Framework App");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Exit");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "b-Games Framework App", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            // ...
        }
    }
    
    /*
    * Input: trayIcon
    * Output: Message that informs the user that Blended Games is actually wroking in the background
    * Description: SelfExplanatory
     */
    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Blended Games Framework.",
                    "We are working in the background!.",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    
    /*
    * Input: trayIcon
    * Output: Hides the UI
    * Description: SelfExplanatory
     */
    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
    }

      
    
        
      
    /*
    
          SENSOR PLUGINS SECTION
    
    
    */
            
    /*
    * Input: Name of the plugin folder in string format
    * Output: Created folder with the named passed beforehand
    * Description: Checks if the folder isn't created and if it's not, then create it
    */
    private static void createPluginFolder(String folder){
        //Init FOLDER of plugins
        if (Files.notExists(Paths.get(folder))) {
            // automatically created
            File dir = new File("dist\\plugins");
            boolean isCreated = dir.mkdirs(); 
            System.out.println("its create? "+isCreated); //check the path with System.out
        } 
    }
    
    /*
    * Input: Name of the plugin folder in string format
    * Output: Calls the initPlugins function with an array of .jar files filled beforehand
    * Description: Initializes the plugin folder and stores the files that contain a .jar extension in an array of File by
    * looping over all the existing files
    */    
    private static void searchPlugins(String folder){
        //Revisar Plugins en la carpeta Plugins
        File libs = new File(folder);
        File[] jars;
        jars = libs.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".jar");
            }       
        });
        initPlugins(jars);
        
    }
    
    /*
    * Input: Array of Files (.jar extension only) that correspond to plugins
    * Output: Started sensors correspondings to the plugins
    * Description: Initiates the plugins by creating a sensorSubject, websocket channel, observer and finally starting 
    * the observer pattern process
    */    
    private static void initPlugins(File[] jars){
        if (jars.length >=1){
            for (int i=0; i<jars.length; i++) {
                //Sensor added
                sensor = new SensorSubject(nameAccount,jars[i], getIdPlayer());
                sensor.setHostWebSocket(WEBSOCKET_HOST);
                sensor.addObvserver(obsp);
                sensorsSubs.add(sensor);
                sensor.start();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ex) {
            Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    * Input: Web socket port host string
    * Output: Started websocket channel
    * Description: Boilerplate code for initiating a web socket server using Socket IO library
     */
    public static void socketComunicationInit(String WebSocketDir0) throws URISyntaxException{
        socket = IO.socket(WebSocketDir0);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                System.out.println("Connection OK!"); //To change body of generated methods, choose Tools | Templates.
            }

        }).on("join_BG", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
                System.out.println("Se unio la app de Blended Games!"); //To change body of generated methods, choose Tools | Templates.
          }

        }).on("join_sensor", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
                System.out.println("Entre pero por aqui? OK!"); //To change body of generated methods, choose Tools | Templates.
          }

        }).on("join_sensor_offline", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
              try {
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("Objet nome: "+obj.getString("name"));
                  System.out.println("Objet message: "+obj.getString("room"));
                  JSONObject message = new JSONObject();        
                  message.put("room",obj.getString("name"));
                  message.put("name", obj.getString("room"));
                  socket.emit("join_sensor_offline",message);
                  
              } catch (JSONException ex) {
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on("join_sensor_videogame", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
              try {
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("Objet nome: "+obj.getString("name"));
                  System.out.println("Objet message: "+obj.getString("room"));
                  JSONObject message = new JSONObject();
                        System.out.println("Entro al de la app"); //To change body of generated methods, choose Tools | Templates.

                    message.put("room",obj.getString("name"));
                    message.put("name", obj.getString("room"));
                  socket.emit("join_sensor_videogame_confirmation",message);
                  
              } catch (JSONException ex) {
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on("JSensorOffline", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
              try {
             
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("Objet message: "+obj.getString("message"));
                  
              } catch (JSONException ex) {
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on("AllSensors", new Emitter.Listener() {
          @Override
          public void call(Object... args) {
              System.out.println("Args[0] enter: "+args[0]);
              JSONObject obj = (JSONObject)args[0];
          }

        }).on("message", new Emitter.Listener() {

          @Override
          public void call(Object... args) {
              try {
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("entre al mensaje original");
                  System.out.println("Objet nome: "+obj.getString("name"));
                  System.out.println("Objet message: "+obj.getString("message"));
              } catch (JSONException ex) {
                  System.out.println("Failed to get object or name");
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on("Smessage", new Emitter.Listener() {

          @Override
          public void call(Object... args) {
              try {
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("entre al mensaje normal");
                  System.out.println("Objet nome: "+obj.getString("name"));
                  System.out.println("Objet message: "+obj.getString("message"));
                  powerUpAction((JSONObject) obj.get("message"),obj.getString("name"));
              } catch (JSONException ex) {
                  System.out.println("Failed to get object or name");
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on("Dimessage", new Emitter.Listener() {

          @Override
          public void call(Object... args) {
              try {
                  JSONObject obj = (JSONObject)args[0];
                  System.out.println("entre al consumo de los mensajes");
                  System.out.println("Objet nome: "+obj.getString("name"));
                  System.out.println("Objet message: "+obj.getString("message"));
                  Send_HTTP_Request2.reduce_attribute_player_confirmation(CONSUME_ATTRIBUTES,consumedAtt,expensedAtt);

              } catch (JSONException ex) {
                  System.out.println("Failed to get object or name");
              } catch (Exception ex) {
                  Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
              }
          }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });
        
        
         socket.connect();
    }
    
    /*
    * Input: Websocket
    * Output: Stops all the sensors and the websocket channel
    * Description: Close all threads and stop app
     */    
    private static void closeAll(){
        socket.disconnect();
        socket.close();
        try {
            websock.stop();
        } catch (IOException ex) {
            Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        watcherProcess.interrupt();
        //ThreadWebSocket.interrupt();
        //System.out.println("WebSocket interrupt! ");
                
        for (int i = 0; i < sensorsSubs.size(); i++) {
            //System.out.println("Killing sensors...: " + i);
            sensorsSubs.get(i).Stopp();
        }
        //System.out.println("He left the For to close Sensors!");
    }
    public void logoutCall() throws IOException, Exception{
        String proper_url = LOGOUT_HOST+"/"+String.valueOf(getIdPlayer());
        System.out.println(proper_url);
        URL obj = new URL(proper_url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + proper_url);
        System.out.println("Response Code : " + responseCode);
        try (BufferedReader in = new BufferedReader(
                new java.io.InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //print in Strin
        }
        System.out.println(responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {  
                 setIdPlayer(0);
                 ListSensors.clear();
                 Players.clear();
                 sensorsSubs.clear();
                 
                 start(BGFApp.this.stage0);

        }
    }

    public void loginCall(JSONObject userData, String email) throws MalformedURLException, ProtocolException, IOException, JSONException {
        String userDataString = userData.toString();
        URL obj = new URL(POST_URL);
        StringBuilder response = null;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        try{                 

            // For POST only - START
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                System.out.println(userDataString);
                os.write(userDataString.getBytes());
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

                    System.out.println(response.toString());
                    String jsonString = response.toString();
                    JSONObject properJSON = new JSONObject(jsonString);
                    int id_player = (int) properJSON.get("id_player");
                    
                    Players.add(id_player);
                    setIdPlayer(Players.get(0));
                    
                    //Search and start plugins
                    searchPlugins(FOLDER);
                    //Run thread of directory of plugins watcher
                    Runnable process = new DirectoryWatcher();
                    watcherProcess = new Thread(process);
                    watcherProcess.start();
                    // print result
                   
                    
                    System.out.println(id_player);
                    //Set up instance instead of using static load() method
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
                    Parent root = loader.load();

                    //Now we have access to getController() through the instance... don't forget the type cast
                    myControllerHandle = (FXMLController) loader.getController();
                    myControllerHandle.initData(email);
                    myControllerHandle.buttonReconnectSens.setText("Reset sensors");

                    //Button event
                    myControllerHandle.buttonReconnectSens.setOnAction((event) -> {
                        // Button was clicked, do something...
                        for (int i = 0; i < sensorsSubs.size(); i++) {
                            //System.out.println("Kill thread of sensors: " + i);
                            sensorsSubs.get(i).Stopp();
                        }
                        ListSensors.clear();
                        myControllerHandle.reloadDataForTables();
                        //System.out.println("Exit if kill threads of sensors!");
                        for (int i = 0; i < sensorsSubs.size(); i++) {
                            //System.out.println("Star thread of sensors: " + i);
                            sensorsSubs.get(i).start();
                        }
                        //System.out.println("OK all thread of sensors Start!");

                    });
                     myControllerHandle.logout.setOnAction((ActionEvent event) -> {
                         try {
                             logoutCall();
                         } catch (IOException ex) {
                             Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                         } catch (Exception ex) {
                             Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                         }
                    }); 
                    Scene scene = new Scene(root);
                    //Set Image icon to app
                    //Image icon = new Image("dist/image");
                    //stage.getIcons().add(icon);
                    scene.getStylesheets().add("/styles/Styles.css");

                    stage0.setScene(scene);
                    stage0.show();
            }
        }catch(FileNotFoundException ex){
            System.out.println("pase por aqui");
            System.out.println(ex);
            JOptionPane.showMessageDialog(null,"Datos incorrectos, revise e intente nuevamente ingresar sus datos" );
            System.out.println("POST request not worked");
            System.out.println(ex);
        }

    }
    
    
}