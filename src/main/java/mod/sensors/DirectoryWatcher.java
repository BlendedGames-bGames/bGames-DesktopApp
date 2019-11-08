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

import static com.webclient.userinterface.BGFApp.sensor;
import static com.webclient.userinterface.BGFApp.sensores;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectoryWatcher implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class.getName());
    File libs = new File("dist/plugins");
    File[] jars;
    ObsPlayer obsp= new ObsPlayer();
    public static ArrayList<String> Sensores= new ArrayList(); //BORRAR
    String pahtName;
    private WatchService watcher;
    private Thread watcherThread;
   
   public void doWath(String directory) throws IOException {

       System.out.println("WatchService in " + directory);
       
       // Obtenemos el directorio
       Path directoryToWatch = Paths.get(directory);
       if (directoryToWatch == null) {
           throw new UnsupportedOperationException("Directory not found");
       }

       // Solicitamos el servicio WatchService
       WatchService watchService = directoryToWatch.getFileSystem().newWatchService();

       // Registramos los eventos que queremos monitorear
       directoryToWatch.register(watchService, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY});

       System.out.println("Started WatchService in " + directory);
            
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
                        System.out.println("Event : " + eventKind + " in File " +  file);
                        
                        if ("ENTRY_MODIFY".equals(eventKind)){
                            
                            System.out.println("Esto que es: "+ event.context().getClass());
                        }
                        else if ("ENTRY_CREATE".equals(eventKind)){
                            
                            System.out.println("Esto que es: "+ event.context().getClass());
                            libs = new File("dist/plugins");
                            jars = libs.listFiles(new FileFilter() {
                            public boolean accept(File pathname) {
                                return pathname.getName().toLowerCase().endsWith(".jar");
                                }       
                            });
                            if (jars.length >=1){
                                 for (int i=0; i<jars.length; i++) {
                                     System.out.println("Entro al LOOP de sensores NUMERO: "+ (i+1));
                                     pahtName = ((jars[i].toString().split("\\\\"))[2]).split("\\.")[0];
                                     System.out.println("NOMBRE DEL PATH AGREGADO: " + pahtName);
                                     Set<String> hashSetAux;
                                     hashSetAux = new HashSet<>(Sensores);
                                     ArrayList<String> SensAux = Sensores;
                                     SensAux.add(pahtName);
                                     Set<String> hashSet;
                                     hashSet = new HashSet<>(SensAux);
                                     if(!hashSet.equals(hashSetAux)){
                                         Sensores.add(pahtName);
                                         System.out.println("Sensoressssssssssss:"+ pahtName);
                                         sensor = new SensorSubject("Jaime",jars[i], 1);
                                         sensores.add(sensor);
                                         sensor.addObvserver(obsp);
                                         sensor.start();
                                     }
                                 }
                             }
                            //File pathname = pathname.getName().toLowerCase().endsWith(".jar");
                        }
                        
                        else if ("ENTRY_DELETE".equals(eventKind)){
                            System.out.println("Esto que es: "+ event.context().getClass());

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
        System.out.println("ESTA CUSTION FEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: "+ System.getProperty("user.dir")+"\\dist\\plugins");
        try {
            fileChangeWatcher.doWath(System.getProperty("user.dir")+"\\dist\\plugins");
        } catch (IOException ex) {
            Logger.getLogger(DirectoryWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("NADANADA");
    }

}

