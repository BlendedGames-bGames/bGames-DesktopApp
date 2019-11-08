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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static javafx.application.Application.launch;
import mod.sensors.DirectoryWatcher;

public class BGFApp extends Application {

    public static ArrayList<PlayerSummaryAttribute> attributes= new ArrayList();
    public static ArrayList<AttributePlayer> attributesAll= new ArrayList();
    public static ArrayList<String> Sensors = new ArrayList(); //BORRAR
    public static ArrayList<SensorNeed> ListSensors = new ArrayList();
    public static ArrayList<Integer> Players = new ArrayList();
    //public static BGFApp Initial;
    public static SensorSubject sensor;
    private static WebSocketServerInit websock;
    public static Socket socket;
    private static int id = 1;
    public static ObsPlayer obsp= new ObsPlayer();
    public static ArrayList<SensorSubject> sensores = new ArrayList();
    public static FXMLController myControllerHandle;
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
         //Set up instance instead of using static load() method
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();

        //Now we have access to getController() through the instance... don't forget the type cast
        myControllerHandle = (FXMLController)loader.getController();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setScene(scene);
        stage.show();
        
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());//this line may need a try-catch block
        System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
        System.out.println(currentDir);
        
        
        stage.setOnCloseRequest( event ->
        {
            System.out.println("CLOSING");
            socket.disconnect();
            
        });
        
        socket = IO.socket("http://localhost:8001");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                System.out.println("A LA VERGA ENTENDI"); //To change body of generated methods, choose Tools | Templates.
            }

          }).on("join_sensor", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("SensorCerebral", "Mindwave Neurosky");
            }

          }).on("AllSensors", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    System.out.println("Lo que entra: "+args[0]);
                    JSONObject obj = (JSONObject)args[0];
                    System.out.println("Lo capturado: "+obj.getString("sensoresActivos"));
                } catch (JSONException ex) {
                    Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

          }).on("message", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject)args[0];
                    System.out.println("Objeto nombre: "+obj.getString("name"));
                    System.out.println("Objeto message: "+obj.getString("message"));
                } catch (JSONException ex) {
                    System.out.println("Error de obtener el objeto o nombre");
                    Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

          }).on("Smessage", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject)args[0];
                    System.out.println("Objeto nombre: "+obj.getString("name"));
                    System.out.println("Objeto message: "+obj.getString("message"));
                } catch (JSONException ex) {
                    System.out.println("Error de obtener el objeto o nombre");
                    Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

          }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

          });/*.on(Socket.EVENT_CONNECT,  new Emitter.Listener(){
            @Override
            public void call(Object... args) {
              socket.emit("foo", "hi");
              socket.disconnect();
            }
            
              
          });*/
        
        
        socket.connect();
        
        
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, JSONException{
        
        //Asumiremos un solo jugador:
        Players.add(1);
        setId(Players.get(0));
   
        System.out.println("SE ESTA CONECTADOOOOOO DÑASDAS DAS DAS DAS");
        try {
            System.out.println("EL NUMERO ES:" +getId());
         Send_HTTP_Request2.call_resum_attributes(attributes,"http://localhost:3030/getAttributesSummary/"+Integer.toString(getId()));
         Send_HTTP_Request2.call_all_attributes(attributesAll,"http://localhost:3030/getAttributes/"+Integer.toString(getId()));
        } catch (Exception e) {
            System.out.println("-- Error al conectar con los microservicios de información --");
       }
        
        System.out.println("ANTES DE CONECTAR?: ");
        
        
        //Inicio intento2
        if (Files.notExists(Paths.get("dist/plugins"))) {
            // automatically created
            File dir = new File("dist\\plugins");
            boolean isCreated = dir.mkdirs(); 
            System.out.println("Fue creado?"+isCreated); //check the path with System.out
        } 
                
        //Revisar Plugins en la carpeta Plugins
        File libs = new File("dist/plugins");
        File[] jars;
        jars = libs.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".jar");
            }       
        });

        
        //WebSocketServer Initialize and Start
        websock = new WebSocketServerInit("");
        
        websock.run();
        
        //ThreadWebSocket = new Thread(websock);
        //ThreadWebSocket.start();
        
        
        //EL OBSERVADOR DE LOS SENSORES DESDE EL PROCESO PADRE
        
        String pahtName;
        
        if (jars.length >=1){
            for (int i=0; i<jars.length; i++) {
                System.out.println("Entro al LOOP de sensores NUMERO: "+ (i+1));
                pahtName = ((jars[i].toString().split("\\\\"))[2]).split("\\.")[0];
                System.out.println("NOMBRE DEL PATH AGREGADO: " + pahtName);
                Sensors.add(pahtName);
                sensor = new SensorSubject("Jaime",jars[i], getId());
                sensor.addObvserver(obsp);
                sensores.add(sensor);
                sensor.start();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ex) {
            Logger.getLogger(BGFApp.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        Runnable process = new DirectoryWatcher();
        Thread watcherProcess;
        
        watcherProcess = new Thread(process);
        watcherProcess.start();
        
        launch(args);
        
        
        
        socket.disconnect();
        socket.close();
        websock.stop();
        watcherProcess.interrupt();
        //ThreadWebSocket.interrupt();
        System.out.println("WebSocket interrupt! ");
        
        //Runtime.getRuntime().exec("cmd.exe /c" + "cd websocket" +"&&" +"npm stop"); // npm stop
        //final intento 3
        
        for (int i = 0; i < sensores.size(); i++) {
            System.out.println("Entro al LOOP de Matar sensores: " + i);
            sensores.get(i).Stopp();
        }
        System.out.println("Salio del For para cerrar Sensores!");
        
        //System.exit(0);
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
    
}
