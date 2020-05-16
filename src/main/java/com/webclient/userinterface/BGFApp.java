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
import java.awt.Toolkit;
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

public class BGFApp extends Application {
    // Init constants
    private static final String folder = "dist/plugins";
    private static final String WebSocketDir = "http://localhost:8001";
    private static int id = 1; // Or id of player init the app
    
    //Minimize vars
    //DisplayTrayIcon DTI = new DisplayTrayIcon();
    private boolean firstTime;
    private TrayIcon trayIcon;
    private static final String iconImageLoc ="https://cdn3.iconfinder.com/data/icons/electronic-device-line/128/Joystick-512.png";
    private static Thread watcherProcess;
        
    //Attributes 
    public static ArrayList<PlayerSummaryAttribute> attributes= new ArrayList();
    public static ArrayList<AttributePlayer> attributesAll= new ArrayList();
    
    //Sensors List and 
    public static ArrayList<SensorNeed> ListSensors = new ArrayList();
    public static ArrayList<Integer> Players = new ArrayList();
    private static SensorSubject sensor;
    //Init Observer
    public static ObsPlayer obsp= new ObsPlayer();
    public static ArrayList<SensorSubject> sensorsSubs = new ArrayList();
    
    //Socket for WebSocket communication
    private static WebSocketServerInit websock;
    public static Socket socket;
        
    
    public static FXMLController myControllerHandle;
    private Stage stage0;
    
    
    
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
        myControllerHandle = (FXMLController)loader.getController();
        myControllerHandle.buttonReconnectSens.setText("Reset sensors");
        
        //Button event
        myControllerHandle.buttonReconnectSens.setOnAction((event)->{
            // Button was clicked, do something...
            for (int i = 0; i < sensorsSubs.size(); i++) {
                System.out.println("Kill thread of sensors: " + i);
                sensorsSubs.get(i).Stopp();
            }
            ListSensors.clear();
            myControllerHandle.reloadDataForTables();
            System.out.println("Exit if kill threads of sensors!");
            for (int i = 0; i < sensorsSubs.size(); i++) {
                System.out.println("Star thread of sensors: " + i);
                sensorsSubs.get(i).start();
            }
            System.out.println("OK all thread of sensors Start!");
            
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
        
        
        
        /*stage0.setOnCloseRequest( event ->
        {
            System.out.println("CLOSING");
            socket.disconnect();
            
        });*/
        
        socketComunicationInit(WebSocketDir);
                
    }

    
    
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
                    stage.close();
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
    
    
    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("Blended Games Framework.",
                    "We are working in the background!.",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

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
    
    /**
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException{
        
        //Only have one player in this app
        //PlayerConecction();
        Players.add(1);
        setId(Players.get(0));
   
        getAttributesAndSummary(id);
        
        createPluginFolder(folder);
        //WebSocketServer Initialize and Start
        websock = new WebSocketServerInit("");
        websock.run();
        
        //Search and start plugins
        searchPlugins(folder);
        
        Runnable process = new DirectoryWatcher();
        
        
        watcherProcess = new Thread(process);
        watcherProcess.start();
        
        launch(args);
        
        closeAll();
                       
        //System.exit(0);
    }
    
    //Function for get Attributes an summary of IdPlayer
    private static void getAttributesAndSummary(int idPlayer){
        System.out.println("Connecting to virtual profile of player");
        try {
            
            System.out.println("Identificator of player is:" +idPlayer);
            Send_HTTP_Request2.call_resum_attributes(attributes,"http://localhost:3030/getAttributesSummary/"+Integer.toString(idPlayer));
            Send_HTTP_Request2.call_all_attributes(attributesAll,"http://localhost:3030/getAttributes/"+Integer.toString(idPlayer));
        } catch (Exception e) {
            System.out.println("-- Failed to connect to information microservices --");
       }
    }

    //Close all threads and stop app
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
        System.out.println("WebSocket interrupt! ");
                
        for (int i = 0; i < sensorsSubs.size(); i++) {
            System.out.println("Killing sensors...: " + i);
            sensorsSubs.get(i).Stopp();
        }
        System.out.println("He left the For to close Sensors!");
    }
    
    /**
     * @return the id
     */
    public static int getId() {
        return id;
    }

    /**
     * @param aId the id to set
     */
    public static void setId(int aId) {
        id = aId;
    }
    
    
    /**
 * Shows the person overview inside the root layout.
 */
    public void showOverview() {
        // Load overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BGFApp.class.getResource("view/PersonOverview.fxml"));
        // Give the controller access to the main app.
        FXMLController controller = loader.getController();
        controller.reloadDataForTables();
    }
    
    private static void createPluginFolder(String folder){
        //Init folder of plugins
        if (Files.notExists(Paths.get(folder))) {
            // automatically created
            File dir = new File("dist\\plugins");
            boolean isCreated = dir.mkdirs(); 
            System.out.println("its create? "+isCreated); //check the path with System.out
        } 
    }
    
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
    
    private static void initPlugins(File[] jars){
        if (jars.length >=1){
            for (int i=0; i<jars.length; i++) {
                //Sensor added
                sensor = new SensorSubject("Jaime",jars[i], getId());
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
    
    //For communication of application with WebSocket server
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
    
    
}
