/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;
import bgapp.utiilities.SensorNeed;
import bgapp.utiilities.CrunchifyFindClassesFromJar;
import bgapp.utiilities.AttributePlayer;
import static com.webclient.userinterface.BGFApp.WEBSOCKET_HOST;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;


/**
 *
 * @author David Calistro
 */
public class SensorSubject implements Subject,Runnable {

    private String playername;
    private int id;
    private File archivename;
    private Thread ThreadExPlug;
    private ArrayList<Observer> Observerss; //Observers Of Sensors
    private Class<?> pluginClass;
    private Object ob = null;
    private String HostWebSocket = WEBSOCKET_HOST; ///WEBSOCKET_DIR
    
    //Para inicializar el Sensor observable se le deben asignar el nombre del jugador y el nombre del archivo el cuel es un .jar
    public SensorSubject(String playername, File archivename, int id){
        this.id = id;
        this.playername = playername;
        this.archivename = archivename;
        this.Observerss=new ArrayList();
    }
        
    @Override
    public void notify(AttributePlayer AP) {
        for(Observer o: Observerss){o.update(AP);}
    }
    @Override
    public void notifyOk() {
        for(Observer o: Observerss){o.update();}
    }

    @Override
    public void addObvserver(Observer obs) {
        Observerss.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        Observerss.remove(obs);
    }
    public void notify(SensorNeed SN) {
        for(Observer o: Observerss){o.update(SN);}
    }

    //Metodo que inicia la ejecución del hilo con el comportamiendo definido
    public void start() {
        setThreadExPlug(new Thread(this));
        getThreadExPlug().start();
    }
    
    public void Stopp() {
        
        try {
            Method metodo = this.pluginClass.getDeclaredMethod("StopThis");
            metodo.invoke(this.ob);
            
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    
    Aquí se ejecuta cada hilo con cada ".JAR", por lo que si vas a agregar un nuevo plugin mira aquí.
    
    Si buscas cambiar lo que hace cada hijo o revisar el orden de ejecución, este es el lugar que tienes que editar!
        
    */
    @Override
    public void run() {
        final JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        //System.out.println("Name of archive .jar detected: "+ this.archivename.getAbsolutePath());
        CrunchifyFindClassesFromJar thasas = new CrunchifyFindClassesFromJar();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = thasas.getCrunchifyClassNamesFromJar(this.archivename.getAbsolutePath(),"initializeValues");
        //System.out.println("PATH or jar.: "+path);
        if (path == null){
            //System.out.println(this.archivename.getAbsolutePath()+": THERE IS NO MINIMUM CONDITION IN THIS JAR FOR THE JAR TO BE A PLUGGIN");
            JOptionPane.showMessageDialog(null, this.archivename.getAbsolutePath()+": THERE IS NO MINIMUM CONDITION IN THIS JAR FOR THE JAR TO BE A PLUGGIN");
        } else{
            //System.out.println(this.archivename.getAbsolutePath()+": PLUGIN STARTED WITH SUCCESS");
            
            JOptionPane.showMessageDialog(dialog, this.archivename.getAbsolutePath()+": PLUGIN STARTED WITH SUCCESS");
            URL myJarFile;
            
            try {
                myJarFile = new URL("jar","","file:"+this.archivename.getAbsolutePath()+"!/");
                ClassLoader cl = URLClassLoader.newInstance(new URL[]{myJarFile},this.getClass().getClassLoader());
                
                try {
                    //CLASE INICIALIZADA DESDE EL .JAR
                    pluginClass = Class.forName(path, false, cl);
                    //OBJETO INICIALIZADO AL UTILIZAR LA CLASE ANTERIOR
                    this.ob = pluginClass.newInstance();
                                     
                    Method metodo = pluginClass.getDeclaredMethod("initializeValues");
                    metodo.invoke(this.ob);
                    
                    Class[] argTypes = new Class[] { Observerss.getClass() };
                    metodo = pluginClass.getDeclaredMethod("setSensorNeedObservers",argTypes);
                    metodo.invoke(this.ob,Observerss);
                    
                    metodo = pluginClass.getDeclaredMethod("getSensorNeed");
                    SensorNeed AuxOB =(SensorNeed)metodo.invoke(this.ob);
                    notify(AuxOB);
                    
                    metodo = pluginClass.getDeclaredMethod("run",int.class);
                    metodo.invoke(this.ob,this.id);
                     
                    getThreadExPlug().interrupt();
                    //System.out.println("\n\nEnd Ejecution of sensor: "+ getThreadExPlug().getId() +" \n\n");


                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } catch (MalformedURLException ex) {
                Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
           
    }
    
        /**
     * @return the HostWebSocket
     */
    public String getHostWebSocket() {
        return HostWebSocket;
    }

    /**
     * @param HostWebSocket the HostWebSocket to set
     */
    public void setHostWebSocket(String HostWebSocket) {
        this.HostWebSocket = HostWebSocket;
    }
        /**
     * @return the ThreadExPlug
     */
    public Thread getThreadExPlug() {
        return ThreadExPlug;
    }

    /**
     * @param ThreadExPlug the ThreadExPlug to set
     */
    public void setThreadExPlug(Thread ThreadExPlug) {
        this.ThreadExPlug = ThreadExPlug;
    }

    
    //Matar al hijo
    public void stopSon(){
        getThreadExPlug().interrupt();
    }
    /**
     * @return the playername
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * @param playername the playername to set
     */
    public void setPlayername(String playername) {
        this.playername = playername;
    }

    
}
