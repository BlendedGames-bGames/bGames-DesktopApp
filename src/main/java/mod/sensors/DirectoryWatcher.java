/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;

/**
 *
 * @author Bad-K
 */

import static com.webclient.userinterface.BGFApp.WEBSOCKET_HOST;
import static com.webclient.userinterface.BGFApp.idPlayer;
import static com.webclient.userinterface.BGFApp.obsp;
import static com.webclient.userinterface.BGFApp.nameAccount;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.webclient.userinterface.BGFApp.sensorsSubs;

public class DirectoryWatcher implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class.getName());
    File libs = new File("dist/plugins");
    File[] jars;
    public static ArrayList<String> SensorsStringList= new ArrayList(); //BORRAR
    String pahtName;
    private Thread watcherThread;
    private static SensorSubject sensor;
   
   public void doWath(String directory) throws IOException {

       //System.out.println("WatchService in " + directory);
       
       // get the directory
       Path directoryToWatch = Paths.get(directory);
       if (directoryToWatch == null) {
           throw new UnsupportedOperationException("Directory not found");
       }

       // request the WatchService service
       WatchService watchService = directoryToWatch.getFileSystem().newWatchService();

       // Register the events that we want to monitor
       directoryToWatch.register(watchService, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY});

       //System.out.println("Started WatchService in " + directory);
            
       watcherThread = new Thread() {
            @Override
            public void run() {

                    while (!interrupted()) {
                        
                    // wait for key to be signalled
                        WatchKey key;
                        try {
                            key = watchService.take();
                        } catch (InterruptedException x) {
                            LOGGER.log(Level.SEVERE, null, x);
                            return;
                        }

                        
                    for (WatchEvent event : key.pollEvents()) {
                        String eventKind = event.kind().toString();
                        String file = event.context().toString();
                        //System.out.println("Event : " + eventKind + " in File " +  file);
                        
                        if ("ENTRY_MODIFY".equals(eventKind)){
                            
                            //System.out.println("Event-Modify: "+ event.context().getClass());
                        }
                        else if ("ENTRY_CREATE".equals(eventKind)){
                            
                            //System.out.println("Esto que es: "+ event.context().getClass());
                            libs = new File("dist/plugins");
                            jars = libs.listFiles(new FileFilter() {
                            public boolean accept(File pathname) {
                                return pathname.getName().toLowerCase().endsWith(".jar");
                                }       
                            });
                            start_sensor_found(file);
                            //File pathname = pathname.getName().toLowerCase().endsWith(".jar");
                        }
                        
                        else if ("ENTRY_DELETE".equals(eventKind)){
                            //System.out.println("Event-Delete: "+ event.context().getClass());

                        }
                    }
              
                }
            }

        };
        watcherThread.start();
       
   }

    @Override
    public void run() {
        DirectoryWatcher fileChangeWatcher = new DirectoryWatcher();
        try {
            fileChangeWatcher.doWath(System.getProperty("user.dir")+"\\dist\\plugins");
        } catch (IOException ex) {
            Logger.getLogger(DirectoryWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //Start plugins instantly when included
    private void start_sensor_found(String filename){
        //System.out.println("jars.length: " + jars.length);
        if (jars.length >=1){
            for (int i=0; i<jars.length; i++) {
                pahtName = ((jars[i].toString().split("\\\\"))[2]).split("\\.")[0]+".jar";
                if(pahtName.equals(filename)){
                    SensorsStringList.add(pahtName);
                    //System.out.println("Sensorpaht:"+ pahtName);                    
                    sensor = new SensorSubject(nameAccount,jars[i], idPlayer);
                    sensor.setHostWebSocket(WEBSOCKET_HOST);
                    sensorsSubs.add(sensor);
                    sensor.addObvserver(obsp);
                    sensor.start();
                }
            }
                
        }
        

    }
    
}

