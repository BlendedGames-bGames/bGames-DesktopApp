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
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BGFApp extends Application {
    // Init constants
    private static final String FOLDER = "dist/plugins";
    public static final String WEBSOCKET_HOST = "http://localhost:8003";
    //private static final String ATTRIBUTES_HOST2 = "http://localhost:3030/getAttributes/";
    private static final String ATTRIBUTES_HOST = "https://bgames-attributedisplayconfig.herokuapp.com/getAttributes/";

    //private static final String SUMMARY_HOST2 = "http://localhost:3030/getAttributesSummary/";
    private static final String SUMMARY_HOST = "https://bgames-attributedisplayconfig.herokuapp.com/getAttributesSummary/";
    
    //private static final String SUMMARY_HOST_ATTRIBUTES = "http://localhost:3030/putAttributes/bycategory/";

    private static final String SUMMARY_HOST_ATTRIBUTES = "https://bgames-attributedisplayconfig.herokuapp.com/putAttributes/bycategory/";

    //private static final String ACCOUNTS_HOST2 = "http://localhost:3000/player/";
    private static final String ACCOUNTS_HOST = "https://bgames-configurationservice.herokuapp.com/player/";

    
    //Account vars
    public static String nameAccount = "Player1";
    public static int idPlayer = 1; // Or idPlayer of player init the app
    public static ArrayList<Integer> Players = new ArrayList();

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
             try {            
           
              Send_HTTP_Request2.reduce_attribute_player(attributes,SUMMARY_HOST_ATTRIBUTES+Integer.toString(idPlayer)+"/"+"Físico",Integer.toString(idPlayer), "Físico");

            } catch (Exception e) {
                //System.out.println("-- Failed to connect to information microservices --");
            }
            JSONObject obj = new JSONObject();
            JSONObject obj2 = new JSONObject();

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
            }

            socket2.emit("AllSensors");
            
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
    
    //Sensors 
    public static ArrayList<SensorNeed> ListSensors = new ArrayList();
    private static SensorSubject sensor;
    
    //Init Observer
    public static ObsPlayer obsp= new ObsPlayer();
    public static ArrayList<SensorSubject> sensorsSubs = new ArrayList();
    
    //Socket for WebSocket communication
    private static WebSocketServerInit websock;
    public static Socket socket;
    public static Socket socket2;

    //FMX Controller
    public static FXMLController myControllerHandle;
    private Stage stage0;
    
   
    public static void main(String[] args) throws IOException, JSONException{
        
        //Only have one player in this app
        //PlayerGetPassword();
        String password = "pass001";
        getIDofPlayer(nameAccount,password);
        Players.add(idPlayer);
        setIdPlayer(Players.get(0));
        //Get attributes and summary of Player
        System.out.println(idPlayer);
        getAttributesAndSummary(idPlayer);
        //Create plugin folder if dosent exist
        createPluginFolder(FOLDER);
        
        //WebSocketServer Initialize and Start
        websock = new WebSocketServerInit("");
        websock.run();
        System.out.println("Ya existe el websocketserver en el 8001");
        
        socketCommunicationPodometer();
        JSONObject obj = new JSONObject();

        obj.put("room", "SensorCerebral");
        obj.put("name", "Mindwave");
        socket2.emit("join_sensor", obj);

        //Search and start plugins
        searchPlugins(FOLDER);
        //Run thread of directory of plugins watcher
        Runnable process = new DirectoryWatcher();
        watcherProcess = new Thread(process);
        watcherProcess.start();
        
        launch(args);
        
        closeAll();
    }
    
  
      
    
    
    /*
    
          USER DATA SECTION
    
    
    */
        
    
    /*
    * Input: Id of a player (range 0 to positive int)
    * Output: Set PlayerSummaryAttribute array 
    * Description: Gets all the attributes and the summary of them of an specific player
    * to be rendered in the javaFX interface
    */
    private static void getAttributesAndSummary(int idPlayer){
        //System.out.println("Connecting to virtual profile of player");
        try {            
            //System.out.println("Identificator of player is:" +idPlayer);
            Send_HTTP_Request2.call_resum_attributes(attributes,SUMMARY_HOST+Integer.toString(idPlayer));
            Send_HTTP_Request2.call_all_attributes(attributesAll,ATTRIBUTES_HOST+Integer.toString(idPlayer));

        } catch (Exception e) {
            //System.out.println("-- Failed to connect to information microservices --");
       }
    }
    
    /*
    * Input: Name and password of a player
    * Output: Id of the player (range 0 to positive int)
    * Description: Passes both parameters to be validated by the respective authentication microservice and get the user's id
    */
    private static void getIDofPlayer(String Name, String Password){
        System.out.println("Connecting to virtual profile of player");
        try {
            Send_HTTP_Request2.call_id_Player(idPlayer,ACCOUNTS_HOST,Name,Password);
        } catch (Exception e) {
            //System.out.println("-- Failed to connect to information microservices --");
       }
    }
   
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();

        //Now we have access to getController() through the instance... don't forget the type cast
        myControllerHandle = (FXMLController) loader.getController();
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
        //Button event
        myControllerHandle.buttonConsumeAtt.setOnAction((event) -> {
            batchDataSpendQuestion();

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
        

        socketComunicationInit(WEBSOCKET_HOST);
       
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
                //System.out.println(ex);
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
    
    public static void socketCommunicationPodometer(){
        try{
              socket2 = IO.socket("http://localhost:8001");

        }
        catch (URISyntaxException ex) {
            
        }
        socket2.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
        @Override
        public void call(Object... os) {
            System.out.println("Sensor connected to the websocket"); //To change body of generated methods, choose Tools | Templates.
        }

      }).on("join_sensor", new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            socket2.emit("SensorCerebral", "Mindwave Neurosky");
        }

      }).on("AllSensors", new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                //System.out.println("Args[0] enter: "+args[0]);
                JSONObject obj = (JSONObject)args[0];
                System.out.println("Sensors: "+obj.getString("sensoresActivos"));
            } catch (JSONException ex) {
            }
        }

      }).on("message", new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            try {
                JSONObject obj = (JSONObject)args[0];
                System.out.println("Objet nome: "+obj.getString("name"));
                System.out.println("Objet message: "+obj.getString("message"));
            } catch (JSONException ex) {
                System.out.println("Failed to get object or name");
            }
        }

      }).on("Imessage", new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            try {
                JSONObject obj = (JSONObject)args[0];
                System.out.println("Objet nome: "+obj.getString("name"));
                System.out.println("Objet message: "+obj.getString("message"));
            } catch (JSONException ex) {
                System.out.println("Failed to get object or name");
            }
        }

      }).on("Smessage", new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            try {
                JSONObject obj = (JSONObject)args[0];
                System.out.println("Objet nome: "+obj.getString("name"));
                System.out.println("Objet message: "+obj.getString("message"));
            } catch (JSONException ex) {
                System.out.println("Failed to get object or name");
            }
        }

      }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

        @Override
        public void call(Object... args) {}

      });           
      System.out.println("me conecte");

      socket2.connect();

    }
    
    
    /*
    * Input: Web socket port host string
    * Output: Started websocket channel
    * Description: Boilerplate code for initiating a web socket server using Socket IO library
     */
    public void socketComunicationInit(String WebSocketDir0) throws URISyntaxException{
        socket = IO.socket(WebSocketDir0);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                System.out.println("Connection OK!"); //To change body of generated methods, choose Tools | Templates.
            }

          }).on("join_sensor", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("RoomOfBFApp", "Blended Games Framework Desktop Aplication!");
            }

          }).on("AllSensors", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    System.out.println("Args[0] enter: "+args[0]);
                    JSONObject obj = (JSONObject)args[0];
                    System.out.println("Sensors: "+obj.getString("sensoresActivos"));
                } catch (JSONException ex) {
                    Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

          }).on("message", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject)args[0];
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
                    System.out.println("Objet nome: "+obj.getString("name"));
                    System.out.println("Objet message: "+obj.getString("message"));
                } catch (JSONException ex) {
                    System.out.println("Failed to get object or name");
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
    
    
}
